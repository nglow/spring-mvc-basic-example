package com.example.itemservice.fileupload.sample.controller;

import com.example.itemservice.fileupload.sample.domain.Item;
import com.example.itemservice.fileupload.sample.domain.ItemForm;
import com.example.itemservice.fileupload.sample.domain.ItemRepository2;
import com.example.itemservice.fileupload.sample.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController2 {

    private final ItemRepository2 itemRepository;
    private final FileStore fileStore;

    @GetMapping("/sample/items/new")
    public String newItem(@ModelAttribute ItemForm form) {
        return "item-form";
    }

    @PostMapping("/sample/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        var attachFile = fileStore.storeFile(form.getAttachFile());
        var storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        // Save to database
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getId());

        return "redirect:/sample/items/{itemId}";
    }

    @GetMapping("/sample/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        var item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view";
    }

    // 보안에 취약, 체크 로직을 넣어줘야함
    @GetMapping("/images/{filename}")
    @ResponseBody
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        var item = itemRepository.findById(itemId);
        var storeFileName = item.getAttachFile().getStoreFileName();
        var uploadFileName = item.getAttachFile().getUploadFileName();

        var resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={}", uploadFileName);

        // 한글 깨짐 방지
        String encodedUploadFilename = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);

        String contentDisposition = "attachment; filename=\"" + encodedUploadFilename + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
