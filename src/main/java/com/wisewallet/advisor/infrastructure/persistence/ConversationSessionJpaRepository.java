package com.wisewallet.advisor.infrastructure.persistence;

import com.wisewallet.advisor.domain.model.ConversationSession;
import com.wisewallet.advisor.domain.model.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConversationSessionJpaRepository extends JpaRepository<ConversationSession, UUID> {

    Page<ConversationSession> findByUserIdAndStatus(UUID userId, SessionStatus status, Pageable pageable);

    long countByUserIdAndStatus(UUID userId, SessionStatus status);
}
