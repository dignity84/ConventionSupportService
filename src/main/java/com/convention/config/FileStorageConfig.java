package com.convention.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 파일 IO 관련 Configuration
 * 
 * @author KimJungJune <91525531@hanmail.net>
 *
 */
@ConfigurationProperties(prefix = "file")
public class FileStorageConfig {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
