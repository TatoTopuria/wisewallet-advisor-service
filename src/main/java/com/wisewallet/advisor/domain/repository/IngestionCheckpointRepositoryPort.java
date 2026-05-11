package com.wisewallet.advisor.domain.repository;

import com.wisewallet.advisor.domain.model.IngestionCheckpoint;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IngestionCheckpointRepositoryPort {

    Optional<IngestionCheckpoint> findByUserIdAndYearAndMonth(UUID userId, int year, int month);

    IngestionCheckpoint save(IngestionCheckpoint checkpoint);

    List<IngestionCheckpoint> findPending(int debounceSeconds, int maxBatchSize);
}
