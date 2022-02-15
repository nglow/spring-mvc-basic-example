package com.example.itemservice;

import com.example.itemservice.domain.item.Item;
import com.example.itemservice.domain.item.ItemRepository;
import com.example.itemservice.domain.member.Member;
import com.example.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        itemRepository.save(new Item("Item A", 10000, 10));
        itemRepository.save(new Item("Item B", 20000, 20));

        memberRepository.save(new Member("test", "test!", "test"));
    }
}
