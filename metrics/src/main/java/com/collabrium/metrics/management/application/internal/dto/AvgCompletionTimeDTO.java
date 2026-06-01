package com.collabrium.metrics.management.application.internal.dto;

import java.util.Map;

public record AvgCompletionTimeDTO(
    String type,
    double value,
    Map<String, Integer> details
) {
}