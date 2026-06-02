package com.collabrium.metrics.management.interfaces.rest.resources;

import java.util.Map;

public record TaskDistributionResource(
    String type,
    int value,
    Map<String, MemberTaskInfoResource> details
) {
}