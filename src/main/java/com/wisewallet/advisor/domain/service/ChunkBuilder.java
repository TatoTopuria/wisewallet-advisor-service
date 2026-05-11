package com.wisewallet.advisor.domain.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Month;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

public class ChunkBuilder {

    public String buildMonthlySummary(int year,
                                      int month,
                                      int transactionCount,
                                      BigDecimal totalAmount,
                                      Map<String, BigDecimal> categorySummary) {

        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        StringBuilder builder = new StringBuilder();
        builder.append("Monthly Spending Summary - ")
                .append(Month.of(month).name())
                .append(" ")
                .append(year)
                .append("\n")
                .append("Total: ")
                .append(currency.format(totalAmount))
                .append(" across ")
                .append(transactionCount)
                .append(" transactions\n")
                .append("Breakdown by category:\n");

        categorySummary.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> builder.append("- ")
                        .append(entry.getKey())
                        .append(": ")
                        .append(currency.format(entry.getValue()))
                        .append("\n"));

        return builder.toString().trim();
    }
}
