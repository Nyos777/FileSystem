package org.example.filesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    private String originalFileName;
    private Long size;
    private String contentType;
    private String minioName;
    private Boolean isPreviewImage;
    private byte[] bytes;
}
