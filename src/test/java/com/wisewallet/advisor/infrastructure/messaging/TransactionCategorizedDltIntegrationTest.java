package com.wisewallet.advisor.infrastructure.messaging;

import com.wisewallet.advisor.application.command.IngestionCommandService;
import com.wisewallet.advisor.application.config.AdvisorProperties;
import com.wisewallet.advisor.infrastructure.config.KafkaConsumerConfig;
import com.wisewallet.advisor.infrastructure.messaging.event.TransactionCategorizedEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(
        classes = TransactionCategorizedDltIntegrationTest.KafkaTestApp.class,
        properties = {
                "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
                "spring.kafka.consumer.group-id=advisor-ingestion-test",
                "wisewallet.advisor.enabled=true",
                "wisewallet.advisor.kafka.topics.txn-categorized=txn.categorized",
                "wisewallet.advisor.kafka.topics.txn-categorized-dlt=txn.categorized.DLT",
                "wisewallet.advisor.kafka.concurrency=1",
                "wisewallet.advisor.account-service-url=http://localhost:8081",
                "wisewallet.advisor.retrieval.top-k=5",
                "wisewallet.advisor.retrieval.min-similarity=0.3",
                "wisewallet.advisor.ingestion.embedding-batch-size=20",
                "wisewallet.advisor.ingestion.poll-interval-ms=300000",
                "wisewallet.advisor.ingestion.debounce-seconds=30",
                "wisewallet.advisor.chat.max-message-length=2000",
                "wisewallet.advisor.chat.rate-limit-per-minute=10",
                "wisewallet.advisor.chat.session-max-active=20",
                "wisewallet.advisor.chat.summarization-threshold=5",
                "wisewallet.advisor.insights.rate-limit-per-minute=5",
                "wisewallet.advisor.insights.cache-ttl-hours=4",
                "wisewallet.advisor.insights.generation-cron=0 0 6 * * *",
                "wisewallet.advisor.session.cleanup-inactive-days=30",
                "wisewallet.advisor.session.cleanup-cron=0 0 2 * * *"
        }
)
@EmbeddedKafka(partitions = 1, topics = {"txn.categorized", "txn.categorized.DLT"})
class TransactionCategorizedDltIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @MockBean
    private IngestionCommandService ingestionCommandService;

    private KafkaTemplate<String, TransactionCategorizedEvent> producer;
    private Consumer<String, TransactionCategorizedEvent> dltConsumer;

    @BeforeEach
    void setUp() {
        doThrow(new IllegalStateException("forced ingestion failure"))
                .when(ingestionCommandService)
                .handleTransactionCategorized(any());

        Map<String, Object> producerProps = new HashMap<>(org.springframework.kafka.test.utils.KafkaTestUtils.producerProps(embeddedKafkaBroker));
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        ProducerFactory<String, TransactionCategorizedEvent> producerFactory =
                new DefaultKafkaProducerFactory<>(producerProps, new StringSerializer(), new JsonSerializer<>());
        producer = new KafkaTemplate<>(producerFactory);

        Map<String, Object> consumerProps = new HashMap<>(org.springframework.kafka.test.utils.KafkaTestUtils.consumerProps(
                "advisor-dlt-test-group",
                "true",
                embeddedKafkaBroker));
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        JsonDeserializer<TransactionCategorizedEvent> jsonDeserializer =
                new JsonDeserializer<>(TransactionCategorizedEvent.class, false);
        jsonDeserializer.addTrustedPackages("com.wisewallet.advisor.infrastructure.messaging.event");
        dltConsumer = new org.springframework.kafka.core.DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                jsonDeserializer
        ).createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(dltConsumer, "txn.categorized.DLT");
    }

    @AfterEach
    void tearDown() {
        if (producer != null) {
            producer.destroy();
        }
        if (dltConsumer != null) {
            dltConsumer.close();
        }
    }

    @Test
    void shouldPublishToDltAfterRetriesWhenListenerFails() throws Exception {
        TransactionCategorizedEvent event = new TransactionCategorizedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new BigDecimal("123.45"),
                "USD",
                "DINING",
                Instant.now()
        );

        producer.send("txn.categorized", event.accountId().toString(), event).get();

        ConsumerRecord<String, TransactionCategorizedEvent> dltRecord = waitForDltRecord(Duration.ofSeconds(20));

        assertThat(dltRecord).isNotNull();
        assertThat(dltRecord.topic()).isEqualTo("txn.categorized.DLT");
        assertThat(dltRecord.key()).isEqualTo(event.accountId().toString());
        assertThat(dltRecord.value()).isNotNull();
        assertThat(dltRecord.value().eventId()).isEqualTo(event.eventId());
        assertThat(dltRecord.value().transactionId()).isEqualTo(event.transactionId());
        assertThat(dltRecord.value().userId()).isEqualTo(event.userId());
    }

    private ConsumerRecord<String, TransactionCategorizedEvent> waitForDltRecord(Duration timeout) {
        long deadline = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < deadline) {
            var records = dltConsumer.poll(Duration.ofMillis(500));
            if (!records.isEmpty()) {
                return records.iterator().next();
            }
        }
        return null;
    }

    @TestConfiguration
    @EnableAutoConfiguration(exclude = {
            org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
            org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
            org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration.class
    })
    @EnableKafka
    @EnableConfigurationProperties(AdvisorProperties.class)
    @Import({KafkaConsumerConfig.class, TransactionCategorizedConsumer.class, KafkaCorrelationIdConsumerHelper.class})
    static class KafkaTestApp {
    }
}
