package kz.toko.app.config;

import io.minio.MinioClient;
import lombok.Setter;
import okhttp3.HttpUrl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Setter
@Configuration
@ConfigurationProperties(prefix = "storage")
public class FileStorageConfig {

    private String endpoint;
    private String accessKey;
    private String secretKey;

    @Bean
    @Profile("!aws")
    public MinioClient minioClient() {
        return MinioClient.builder()
                        .endpoint(HttpUrl.parse(this.endpoint))
                        .credentials(this.accessKey, this.secretKey)
                        .build();
    }

    @Bean
    @Profile("aws")
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(awsCredentialsProvider())
                .build();
    }

    @Bean
    @Profile("aws")
    public AwsCredentialsProvider awsCredentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(this.accessKey, this.secretKey));
    }


}
