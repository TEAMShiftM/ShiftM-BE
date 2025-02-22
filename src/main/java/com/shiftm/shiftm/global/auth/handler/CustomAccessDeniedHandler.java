package com.shiftm.shiftm.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiftm.shiftm.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {

        response.setStatus(ErrorCode.FORBIDDEN.getStatus());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        final Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("code", ErrorCode.FORBIDDEN.getCode());
        jsonResponse.put("message", ErrorCode.FORBIDDEN.getMessage());

        final ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
        response.getWriter().flush();
    }
}
