package com.example.itemservice.domain.login;

import com.example.itemservice.domain.member.Member;
import com.example.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null 로그인 실패
     * */
    public Member login(String loginId, String password) {
//        var byLoginId = memberRepository.findByLoginId(loginId);
//
//        if (byLoginId.isEmpty()) return null;
//        var member  = byLoginId.get();
//
//        if (member.getPassword().equals(password)) return member;
//        else return null;

        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
