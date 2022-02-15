package com.example.itemservice.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Component
public class SessionManager {

    public static final String MY_SESSION_ID = "mySessionId";
    private final Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     */
    public void createSession(Object value, HttpServletResponse response) {
        var sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        var cookie = new Cookie(MY_SESSION_ID, sessionId);
        response.addCookie(cookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {
        var sessionCookie = findCookie(request);

        if (sessionCookie == null) return null;
        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request) {
        var cookie = findCookie(request);
        if (cookie != null) {
            sessionStore.remove(cookie.getValue());
        }
    }

    private Cookie findCookie(HttpServletRequest request) {
        var cookies = request.getCookies();

        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals(MY_SESSION_ID))
                .findFirst()
                .orElse(null);
    }
}
