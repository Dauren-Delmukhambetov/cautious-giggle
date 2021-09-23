package kz.toko.app.service.impl;

import kz.toko.app.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static java.util.UUID.randomUUID;
import static org.apache.commons.io.FilenameUtils.getExtension;

@Slf4j
@Service
@Profile("aws")
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {

    private final S3Client s3Client;

    @Value("${storage.bucket-name}")
    private String bucketName;

    @PostConstruct
    public void init() {
        if (!bucketExists(this.bucketName)) { createBucket(this.bucketName); }
    }

    @Override
    public String write(MultipartFile fileToSave) throws IOException {
        final var filename = String.format("%s.%s", randomUUID(), getExtension(fileToSave.getOriginalFilename()));
        try (final var fileStream = fileToSave.getInputStream()) {
            final var putObjectRequest = PutObjectRequest.builder()
                    .bucket(this.bucketName)
                    .key(filename)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType(fileToSave.getContentType())
                    .build();

            final var response = s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(fileStream, fileToSave.getSize()));
            log.info("File {} has been uploaded to the bucket {} with version {}",
                    filename, this.bucketName, response.versionId());
        }
        return String.format("%s", filename);
    }

    @Override
    public void delete(String filePath) {
        final var deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(this.bucketName)
                .key(filePath)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        log.info("File {} has been deleted from the bucket {}", filePath, this.bucketName);
    }

    private boolean bucketExists(final String bucketName) {
        final var listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = s3Client.listBuckets(listBucketsRequest);
        return listBucketsResponse.buckets()
                .stream()
                .map(Bucket::name)
                .anyMatch(bucketName::equals);
    }

    private void createBucket(final String bucketName) {
        try (final var s3Waiter = s3Client.waiter()) {
            CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            s3Client.createBucket(bucketRequest);
            HeadBucketRequest bucketRequestWait = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(bucketRequestWait);
            waiterResponse.matched().response().ifPresent(it -> log.info(it.toString()));
            log.info("Bucket with the name {} has been created", bucketName);
        } catch (S3Exception e) {
            log.error("Error occurred while creating bucket with the name {}: {}", bucketName, e.awsErrorDetails().errorMessage());
        }
    }
}
