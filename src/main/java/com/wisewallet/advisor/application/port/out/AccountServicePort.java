package com.wisewallet.advisor.application.port.out;

import java.util.UUID;

public interface AccountServicePort {

    UserProfile getUserInfo(UUID userId);

    record UserProfile(UUID userId, String firstName, String lastName, String accountSummary) {
    }
}
