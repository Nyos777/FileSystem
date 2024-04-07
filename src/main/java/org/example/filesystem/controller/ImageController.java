package org.example.filesystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.filesystem.dto.ImageDto;
import org.example.filesystem.services.MinioService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Tag(name = "Сервис для получения аватарки")
@RequiredArgsConstructor
@RequestMapping(value = "/file")
@RestController
@Slf4j
public class ImageController {
    private final MinioService minioService;
    private String bucket = "image";
    @GetMapping("/getImage")
    @Operation(description = "Метод для получения фото")
    public ResponseEntity<byte[]> downloadImage(@RequestParam("imageMinioName")  String imageNameMinio) {
        byte[] imageData = minioService.downloadFromMinio(bucket, imageNameMinio);
        log.info("Success download from minio");
        // Установка HTTP заголовков
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(imageNameMinio).build());
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Метод для добавления фото")
    public ResponseEntity<String> uploadImage(@RequestPart("image") MultipartFile image){
        UUID uuid = UUID.randomUUID();
        String objectName = "minio_" + image.getOriginalFilename() + "_" + uuid;
        try {
            minioService.uploadToMinio(bucket, objectName, image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(objectName);
    }

    @DeleteMapping("/deleteImage")
    @Operation(description = "Метод для удаление фото")
    public void deleteImage(@RequestParam String objectName){
        minioService.deleteFromMinio(bucket, objectName);
    }

    @PostMapping("/jsonFeign")
    @Operation(description = "Метод для получения JSON файла и отправка его же обратно")
    public String sendJson(@RequestBody String jsonFile){
        return "Received Json: \n" + jsonFile;
    }
}
