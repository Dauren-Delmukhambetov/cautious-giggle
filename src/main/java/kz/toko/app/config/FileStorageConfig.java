package kz.toko.app.config;

import io.minio.MinioClient;
import lombok.Setter;
import okhttp3.HttpUrl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
@ConfigurationProperties(prefix = "storage")
public class FileStorageConfig {

    private String endpoint;
    private String accessKey;
    private String secretKey;

    @Bean
    public MinioClient client() {
        return MinioClient.builder()
                        .endpoint(HttpUrl.parse(this.endpoint))
                        .credentials(this.accessKey, this.secretKey)
                        .build();
    }

}
