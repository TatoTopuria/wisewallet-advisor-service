package com.wisewallet.advisor.infrastructure.messaging;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class KafkaCorrelationIdConsumerHelper {

    public static final String CORRELATION_HEADER = "X-Correlation-ID";
    private static final String MDC_KEY = "correlationId";

    public void putCorrelationId(Headers headers) {
        Header header = headers.lastHeader(CORRELATION_HEADER);
        if (header == null || header.value() == null) {
            return;
        }
        MDC.put(MDC_KEY, new String(header.value(), StandardCharsets.UTF_8));
    }

    public void clearCorrelationId() {
        MDC.remove(MDC_KEY);
    }
}
