package com.collabrium.groups.management.domain.model.commands;

public record RemoveMemberFromGroupCommand(
    Long leaderId,
    Long memberId
) {
}