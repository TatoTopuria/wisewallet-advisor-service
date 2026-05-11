package com.wisewallet.advisor.application.port.out;

import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatCompletionPort {

    Flux<String> streamChat(List<String> promptMessages);

    String complete(List<String> promptMessages);
}
