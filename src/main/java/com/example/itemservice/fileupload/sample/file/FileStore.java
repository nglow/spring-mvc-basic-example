package com.example.itemservice.fileupload.sample.file;

import com.example.itemservice.fileupload.sample.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {


    private final String fileDir;

    public FileStore(@Value("${file.dir}") String fileDir) {
        this.fileDir = System.getProperty("user.dir") + fileDir;
    }

    public String getFullPath(String filename) {
        return this.fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            storeFileResult.add(storeFile(multipartFile));
        }

        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) return null;

        var originalFilename = multipartFile.getOriginalFilename();
        var storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        var fileExtension = extractFileExtension(originalFilename);
        return uuid + "." + fileExtension;
    }

    private String extractFileExtension(String originalFilename) {
        var position = originalFilename.lastIndexOf(".");
        return originalFilename.substring(position + 1);
    }

}
