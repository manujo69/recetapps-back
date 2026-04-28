package com.recipes.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.recipes.dto.StoredFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:${user.home}/uploads}")
    private String uploadDir;

    @Autowired(required = false)
    private Cloudinary cloudinary;

    @SuppressWarnings("unchecked")
    public StoredFile storeFile(MultipartFile file) throws IOException {
        if (cloudinary != null) {
            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return new StoredFile(
                (String) result.get("public_id"),
                (String) result.get("secure_url")
            );
        }
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir).resolve(filename);
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return new StoredFile(filename, null);
    }

    public Path loadFile(String filename) {
        return Paths.get(uploadDir).resolve(filename);
    }

    @SuppressWarnings("unchecked")
    public void deleteFile(String publicId) throws IOException {
        if (cloudinary != null) {
            try {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            } catch (Exception e) {
                throw new IOException("Failed to delete file from Cloudinary: " + publicId, e);
            }
            return;
        }
        Files.deleteIfExists(loadFile(publicId));
    }
}
