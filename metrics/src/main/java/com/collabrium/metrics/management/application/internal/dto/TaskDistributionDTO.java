package com.collabrium.metrics.management.application.internal.dto;

import java.util.Map;

public record TaskDistributionDTO(
    String type,
    int value,
    Map<String, MemberTaskInfoDTO> details
) {
}