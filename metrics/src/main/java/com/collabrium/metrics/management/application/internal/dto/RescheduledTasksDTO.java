package com.collabrium.metrics.management.application.internal.dto;

import java.util.List;
import java.util.Map;

public record RescheduledTasksDTO(
    String type,
    long value,
    Map<String, Integer> details,
    List<Long> rescheduledMemberIds
) {
}