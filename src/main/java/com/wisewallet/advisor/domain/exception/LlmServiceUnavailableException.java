package com.wisewallet.advisor.domain.exception;

public class LlmServiceUnavailableException extends RuntimeException {

    public LlmServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public LlmServiceUnavailableException(String message) {
        super(message);
    }
}
