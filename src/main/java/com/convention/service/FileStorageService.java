package com.convention.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.processing.FilerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.convention.config.FileStorageConfig;

/**
 * 파일 IO 관련 서비스
 * 
 * @author KimJungJune <91525531@hanmail.net>
 *
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }

    public String storeFile(MultipartFile file) {
    	
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // 유효하지 않은 파일명 확인
            if(fileName.contains("..")) {
                throw new FilerException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // 같은 파일명으로 복사
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return String.valueOf(targetLocation);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        return null;
    }

}
