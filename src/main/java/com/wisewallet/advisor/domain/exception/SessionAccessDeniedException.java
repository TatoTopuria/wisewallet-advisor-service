package com.wisewallet.advisor.domain.exception;

import java.util.UUID;

public class SessionAccessDeniedException extends RuntimeException {

    public SessionAccessDeniedException(UUID sessionId) {
        super("Access denied to session: " + sessionId);
    }
}
