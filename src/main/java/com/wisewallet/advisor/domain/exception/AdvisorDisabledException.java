package com.wisewallet.advisor.domain.exception;

public class AdvisorDisabledException extends RuntimeException {

    public AdvisorDisabledException() {
        super("AI Advisor feature is currently disabled");
    }
}
