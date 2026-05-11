package com.wisewallet.advisor.presentation.controller;

import com.wisewallet.advisor.application.query.InsightQueryService;
import com.wisewallet.advisor.presentation.dto.response.InsightsEnvelope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.UUID;

@RestController
@RequestMapping("/api/advisor")
public class InsightController {

    private final InsightQueryService insightQueryService;

    public InsightController(InsightQueryService insightQueryService) {
        this.insightQueryService = insightQueryService;
    }

    @GetMapping("/insights")
    public InsightsEnvelope getInsights(@RequestHeader("X-User-Id") UUID userId,
                                        @RequestParam(name = "month", required = false) String month) {
        YearMonth yearMonth = month == null ? YearMonth.now() : YearMonth.parse(month);
        return insightQueryService.getInsights(userId, yearMonth.getYear(), yearMonth.getMonthValue());
    }
}
