package com.collabrium.metrics.management.interfaces.rest.resources;

import java.util.Map;

public record AvgCompletionTimeResource(
    String type,
    double value,
    Map<String, Integer> details
) {
}