package com.wisewallet.advisor.infrastructure.persistence;

import com.wisewallet.advisor.domain.model.ConversationSession;
import com.wisewallet.advisor.domain.model.SessionStatus;
import com.wisewallet.advisor.domain.repository.ConversationSessionRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ConversationSessionRepositoryAdapter implements ConversationSessionRepositoryPort {

    private final ConversationSessionJpaRepository jpaRepository;

    public ConversationSessionRepositoryAdapter(ConversationSessionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ConversationSession save(ConversationSession session) {
        return jpaRepository.save(session);
    }

    @Override
    public Optional<ConversationSession> findById(UUID sessionId) {
        return jpaRepository.findById(sessionId);
    }

    @Override
    public Page<ConversationSession> findByUserIdAndStatus(UUID userId, SessionStatus status, Pageable pageable) {
        return jpaRepository.findByUserIdAndStatus(userId, status, pageable);
    }

    @Override
    public long countByUserIdAndStatus(UUID userId, SessionStatus status) {
        return jpaRepository.countByUserIdAndStatus(userId, status);
    }
}
