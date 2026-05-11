package com.wisewallet.advisor.presentation.dto.response;

import com.wisewallet.advisor.domain.model.MessageRole;

import java.time.Instant;
import java.util.UUID;

public record ChatMessageResponse(
        UUID id,
        MessageRole role,
        String content,
        Integer turnNumber,
        Instant createdAt
) {
}
