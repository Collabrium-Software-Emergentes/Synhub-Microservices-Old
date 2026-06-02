package com.collabrium.metrics.management.interfaces.rest.resources;

import java.util.Map;

public record TaskOverviewResource(
    String type,
    int value,
    Map<String, Integer> details
) {
}