package com.example.itemservice.web;

import com.example.itemservice.web.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validation/api/items")
@Slf4j
public class ValidationItemApiController {

    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {
        log.info("API has been invoked");

        if (bindingResult.hasErrors()) {
            log.info("Error occurred. errors={}", bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("Success logic executed");
        return form;
    }
}
