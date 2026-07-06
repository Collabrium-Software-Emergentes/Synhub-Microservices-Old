package com.collabrium.iam.authentication.domain.model.commands;

public record VerifyUserCommand(
    String token
) {
}
