package org.example.filesystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.filesystem.dto.ImageDto;
import org.example.filesystem.services.MinioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Сервис для получения аватарки")
@RequiredArgsConstructor
@RequestMapping(value = "/file")
@RestController
@Slf4j
public class ImageController {
    private final MinioService minioService;
    private String bucket = "Image";
    @GetMapping("/getImage")
    @Operation(description = "Метод для получения фото")
    public byte[] downloadImage(@RequestParam String objectName) {
        return minioService.downloadFromMinio(bucket, objectName);
    }

    @PostMapping("/uploadImage")
    @Operation(description = "Меод для добавления фото")
    public void uploadImage(@RequestParam MultipartFile image){
        try {
            minioService.uploadToMinio(bucket, "imageMinioName", image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/deleteImage")
    @Operation(description = "Метод для удаление фото")
    public void deleteImage(@RequestParam String objectName){
        minioService.deleteFromMinio(bucket, objectName);
    }
}
