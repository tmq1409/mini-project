package com.example.miniproject2.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            System.out.println("File is null or empty");
            return null;
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename().trim();
        Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
        Files.createDirectories(uploadDir);
        Path filePath = uploadDir.resolve(fileName);

        try {
            file.transferTo(filePath.toFile());
            System.out.println("File uploaded to: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Upload failed: " + e.getMessage());
        }

        return "/uploads/" + fileName;
    }

    public void deleteFile(String avatarUrl) {
        if (avatarUrl == null) return;
        try {
            String fileName = Paths.get(avatarUrl).getFileName().toString();
            Path filePath = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
            Files.deleteIfExists(filePath);
            System.out.println("Deleted avatar: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to delete avatar: " + e.getMessage());
        }
    }

}
