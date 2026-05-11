package com.wisewallet.advisor.application.command;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record IngestionEventData(
        UUID userId,
        BigDecimal amount,
        String category,
        Instant categorizedAt
) {
}
