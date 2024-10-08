package com.carpooling.ums.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${}")
    private String bucketName;

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) {
        try {
            // Generate a unique filename to avoid conflicts
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

            // Create a PutObjectRequest
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            // Convert MultipartFile to InputStream and upload to S3
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // Return the file URL after upload
            return "https://" + bucketName + ".s3.amazonaws.com/" + fileName; // Adjust the URL format if necessary
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to S3", e);
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.awsErrorDetails().errorMessage());
        }
    }
}
