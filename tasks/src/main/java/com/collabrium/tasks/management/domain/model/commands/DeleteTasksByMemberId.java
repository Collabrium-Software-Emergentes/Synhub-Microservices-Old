package com.collabrium.tasks.management.domain.model.commands;

public record DeleteTasksByMemberId(
    Long memberId,
    Long groupId
) {
}