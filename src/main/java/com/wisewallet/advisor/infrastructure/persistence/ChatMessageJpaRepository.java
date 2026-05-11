package com.wisewallet.advisor.infrastructure.persistence;

import com.wisewallet.advisor.domain.model.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, UUID> {

    List<ChatMessage> findBySession_IdOrderByTurnNumberAsc(UUID sessionId);

    @Query("""
            SELECT COALESCE(MAX(c.turnNumber), 0)
            FROM ChatMessage c
            WHERE c.session.id = :sessionId
            """)
    int findMaxTurnNumberBySessionId(@Param("sessionId") UUID sessionId);

    List<ChatMessage> findBySession_IdOrderByTurnNumberDesc(UUID sessionId, Pageable pageable);
}
