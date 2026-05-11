package com.wisewallet.advisor.infrastructure.persistence;

import com.wisewallet.advisor.domain.model.Insight;
import com.wisewallet.advisor.domain.model.InsightStatus;
import com.wisewallet.advisor.domain.repository.InsightRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class InsightRepositoryAdapter implements InsightRepositoryPort {

    private final InsightJpaRepository jpaRepository;

    public InsightRepositoryAdapter(InsightJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Insight save(Insight insight) {
        return jpaRepository.save(insight);
    }

    @Override
    public List<Insight> findActiveByUserAndMonth(UUID userId, int year, int month) {
        return jpaRepository.findByUserIdAndPeriodYearAndPeriodMonthAndStatusOrderByCreatedAtDesc(
                userId,
                year,
                month,
                InsightStatus.ACTIVE
        );
    }
}
