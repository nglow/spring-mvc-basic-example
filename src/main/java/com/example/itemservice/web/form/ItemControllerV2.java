package com.example.itemservice.web.form;

import com.example.itemservice.domain.item.DeliveryCode;
import com.example.itemservice.domain.item.Item;
import com.example.itemservice.domain.item.ItemRepository;
import com.example.itemservice.domain.item.ItemType;
import com.example.itemservice.web.form.validation.ItemValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
@RequestMapping("/form/v2/items")
@Slf4j
@RequiredArgsConstructor
public class ItemControllerV2 {
    
    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemValidator);
    }

    @PostConstruct
    public void initialize() {
        itemRepository.save(new Item("itemC", 30000, 30));
        itemRepository.save(new Item("itemD", 40000, 40));
    }

    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");

        return regions;
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));

        return deliveryCodes;
    }

    @GetMapping
    public String items(Model model) {
        var items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "form-v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        var item = itemRepository.findById(itemId);
        model.addAttribute(item);
        return "form-v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());

        return "form-v2/addForm";
    }

    //    @PostMapping("/add")
    public String saveV1(@RequestParam String itemName,
                         @RequestParam Integer price,
                         @RequestParam Integer quantity,
                         Model model) {
        var item = new Item(itemName, price, quantity);
        itemRepository.save(item);

        model.addAttribute(item);
        return "form-v2/item";
    }

    //    @PostMapping("/add")
    public String saveV2(@ModelAttribute Item item) {
        itemRepository.save(item);

        return "form-v2/item";
    }

    //    @PostMapping("/add")
    public String saveV3(Item item) {
        itemRepository.save(item);

        return "form-v2/item";
    }

    //    @PostMapping("/add")
    public String saveV4(Item item) {
        itemRepository.save(item);

        return "redirect:/form/v2/items/" + item.getId();
    }

//    @PostMapping("/add")
    public String addItem(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 검증 로직
        if (!StringUtils.hasText(item.getItemName()))
            bindingResult.addError(new FieldError("item", "itemName", "Item name is necessary"));

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000)
            bindingResult.addError(new FieldError("item", "price", "Price range is 1,000~ 1,000,000"));

        if (item.getQuantity() == null || item.getQuantity() > 9999)
            bindingResult.addError(new FieldError("item", "quantity", "Max quantity of item is 9,999"));

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            var totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000)
                bindingResult.addError(new ObjectError("item", "Total price must be 1,0000 or more"));

        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("errors = {}", bindingResult);
            return "form-v2/addForm";
        }

        // 성공 로직
        var itemPersisted = itemRepository.save(item);
        log.info("Item is saved. item={}", item);

        model.addAttribute("message", "Item is saved");
        redirectAttributes.addAttribute("itemId", itemPersisted.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/form/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV2(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 검증 로직
        if (!StringUtils.hasText(item.getItemName()))
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000)
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 10000}, null));

        if (item.getQuantity() == null || item.getQuantity() > 9999)
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            var totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000)
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, totalPrice}, null));
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("errors = {}", bindingResult);
            return "form-v2/addForm";
        }

        // 성공 로직
        var itemPersisted = itemRepository.save(item);
        log.info("Item is saved. item={}", item);

        model.addAttribute("message", "Item is saved");
        redirectAttributes.addAttribute("itemId", itemPersisted.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/form/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV3(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName()))
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000)
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 10000}, null));

        if (item.getQuantity() == null || item.getQuantity() > 9999)
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            var totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000)
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, totalPrice}, null));
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("errors = {}", bindingResult);
            return "form-v2/addForm";
        }

        // 성공 로직
        var itemPersisted = itemRepository.save(item);
        log.info("Item is saved. item={}", item);

        model.addAttribute("message", "Item is saved");
        redirectAttributes.addAttribute("itemId", itemPersisted.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/form/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 검증 로직
//        if (!StringUtils.hasText(item.getItemName()))
//            bindingResult.rejectValue("itemName", "required");

        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000)
            bindingResult.rejectValue("price", "range", new Object[]{1000, 10000}, null);

        if (item.getQuantity() == null || item.getQuantity() > 9999)
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            var totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000)
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalPrice}, null);
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("errors = {}", bindingResult);
            return "form-v2/addForm";
        }

        // 성공 로직
        var itemPersisted = itemRepository.save(item);
        log.info("Item is saved. item={}", item);

        model.addAttribute("message", "Item is saved");
        redirectAttributes.addAttribute("itemId", itemPersisted.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/form/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV5(Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        itemValidator.validate(item, bindingResult);

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("errors = {}", bindingResult);
            return "form-v2/addForm";
        }

        // 성공 로직
        var itemPersisted = itemRepository.save(item);
        log.info("Item is saved. item={}", item);

        model.addAttribute("message", "Item is saved");
        redirectAttributes.addAttribute("itemId", itemPersisted.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/form/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV6(@Validated Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("errors = {}", bindingResult);
            return "form-v2/addForm";
        }

        // 성공 로직
        var itemPersisted = itemRepository.save(item);
        log.info("Item is saved. item={}", item);

        model.addAttribute("message", "Item is saved");
        redirectAttributes.addAttribute("itemId", itemPersisted.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/form/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        var item = itemRepository.findById(itemId);
        model.addAttribute(item);

        return "/form-v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/v2/items/{itemId}";
    }
}
