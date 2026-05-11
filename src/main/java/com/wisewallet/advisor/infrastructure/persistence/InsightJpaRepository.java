package com.wisewallet.advisor.infrastructure.persistence;

import com.wisewallet.advisor.domain.model.Insight;
import com.wisewallet.advisor.domain.model.InsightStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InsightJpaRepository extends JpaRepository<Insight, UUID> {

    List<Insight> findByUserIdAndPeriodYearAndPeriodMonthAndStatusOrderByCreatedAtDesc(
            UUID userId,
            int periodYear,
            int periodMonth,
            InsightStatus status
    );
}
