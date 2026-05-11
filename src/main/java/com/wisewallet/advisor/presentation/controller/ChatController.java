package com.wisewallet.advisor.presentation.controller;

import com.wisewallet.advisor.application.command.ChatCommandService;
import com.wisewallet.advisor.presentation.dto.request.ChatRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/advisor")
public class ChatController {

    private final ChatCommandService chatCommandService;

    public ChatController(ChatCommandService chatCommandService) {
        this.chatCommandService = chatCommandService;
    }

    @PostMapping(path = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@RequestHeader("X-User-Id") UUID userId,
                           @Valid @RequestBody ChatRequest request) {
        return chatCommandService.chat(userId, request.sessionId(), request.message());
    }
}
