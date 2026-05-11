package com.wisewallet.advisor.domain.repository;

import com.wisewallet.advisor.domain.model.Insight;

import java.util.List;
import java.util.UUID;

public interface InsightRepositoryPort {

    Insight save(Insight insight);

    List<Insight> findActiveByUserAndMonth(UUID userId, int year, int month);
}
