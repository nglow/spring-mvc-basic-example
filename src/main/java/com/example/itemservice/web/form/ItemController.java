package com.example.itemservice.web.form;

import com.example.itemservice.domain.item.DeliveryCode;
import com.example.itemservice.domain.item.Item;
import com.example.itemservice.domain.item.ItemRepository;
import com.example.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
@RequestMapping("/form/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

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

        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        var item = itemRepository.findById(itemId);
        model.addAttribute(item);
        return "form/item";
    }

    @PostConstruct
    public void initialize() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());

        return "form/addForm";
    }

    //    @PostMapping("/add")
    public String saveV1(@RequestParam String itemName,
                         @RequestParam Integer price,
                         @RequestParam Integer quantity,
                         Model model) {
        var item = new Item(itemName, price, quantity);
        itemRepository.save(item);

        model.addAttribute(item);
        return "form/item";
    }

    //    @PostMapping("/add")
    public String saveV2(@ModelAttribute Item item) {
        itemRepository.save(item);

        return "form/item";
    }

    //    @PostMapping("/add")
    public String saveV3(Item item) {
        itemRepository.save(item);

        return "form/item";
    }

    //    @PostMapping("/add")
    public String saveV4(Item item) {
        itemRepository.save(item);

        return "redirect:/form/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItem(Item item, RedirectAttributes redirectAttributes, Model model) {
        // 검증 오류 결과를 보관
        Map<String, String> errors = new HashMap<>();

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) errors.put("itemName", "Item name is necessary");
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) errors.put("price", "Price range is 1,000~ 1,000,000");
        if (item.getQuantity() == null || item.getQuantity() > 9999) errors.put("quantity", "Max quantity of item is 9,999");

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            var totalPrice = item.getPrice() * item.getQuantity();
            if (totalPrice < 10000) errors.put("globalError", "Total price must be 1,0000 or more");
        }

        // 검증에 실패하면 다시 입력 폼으로
        if (!errors.isEmpty()) {
            log.error("errors = {}", errors);
            model.addAttribute("errors", errors);
            return "form/addForm";
        }

        // 성공 로직
        var itemPersisted = itemRepository.save(item);
        log.info("Item is saved. item={}", item);

        model.addAttribute("message", "Item is saved");
        redirectAttributes.addAttribute("itemId", itemPersisted.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        var item = itemRepository.findById(itemId);
        model.addAttribute(item);

        return "/form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }
}
