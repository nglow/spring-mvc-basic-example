package com.example.itemservice.domain.exception.servlet;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class ServletExceptionController {

    @GetMapping("/error-ex")
    public void errorEx() {
        throw new RuntimeException("예외발생!1");
    }
}
