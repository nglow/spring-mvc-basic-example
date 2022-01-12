package com.example.itemservice.web.form;

import com.example.itemservice.domain.item.*;
import com.example.itemservice.web.form.validation.ItemValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/form/v4/items")
@Slf4j
@RequiredArgsConstructor
public class ItemControllerV4 {
    
    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @PostConstruct
    public void initialize() {
        itemRepository.save(new Item("itemE", 50000, 50));
        itemRepository.save(new Item("itemF", 60000, 60));
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

        return "form-v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        var item = itemRepository.findById(itemId);
        model.addAttribute(item);
        return "form-v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());

        return "form-v4/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@Validated Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            var totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000)
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalPrice}, null);
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("errors = {}", bindingResult);
            return "form-v4/addForm";
        }

        // 성공 로직
        var itemPersisted = itemRepository.save(item);
        log.info("Item is saved. item={}", item);

        model.addAttribute("message", "Item is saved");
        redirectAttributes.addAttribute("itemId", itemPersisted.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/form/v4/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV2(@Validated(SaveCheck.class) Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            var totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000)
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalPrice}, null);
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("errors = {}", bindingResult);
            return "form-v4/addForm";
        }

        // 성공 로직
        var itemPersisted = itemRepository.save(item);
        log.info("Item is saved. item={}", item);

        model.addAttribute("message", "Item is saved");
        redirectAttributes.addAttribute("itemId", itemPersisted.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/form/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        var item = itemRepository.findById(itemId);
        model.addAttribute(item);

        return "/form-v4/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {
        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            var totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000)
                bindingResult.reject("totalPriceMin", new Object[]{10000, totalPrice}, null);
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.error("errors = {}", bindingResult);
            return "form-v4/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/form/v4/items/{itemId}";
    }
}
