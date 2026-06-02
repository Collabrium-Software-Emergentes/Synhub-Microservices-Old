package com.collabrium.metrics.management.interfaces.rest.resources;

public record MemberTaskInfoResource(
    String memberName,
    int taskCount
) {
}