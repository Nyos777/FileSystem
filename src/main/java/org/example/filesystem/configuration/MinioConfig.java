package org.example.filesystem.configuration;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = {MinioProperties.class})
public class MinioConfig {
    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                        .endpoint(minioProperties.getUrl())
                        .credentials(minioProperties.getUsername(), minioProperties.getPassword())
                        .build();
    }
}
