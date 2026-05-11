package com.wisewallet.advisor.presentation.controller;

import com.wisewallet.advisor.application.query.SessionQueryService;
import com.wisewallet.advisor.presentation.dto.response.ChatMessageResponse;
import com.wisewallet.advisor.presentation.dto.response.SessionResponse;
import com.wisewallet.advisor.presentation.mapper.SessionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/advisor/sessions")
public class SessionController {

    private final SessionQueryService sessionQueryService;
    private final SessionMapper sessionMapper;

    public SessionController(SessionQueryService sessionQueryService, SessionMapper sessionMapper) {
        this.sessionQueryService = sessionQueryService;
        this.sessionMapper = sessionMapper;
    }

    @GetMapping
    public Page<SessionResponse> listSessions(@RequestHeader("X-User-Id") UUID userId, Pageable pageable) {
        return sessionQueryService.findActiveSessions(userId, pageable).map(sessionMapper::toResponse);
    }

    @GetMapping("/{sessionId}/messages")
    public List<ChatMessageResponse> getMessages(@RequestHeader("X-User-Id") UUID userId,
                                                 @PathVariable UUID sessionId) {
        return sessionQueryService.getMessages(sessionId).stream().map(sessionMapper::toResponse).toList();
    }
}
