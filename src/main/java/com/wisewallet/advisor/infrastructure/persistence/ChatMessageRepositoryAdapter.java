package com.wisewallet.advisor.infrastructure.persistence;

import com.wisewallet.advisor.domain.model.ChatMessage;
import com.wisewallet.advisor.domain.repository.ChatMessageRepositoryPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ChatMessageRepositoryAdapter implements ChatMessageRepositoryPort {

    private final ChatMessageJpaRepository jpaRepository;

    public ChatMessageRepositoryAdapter(ChatMessageJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return jpaRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> findBySessionIdOrderByTurnNumberAsc(UUID sessionId) {
        return jpaRepository.findBySession_IdOrderByTurnNumberAsc(sessionId);
    }

    @Override
    public List<ChatMessage> findRecentBySessionId(UUID sessionId, int maxMessages) {
        List<ChatMessage> messages = jpaRepository
                .findBySession_IdOrderByTurnNumberDesc(sessionId, PageRequest.of(0, maxMessages));
        return messages.reversed();
    }

    @Override
    public int nextTurnNumber(UUID sessionId) {
        return jpaRepository.findMaxTurnNumberBySessionId(sessionId) + 1;
    }
}
