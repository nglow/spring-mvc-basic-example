package com.example.itemservice.fileupload.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Controller
@RequestMapping("/servlet/v2")
@Slf4j
public class ServletUploadControllerV2 {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("request={}", request);

        var itemName = request.getParameter("itemName");
        log.info(itemName);

        var parts = request.getParts();
        log.info("parts={}", parts);

        for (Part part : parts) {
            log.info("==== PART ====");
            log.info("name={}", part.getName());
            var headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("header {}: {}", headerName, part.getHeader(headerName));
            }
            // 펀의 메서드
            log.info("submittedFilename={}", part.getSubmittedFileName());
            log.info("size={}", part.getSize());

            // 데이터 읽기
            var inputStream = part.getInputStream();
            var body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            log.info("body={}", body);

            // 파일에 저장하기
            if (StringUtils.hasText(part.getSubmittedFileName())) {
                var fullPath = System.getProperty("user.dir") + fileDir + part.getSubmittedFileName();
                log.info("fullPath={}", fullPath);
                part.write(fullPath);
            }
        }
        return "upload-form";
    }
}
