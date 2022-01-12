package com.example.itemservice.validation;

import com.example.itemservice.domain.item.Item;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidationTest {

    @Test
    void beanValidation() {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        var validator = validatorFactory.getValidator();

        var item = new Item();
        item.setItemName(" ");
        item.setPrice(0);
        item.setQuantity(10000);

        var violations = validator.validate(item);
        for (ConstraintViolation<Item> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation =" + violation.getMessage());
        }
    }
}
