package com.example.postblog.service;

import com.example.postblog.model.Picture;
import com.example.postblog.repository.PictureRepo;
import com.example.postblog.util.FileUploadUtil;
import com.example.postblog.repository.PictureRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class PictureService {

    private final PictureRepo pictureRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public Picture store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        String savedFileName = FileUploadUtil.saveFile(uploadPath, file.getOriginalFilename(), file.getBytes());
        Picture pic = Picture.builder()
                .fileName(savedFileName)
                .originalName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .filePath(Paths.get(uploadPath).resolve(savedFileName).toAbsolutePath().toString())
                .createdAt(LocalDateTime.now())
                .build();
        return pictureRepo.save(pic);
    }

    public void delete(Picture picture) {
        if (picture == null) return;
        FileUploadUtil.deleteFileIfExists(picture.getFilePath());
        pictureRepo.delete(picture);
    }
}
