package com.shiftm.shiftm.global.config;

import com.shiftm.shiftm.global.auth.annotation.AuthIdResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    private final AuthIdResolver authIdResolver;

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authIdResolver);
    }
}
