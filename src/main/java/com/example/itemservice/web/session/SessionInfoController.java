package com.example.itemservice.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@Slf4j
public class SessionInfoController {

    @GetMapping("/session-info")
    public void sessionInfo(HttpServletRequest request) {
        var session = request.getSession(false);

        if (session == null) {
            log.info("There is no session");
            return;
        }

        session.getAttributeNames().asIterator()
                .forEachRemaining(n -> log.info("session name={}, value={}", n, session.getAttribute(n)));

        log.info("sessionId={}", session.getId());
        log.info("getMaxInteractiveInterval={}", session.getMaxInactiveInterval());
        log.info("creationTime={}", new Date(session.getCreationTime()));
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew());
    }
}
