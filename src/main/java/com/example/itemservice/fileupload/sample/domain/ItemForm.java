package com.example.itemservice.fileupload.sample.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemForm {

    private Long itemId;
    private String itemName;
    MultipartFile attachFile;
    private List<MultipartFile> imageFiles;
}
