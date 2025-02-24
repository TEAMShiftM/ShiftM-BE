package com.shiftm.shiftm.global.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiftm.shiftm.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {

        response.setStatus(ErrorCode.UNAUTHORIZED.getStatus());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        final Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("code", ErrorCode.UNAUTHORIZED.getCode());
        jsonResponse.put("message", ErrorCode.UNAUTHORIZED.getMessage());

        final ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
        response.getWriter().flush();
    }
}
