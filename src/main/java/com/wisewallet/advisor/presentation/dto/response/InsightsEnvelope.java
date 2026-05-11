package com.wisewallet.advisor.presentation.dto.response;

import java.time.Instant;
import java.util.List;

public record InsightsEnvelope(List<InsightResponse> insights, Instant generatedAt, boolean cached) {
}
