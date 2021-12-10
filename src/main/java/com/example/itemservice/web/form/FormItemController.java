package com.example.itemservice.web.form;

import com.example.itemservice.domain.item.Item;
import com.example.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/form/items")
@Slf4j
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");

        return regions;
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
    public String addItem(Item item, RedirectAttributes redirectAttributes) {
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());

        var itemPersisted = itemRepository.save(item);
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
