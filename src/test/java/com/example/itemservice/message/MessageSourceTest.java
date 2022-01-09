package com.example.itemservice.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource messageSource;


    @Test
    void helloMessage() {
        var helloEn = messageSource.getMessage("hello", null, Locale.ENGLISH);
        assertThat(helloEn).isEqualTo("hello");

        var hello = messageSource.getMessage("hello", null, Locale.ROOT);
        assertThat(hello).isEqualTo("안녕");

//        messageSource.getMessage("hello", null, )
    }

    @Test
    void notFoundMessageCode() {
        assertThatThrownBy(() -> messageSource.getMessage("no_code", null, Locale.ROOT))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    void testGetDefaultMessage() {
        String result = messageSource.getMessage("no_code", null, "기본 메시지", Locale.ROOT);
        assertThat(result).isEqualTo("기본 메시지");
    }

    @Test
    void argumentMessage() {
        var message = messageSource.getMessage("hello.name", new Object[]{"Spring"}, Locale.ROOT);
        assertThat(message).isEqualTo("안녕 Spring");
    }

    @Test
    void defaultLang() {
        assertThat(messageSource.getMessage("hello", null, Locale.ROOT)).isEqualTo("안녕");
//        assertThat(messageSource.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
    }

    @Test
    void enLang() {
        assertThat(messageSource.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }
}
