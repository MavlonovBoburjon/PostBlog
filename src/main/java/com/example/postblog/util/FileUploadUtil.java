package com.example.postblog.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class FileUploadUtil {

    public static String saveFile(String uploadDir, String originalFilename, byte[] bytes) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        String fileName = UUID.randomUUID().toString() + ext;
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, bytes, StandardOpenOption.CREATE);
        return fileName;
    }

    public static void deleteFileIfExists(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (Exception ignored) {}
    }
}
