package org.example.filesystem.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(value = "minio.file-system-project.props")
public class MinioProperties {
        private String url;
        private String username;
        private String password;
}
