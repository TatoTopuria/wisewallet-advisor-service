package com.wisewallet.advisor.domain.repository;

import com.wisewallet.advisor.domain.model.ChatMessage;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepositoryPort {

    ChatMessage save(ChatMessage chatMessage);

    List<ChatMessage> findBySessionIdOrderByTurnNumberAsc(UUID sessionId);

    List<ChatMessage> findRecentBySessionId(UUID sessionId, int maxMessages);

    int nextTurnNumber(UUID sessionId);
}
