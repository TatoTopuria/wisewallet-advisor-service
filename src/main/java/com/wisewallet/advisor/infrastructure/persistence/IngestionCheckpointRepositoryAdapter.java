package com.wisewallet.advisor.infrastructure.persistence;

import com.wisewallet.advisor.domain.model.IngestionCheckpoint;
import com.wisewallet.advisor.domain.repository.IngestionCheckpointRepositoryPort;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class IngestionCheckpointRepositoryAdapter implements IngestionCheckpointRepositoryPort {

    private final IngestionCheckpointJpaRepository jpaRepository;

    public IngestionCheckpointRepositoryAdapter(IngestionCheckpointJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<IngestionCheckpoint> findByUserIdAndYearAndMonth(UUID userId, int year, int month) {
        return jpaRepository.findByUserIdAndYearAndMonth(userId, year, month);
    }

    @Override
    public IngestionCheckpoint save(IngestionCheckpoint checkpoint) {
        return jpaRepository.save(checkpoint);
    }

    @Override
    public List<IngestionCheckpoint> findPending(int debounceSeconds, int maxBatchSize) {
        Instant cutoff = Instant.now().minusSeconds(debounceSeconds);
        return jpaRepository.findByEmbeddedFalseAndLastProcessedAtBeforeOrderByLastProcessedAtAsc(cutoff)
                .stream()
                .limit(maxBatchSize)
                .toList();
    }
}
