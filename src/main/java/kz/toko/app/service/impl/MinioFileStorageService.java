package kz.toko.app.service.impl;

import io.minio.*;
import kz.toko.app.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Paths;

import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FilenameUtils.getExtension;

@Slf4j
@Service
@Profile("!aws")
@RequiredArgsConstructor
public class MinioFileStorageService implements FileStorageService {

    private final MinioClient minioClient;

    @Value("${storage.bucket-name}")
    private String bucketName;

    @SneakyThrows
    @PostConstruct
    public void init() {
        final var bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        if (!this.minioClient.bucketExists(bucketExistsArgs)) {
            log.info("Bucket with the name {} does not exist", this.bucketName);
            final var makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
            this.minioClient.makeBucket(makeBucketArgs);
            log.info("Bucket with the name {} has been created", this.bucketName);
        }
    }

    @SneakyThrows
    @Override
    public String write(final MultipartFile fileToSave) throws IOException {
        final var filename = String.format("%s.%s", randomUUID(), getExtension(fileToSave.getOriginalFilename()));
        try (final var fileStream = fileToSave.getInputStream()) {
            final var putObjectArgs = PutObjectArgs.builder()
                    .bucket(this.bucketName)
                    .object(filename)
                    .stream(fileStream, fileToSave.getSize(), -1)
                    .contentType(fileToSave.getContentType())
                    .build();

            final var response = minioClient.putObject(putObjectArgs);
            log.info("File {} has been uploaded to the bucket {} with version {}",
                    response.object(), response.bucket(), response.versionId());
        }
        return String.format("%s/%s", this.bucketName, filename);
    }

    @SneakyThrows
    @Override
    public void delete(final String filePath) {
        final var filePathToDelete = Paths.get(filePath).getFileName().toString();
        final var removeObjectArgs = RemoveObjectArgs.builder().bucket(this.bucketName)
                .object(filePathToDelete)
                .build();

        minioClient.removeObject(removeObjectArgs);
        log.info("File {} has been deleted from the bucket {}", filePathToDelete, this.bucketName);
    }
}
