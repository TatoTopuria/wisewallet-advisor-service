package com.wisewallet.advisor.infrastructure.persistence;

import com.wisewallet.advisor.domain.model.IngestionCheckpoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IngestionCheckpointJpaRepository extends JpaRepository<IngestionCheckpoint, UUID> {

    Optional<IngestionCheckpoint> findByUserIdAndYearAndMonth(UUID userId, int year, int month);

    List<IngestionCheckpoint> findByEmbeddedFalseAndLastProcessedAtBeforeOrderByLastProcessedAtAsc(
            Instant cutoff);
}
