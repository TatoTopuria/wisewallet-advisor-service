package com.wisewallet.advisor.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 40)
public class FeatureFlagFilter extends OncePerRequestFilter {

    private final AdvisorProperties advisorProperties;
    private final ObjectMapper objectMapper;

    public FeatureFlagFilter(AdvisorProperties advisorProperties, ObjectMapper objectMapper) {
        this.advisorProperties = advisorProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/api/advisor") && !advisorProperties.enabled()) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), Map.of(
                    "timestamp", Instant.now().toString(),
                    "status", 503,
                    "error", "Service Unavailable",
                    "message", "AI Advisor feature is currently disabled",
                    "path", request.getRequestURI()
            ));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
