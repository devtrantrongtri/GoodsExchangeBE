package com.uth.BE.Service;

import com.uth.BE.Service.Interface.IStoreService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

@Service
public class StoreService implements IStoreService {

    @Value("${store.file-path}")
    private String rootFilePath;

    private Path root;

    @PostConstruct
    public void init() {
        try {
            root = Paths.get(rootFilePath);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize file storage directory", e);
        }
    }

    @Override
     public boolean saveFile(MultipartFile file, String filename) {
        try {
            Path destinationFile = root.resolve(filename);
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public Resource loadFile(String filename) {
        try {
            Path file = root.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new NoSuchFileException("File not found or not readable: " + filename);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file", e);
        }
    }

    @Override
    public boolean deleteFile(String filename) {
        try {
            Path file = root.resolve(filename).normalize();
            Files.deleteIfExists(file);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

}
