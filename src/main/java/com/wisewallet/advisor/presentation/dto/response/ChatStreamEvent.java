package com.wisewallet.advisor.presentation.dto.response;

import java.util.UUID;

public record ChatStreamEvent(String type, UUID sessionId, String text, UUID messageId) {
}
