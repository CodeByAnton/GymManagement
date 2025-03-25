package com.annton.api.interceptors;

import com.annton.api.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LogInterceptor implements HandlerInterceptor {

    private final UserService userService;

    private static final Logger logger = LogManager.getLogger(LogInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        var email = userService.getCurrentUserEmail();
        if(email != null) logger.info("User info: email: {}", email);
        logger.info("--- Finish request processing ---");
    }
}
