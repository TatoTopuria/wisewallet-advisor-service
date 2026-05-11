package com.wisewallet.advisor.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI advisorOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WiseWallet Advisor Service API")
                        .version("v1")
                        .description("RAG-powered advisor service API endpoints."));
    }
}
