package com.carpooling.ums.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carpooling.ums.configurer.S3Config;

@RestController
public class S3RefreshController {

    private final S3Config s3Config;

    @Autowired
    public S3RefreshController(S3Config s3Config) {
        this.s3Config = s3Config;
    }

    @PostMapping("/refresh-s3-client")
    public ResponseEntity<String> refreshS3Client() {
        s3Config.refreshS3Client();
        return ResponseEntity.ok("S3 Client refreshed successfully.");
    }
}
