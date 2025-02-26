package com.shiftm.shiftm.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ShiftM API")
                        .version("v0.0.1")
                        .description("ShiftM API 명세서입니다."))
                .components(new Components()
                        .addSecuritySchemes("Authorization", customSecurityScheme()))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Authorization"));
    }

    private SecurityScheme customSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);
    }
}
