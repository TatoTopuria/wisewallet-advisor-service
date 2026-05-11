package com.wisewallet.advisor.presentation.dto.response;

import com.wisewallet.advisor.domain.model.InsightType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record InsightResponse(
        UUID id,
        InsightType type,
        String title,
        String content,
        Integer periodYear,
        Integer periodMonth,
        BigDecimal relevanceScore,
        Instant createdAt
) {
}
