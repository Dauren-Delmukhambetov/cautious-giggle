package kz.toko.app.service.impl;

import io.minio.*;
import kz.toko.app.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioFileStorageService implements FileStorageService {

    private final MinioClient client;

    @Value("${storage.bucket-name}")
    private String bucketName;

    @SneakyThrows
    @PostConstruct
    public void init() {
        final var bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        if (!this.client.bucketExists(bucketExistsArgs)) {
            log.debug("Bucket with the name {} does not exist", this.bucketName);
            final var makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
            this.client.makeBucket(makeBucketArgs);
            log.debug("Bucket with the name {} has been created", this.bucketName);
        }
    }

    @SneakyThrows
    @Override
    public String write(final MultipartFile fileToSave) throws IOException {
        try (final var fileStream = fileToSave.getInputStream()) {
            final var putObjectArgs = PutObjectArgs.builder()
                    .bucket(this.bucketName)
                    .object(fileToSave.getOriginalFilename())
                    .stream(fileStream, fileToSave.getSize(), -1)
                    .contentType(fileToSave.getContentType())
                    .build();

            final var response = client.putObject(putObjectArgs);
            log.debug("File {} has been uploaded to the bucket {} with version {}",
                    response.object(), response.bucket(), response.versionId());
        }

        return String.format("%s/%s", this.bucketName, fileToSave.getOriginalFilename());
    }

    @SneakyThrows
    @Override
    public void delete(final String filePath) {
        final var removeObjectArgs = RemoveObjectArgs.builder().bucket(this.bucketName)
                .object(filePath)
                .build();

        client.removeObject(removeObjectArgs);
        log.debug("File {} has been deleted from the bucket {}", filePath, this.bucketName);
    }
}
