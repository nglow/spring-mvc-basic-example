package com.example.itemservice.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    public static long sequence = 0;

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);

        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values() );
    }

    public Optional<Member> findByLoginId(String loginId) {
        var all = findAll();

        //        for (Member member : all) {
//            if (Objects.equals(member.getLoginId(), loginId)) return Optional.of(member);
//        }
//
//        return Optional.empty();

        return all.stream()
                .filter(member -> Objects.equals(member.getLoginId(), loginId))
                .findFirst();
    }
}
