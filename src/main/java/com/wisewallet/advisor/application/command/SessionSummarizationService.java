package com.wisewallet.advisor.application.command;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionSummarizationService {

    @Async
    public void summarizeIfNeeded(UUID sessionId) {
        // Implemented in next iteration with chat history retrieval + LLM summary call.
    }
}
