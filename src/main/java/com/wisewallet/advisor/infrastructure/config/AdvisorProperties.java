package com.wisewallet.advisor.infrastructure.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "wisewallet.advisor")
public record AdvisorProperties(
        boolean enabled,
        Retrieval retrieval,
        Ingestion ingestion,
        Chat chat,
        Insights insights,
        Session session,
        @NotBlank String accountServiceUrl
) {

    public record Retrieval(
            @Min(1) int topK,
            double minSimilarity
    ) {
    }

    public record Ingestion(
            @Min(1) int embeddingBatchSize,
            @Min(1000) long pollIntervalMs,
            @Min(1) int debounceSeconds
    ) {
    }

    public record Chat(
            @Min(1) int maxMessageLength,
            @Min(1) int rateLimitPerMinute,
            @Min(1) int sessionMaxActive,
            @Min(1) int summarizationThreshold
    ) {
    }

    public record Insights(
            @Min(1) int rateLimitPerMinute,
            @Min(1) int cacheTtlHours,
            @NotBlank String generationCron
    ) {
    }

    public record Session(
            @Min(1) int cleanupInactiveDays,
            @NotBlank String cleanupCron
    ) {
    }
}
