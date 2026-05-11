package com.wisewallet.advisor.presentation.dto.response;

import com.wisewallet.advisor.domain.model.SessionStatus;

import java.time.Instant;
import java.util.UUID;

public record SessionResponse(
        UUID id,
        String title,
        SessionStatus status,
        Instant updatedAt
) {
}
