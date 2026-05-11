package com.wisewallet.advisor.application.query;

import com.wisewallet.advisor.domain.exception.SessionAccessDeniedException;
import com.wisewallet.advisor.domain.exception.SessionNotFoundException;
import com.wisewallet.advisor.domain.model.ChatMessage;
import com.wisewallet.advisor.domain.model.ConversationSession;
import com.wisewallet.advisor.domain.model.SessionStatus;
import com.wisewallet.advisor.domain.repository.ChatMessageRepositoryPort;
import com.wisewallet.advisor.domain.repository.ConversationSessionRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SessionQueryService {

    private final ConversationSessionRepositoryPort sessionRepository;
    private final ChatMessageRepositoryPort messageRepository;

    public SessionQueryService(ConversationSessionRepositoryPort sessionRepository,
                               ChatMessageRepositoryPort messageRepository) {
        this.sessionRepository = sessionRepository;
        this.messageRepository = messageRepository;
    }

    public Page<ConversationSession> findActiveSessions(UUID userId, Pageable pageable) {
        return sessionRepository.findByUserIdAndStatus(userId, SessionStatus.ACTIVE, pageable);
    }

    public List<ChatMessage> getMessages(UUID userId, UUID sessionId) {
        ConversationSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        if (!session.getUserId().equals(userId)) {
            throw new SessionAccessDeniedException(sessionId);
        }

        return messageRepository.findBySessionIdOrderByTurnNumberAsc(sessionId);
    }
}
