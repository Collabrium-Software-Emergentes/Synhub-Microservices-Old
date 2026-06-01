package com.collabrium.metrics.management.application.internal.dto;

import java.util.Map;

public record TaskOverviewDTO(
    String type,
    int value,
    Map<String, Integer> details
) {
}