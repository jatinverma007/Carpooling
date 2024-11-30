package com.carpooling.ums.configurer;

import com.carpooling.ums.services.RefreshablePropertyService;

import jakarta.annotation.PostConstruct;

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
    private S3Client s3Client;

    @Autowired
    public S3Config(RefreshablePropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostConstruct
    public void init() {
        refreshS3Client(); // Initialize the S3 client when the application starts
    }

    // Method to create a new S3Client with updated credentials
    private S3Client createS3Client() {
        // Fetch AWS properties from the refreshable property service
        String awsAccessKey = propertyService.getProperty("access-key");
        String awsSecretKey = propertyService.getProperty("secret-key");
        String bucketName = propertyService.getProperty("bucket-name");

        logger.info("Initializing S3Client with bucket: {}", bucketName);

        if (awsAccessKey == null || awsSecretKey == null || bucketName == null) {
            logger.error("Missing AWS properties: access-key, secret-key, or bucket-name.");
            throw new IllegalStateException("AWS properties are not properly configured.");
        }

        return S3Client.builder()
                .region(Region.AP_SOUTH_1) // Set the region
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
                .build();
    }

    public void refreshS3Client() {
        logger.info("Refreshing S3Client with updated properties.");
        propertyService.refreshProperties(); // Ensure properties are up to date
        this.s3Client = createS3Client(); // Recreate the S3 client with updated properties
        logger.info("S3 Client refreshed successfully.");
    }

    @Bean
    public S3Client s3Client() {
        return this.s3Client;
    }
}
