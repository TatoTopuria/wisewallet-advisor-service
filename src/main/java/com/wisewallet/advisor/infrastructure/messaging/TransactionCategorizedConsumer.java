package com.wisewallet.advisor.infrastructure.messaging;

import com.wisewallet.advisor.application.command.IngestionCommandService;
import com.wisewallet.advisor.application.command.IngestionEventData;
import com.wisewallet.advisor.application.config.AdvisorProperties;
import com.wisewallet.advisor.infrastructure.messaging.event.TransactionCategorizedEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionCategorizedConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransactionCategorizedConsumer.class);

    private final IngestionCommandService ingestionCommandService;
    private final AdvisorProperties advisorProperties;
    private final KafkaCorrelationIdConsumerHelper correlationIdHelper;

    public TransactionCategorizedConsumer(IngestionCommandService ingestionCommandService,
                                          AdvisorProperties advisorProperties,
                                          KafkaCorrelationIdConsumerHelper correlationIdHelper) {
        this.ingestionCommandService = ingestionCommandService;
        this.advisorProperties = advisorProperties;
        this.correlationIdHelper = correlationIdHelper;
    }

    @KafkaListener(
            topics = "${wisewallet.advisor.kafka.topics.txn-categorized:txn.categorized}",
            groupId = "${spring.kafka.consumer.group-id:advisor-ingestion}",
            concurrency = "${wisewallet.advisor.kafka.concurrency:3}"
    )
    public void consume(ConsumerRecord<String, TransactionCategorizedEvent> record) {
        TransactionCategorizedEvent event = record.value();
        if (event == null) {
            return;
        }
        correlationIdHelper.putCorrelationId(record.headers());
        try {
            if (!advisorProperties.enabled()) {
                log.warn("Advisor disabled; skipping event processing. topic={}, partition={}, offset={}, eventId={}",
                        record.topic(), record.partition(), record.offset(), event.eventId());
                return;
            }

            if (event.userId() == null || event.amount() == null || event.category() == null || event.categorizedAt() == null) {
                log.warn("Malformed txn.categorized event skipped. topic={}, partition={}, offset={}, eventId={}",
                        record.topic(), record.partition(), record.offset(), event.eventId());
                return;
            }

            ingestionCommandService.handleTransactionCategorized(new IngestionEventData(
                    event.userId(),
                    event.amount(),
                    event.category(),
                    event.categorizedAt()
            ));
        } finally {
            correlationIdHelper.clearCorrelationId();
        }
    }
}
