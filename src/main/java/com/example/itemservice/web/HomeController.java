package com.example.itemservice.web;

import com.example.itemservice.domain.member.Member;
import com.example.itemservice.domain.member.MemberRepository;
import com.example.itemservice.web.argumentresolver.Login;
import com.example.itemservice.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //    @GetMapping("/")
    public String home() {
        return "home";
    }

    //    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        if (memberId == null) return "home";

        var member = memberRepository.findById(memberId);

        if (member == null) return "home";
        model.addAttribute("member", member);
        return "loginHome";
    }

    //    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {
        var member = (Member) sessionManager.getSession(request);

        if (member == null) return "home";

        model.addAttribute("member", member);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV3(@SessionAttribute(name = Session.LOGIN_MEMBER, required = false) Member member, Model model) {
        if (member == null) return "home";

        model.addAttribute("member", member);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver (@Login Member member, Model model) {
        if (member == null) return "home";

        model.addAttribute("member", member);
        return "loginHome";
    }
}
