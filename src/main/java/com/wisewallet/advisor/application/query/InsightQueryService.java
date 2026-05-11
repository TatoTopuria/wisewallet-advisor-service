package com.wisewallet.advisor.application.query;

import com.wisewallet.advisor.application.shared.RateLimitService;
import com.wisewallet.advisor.infrastructure.config.AdvisorProperties;
import com.wisewallet.advisor.presentation.dto.response.InsightsEnvelope;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class InsightQueryService {

    private final RateLimitService rateLimitService;
    private final AdvisorProperties advisorProperties;

    public InsightQueryService(RateLimitService rateLimitService, AdvisorProperties advisorProperties) {
        this.rateLimitService = rateLimitService;
        this.advisorProperties = advisorProperties;
    }

    public InsightsEnvelope getInsights(UUID userId, int year, int month) {
        rateLimitService.checkLimit("insights:" + userId, advisorProperties.insights().rateLimitPerMinute());
        return new InsightsEnvelope(List.of(), Instant.now(), false);
    }
}
