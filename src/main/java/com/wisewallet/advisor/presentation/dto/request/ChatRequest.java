package com.wisewallet.advisor.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ChatRequest(
        UUID sessionId,
        @NotBlank @Size(min = 1, max = 2000) String message
) {
}
