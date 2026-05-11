package com.wisewallet.advisor.application.command;

import com.wisewallet.advisor.application.shared.RateLimitService;
import com.wisewallet.advisor.infrastructure.config.AdvisorProperties;
import com.wisewallet.advisor.presentation.dto.response.ChatStreamEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ChatCommandService {

    private final RateLimitService rateLimitService;
    private final AdvisorProperties advisorProperties;
    private final ExecutorService sseExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public ChatCommandService(RateLimitService rateLimitService, AdvisorProperties advisorProperties) {
        this.rateLimitService = rateLimitService;
        this.advisorProperties = advisorProperties;
    }

    public SseEmitter chat(UUID userId, UUID requestedSessionId, String message) {
        rateLimitService.checkLimit("chat:" + userId, advisorProperties.chat().rateLimitPerMinute());

        UUID sessionId = requestedSessionId != null ? requestedSessionId : UUID.randomUUID();
        UUID messageId = UUID.randomUUID();
        SseEmitter emitter = new SseEmitter(30_000L);

        sseExecutor.submit(() -> {
            try {
                emitter.send(SseEmitter.event().data(new ChatStreamEvent("session", sessionId, null, null)));
                emitter.send(SseEmitter.event().data(new ChatStreamEvent("content", sessionId,
                        "This is an initial scaffold response. RAG pipeline wiring is next.", null)));
                emitter.send(SseEmitter.event().data(new ChatStreamEvent("done", sessionId, null, messageId)));
                emitter.complete();
            } catch (IOException ex) {
                emitter.completeWithError(ex);
            }
        });

        return emitter;
    }
}
