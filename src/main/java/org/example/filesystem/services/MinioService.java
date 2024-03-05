package org.example.filesystem.services;

import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {
    private final MinioClient minioClient;
    public void createBucket(String bucketName) {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket: {}", bucketName + " created successfully.");
            } else {
                log.info("Bucket: {}", bucketName + " already exists.");
            }
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("Error createBucket on Minio Message: {}", e.getMessage());
        }
    }

    public String uploadToMinio(String bucketName, String objectName, byte[] data) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data)){
            createBucket(bucketName);
            ObjectWriteResponse response = minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(bais, bais.available(), -1)
                            .build()
            );
            log.info("File uploaded successfully to MinIO. ETag: {}", response.etag());
        } catch (Exception e) {
            log.error("File upload to MinIO Error: {}", e.getMessage());
            // Обработка ошибок загрузки файла в MinIO
        }
        return objectName;
    }

    public byte[] downloadFromMinio(String bucketName, String objectName) {
        try (GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build())) {
            // Считываем данные файла в байтовый массив и возвращаем его
            return IOUtils.toByteArray(response);

        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("Error downloading file from MinIO: {}", e.getMessage());
            return null;
        }
    }

    public void deleteFromMinio(String bucketName, String objectName) {
        // Удаляем объекты из Minio
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (XmlParserException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 ServerException e) {
            log.error("Delete from MinIO Error: {}", e.getMessage());
        }
    }
}
