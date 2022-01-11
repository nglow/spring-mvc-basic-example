package com.example.itemservice.validation;


import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject() {
        var messageCodes = codesResolver.resolveMessageCodes("required", "item");
        Arrays.stream(messageCodes).forEach(messageCode -> System.out.println("messageCode = " + messageCode));

        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodeResolverField() {
        var messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        Arrays.stream(messageCodes).forEach(messageCode -> System.out.println("messageCode = " + messageCode));

        assertThat(messageCodes).containsExactly("required.item.itemName", "required.itemName", "required.java.lang.String", "required");
    }


}
