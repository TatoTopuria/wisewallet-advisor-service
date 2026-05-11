package com.wisewallet.advisor.application.port.out;

import com.wisewallet.advisor.presentation.dto.response.InsightsEnvelope;

import java.util.Optional;
import java.util.UUID;

public interface InsightCachePort {

    Optional<InsightsEnvelope> get(UUID userId, int year, int month);

    void put(UUID userId, int year, int month, InsightsEnvelope value);
}
