package com.example.itemservice.web.session;

import com.example.itemservice.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void testSession() {
        var response = new MockHttpServletResponse();

        var member = new Member("id", "name", "password");
        sessionManager.createSession(member, response);

        var request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        var result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        sessionManager.expire(request);
        var sessionExpired = sessionManager.getSession(request);
        assertThat(sessionExpired).isEqualTo(null);
    }
}