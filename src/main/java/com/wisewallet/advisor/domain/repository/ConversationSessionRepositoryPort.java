package com.wisewallet.advisor.domain.repository;

import com.wisewallet.advisor.domain.model.ConversationSession;
import com.wisewallet.advisor.domain.model.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ConversationSessionRepositoryPort {

    ConversationSession save(ConversationSession session);

    Optional<ConversationSession> findById(UUID sessionId);

    Page<ConversationSession> findByUserIdAndStatus(UUID userId, SessionStatus status, Pageable pageable);

    long countByUserIdAndStatus(UUID userId, SessionStatus status);
}
