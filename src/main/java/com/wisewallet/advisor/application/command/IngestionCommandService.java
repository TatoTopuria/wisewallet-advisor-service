package com.wisewallet.advisor.application.command;

import com.wisewallet.advisor.domain.model.IngestionCheckpoint;
import com.wisewallet.advisor.domain.repository.IngestionCheckpointRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
public class IngestionCommandService {

    private final IngestionCheckpointRepositoryPort checkpointRepository;

    public IngestionCommandService(IngestionCheckpointRepositoryPort checkpointRepository) {
        this.checkpointRepository = checkpointRepository;
    }

    @Transactional
    public void handleTransactionCategorized(IngestionEventData eventData) {
        int year = eventData.categorizedAt().atZone(ZoneOffset.UTC).getYear();
        int month = eventData.categorizedAt().atZone(ZoneOffset.UTC).getMonthValue();

        IngestionCheckpoint checkpoint = checkpointRepository
                .findByUserIdAndYearAndMonth(eventData.userId(), year, month)
                .orElseGet(() -> IngestionCheckpoint.builder()
                        .userId(eventData.userId())
                        .year(year)
                        .month(month)
                        .transactionCount(0)
                        .totalAmount(BigDecimal.ZERO)
                        .categorySummary(new HashMap<>())
                        .embedded(false)
                        .lastProcessedAt(Instant.now())
                        .build());

        checkpoint.setTransactionCount(checkpoint.getTransactionCount() + 1);
        checkpoint.setTotalAmount(checkpoint.getTotalAmount().add(eventData.amount()));
        checkpoint.setCategorySummary(mergeCategorySummary(
                checkpoint.getCategorySummary(),
                eventData.category(),
                eventData.amount()
        ));
        checkpoint.setLastProcessedAt(Instant.now());
        checkpoint.setEmbedded(false);

        checkpointRepository.save(checkpoint);
    }

    public void processPendingCheckpoints() {
        // Implemented in next iteration with checkpoint + embedding ports.
    }

    private Map<String, BigDecimal> mergeCategorySummary(Map<String, BigDecimal> current,
                                                         String category,
                                                         BigDecimal amount) {
        Map<String, BigDecimal> merged = new HashMap<>(current != null ? current : Map.of());
        merged.merge(category, amount, BigDecimal::add);
        return merged;
    }
}
