package com.example.itemservice.web.basic;

import com.example.itemservice.domain.item.Item;
import com.example.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        var items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        var item = itemRepository.findById(itemId);
        model.addAttribute(item);
        return "basic/item";
    }

    @PostConstruct
    public void initialize() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String saveV1(@RequestParam String itemName,
                       @RequestParam Integer price,
                       @RequestParam Integer quantity,
                       Model model) {
        var item = new Item(itemName, price, quantity);
        itemRepository.save(item);

        model.addAttribute(item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String saveV2(@ModelAttribute Item item) {
        itemRepository.save(item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String saveV3(Item item) {
        itemRepository.save(item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String saveV4(Item item) {
        itemRepository.save(item);

        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String saveV5(Item item, RedirectAttributes redirectAttributes) {
        var itemPersisted = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", itemPersisted.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        var item = itemRepository.findById(itemId);
        model.addAttribute(item);

        return "/basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }
}
