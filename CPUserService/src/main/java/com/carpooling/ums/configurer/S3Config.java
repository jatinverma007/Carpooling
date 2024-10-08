package com.carpooling.ums.configurer;

import com.carpooling.ums.services.RefreshablePropertyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    private static final Logger logger = LoggerFactory.getLogger(S3Config.class);

    private final RefreshablePropertyService propertyService;

    @Autowired
    public S3Config(RefreshablePropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Bean
    S3Client s3Client() {
        // Fetch AWS properties from the refresh table or service
        String awsAccessKey = propertyService.getProperty("access-key");
        String awsSecretKey = propertyService.getProperty("secret-key");
        String bucketName = propertyService.getProperty("bucket-name");

        // Log the fetched properties for debugging purposes
        logger.info("Initializing S3Client with bucket: {}", bucketName);

        // Validate that all required properties are present
        if (awsAccessKey == null || awsSecretKey == null || bucketName == null) {
            logger.error("Missing AWS properties: access-key, secret-key, or bucket-name.");
            throw new IllegalStateException("AWS properties are not properly configured.");
        }

        // Return the configured S3Client
        return S3Client.builder()
                .region(Region.AP_SOUTH_1) // Set the region dynamically
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
                .build();
    }
}
