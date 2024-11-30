package com.carpooling.ums.services;

import com.carpooling.ums.services.RefreshablePropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    private final S3Client s3Client;
    private final RefreshablePropertyService propertyService;

    @Autowired
    public S3Service(S3Client s3Client, RefreshablePropertyService propertyService) {
        this.s3Client = s3Client;
        this.propertyService = propertyService;
    }

    public String uploadFile(MultipartFile file) {
        // Fetch bucket name dynamically from property service
        String bucketName = propertyService.getProperty("bucket-name");

        if (bucketName == null || bucketName.isEmpty()) {
            logger.error("S3 bucket name is not configured");
            throw new IllegalStateException("S3 bucket name must be configured.");
        }

        try {
            // Generate a unique filename to avoid conflicts
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

            // Log the file upload process
            logger.info("Uploading file to S3 with name: {}", fileName);

            // Create a PutObjectRequest
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            // Convert MultipartFile to InputStream and upload to S3
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // Return the file URL after upload
            String fileUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
            logger.info("File uploaded successfully. File URL: {}", fileUrl);
            return fileUrl;

        } catch (IOException e) {
            logger.error("Error uploading file to S3: {}", e.getMessage());
            throw new RuntimeException("Error uploading file to S3", e);
        } catch (S3Exception e) {
            logger.error("Failed to upload file: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to upload file: " + e.awsErrorDetails().errorMessage());
        }
    }
}
