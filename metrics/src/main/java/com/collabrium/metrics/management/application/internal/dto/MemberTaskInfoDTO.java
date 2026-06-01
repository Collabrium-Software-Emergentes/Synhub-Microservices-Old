package com.collabrium.metrics.management.application.internal.dto;

public record MemberTaskInfoDTO(
    String memberName,
    int taskCount
) {
}