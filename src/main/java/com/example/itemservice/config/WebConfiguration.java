package com.example.itemservice.config;

import com.example.itemservice.web.validation.ItemValidator;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public Validator getValidator() {
        return new ItemValidator();
    }
}
