package com.collabrium.groups.management.domain.model.commands;

public record UpdateGroupCommand(
    Long leaderId,
    String name,
    String description,
    String imgUrl
) {
}