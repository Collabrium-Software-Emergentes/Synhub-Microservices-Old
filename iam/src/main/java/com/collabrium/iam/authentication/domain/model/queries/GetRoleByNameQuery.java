package com.collabrium.iam.authentication.domain.model.queries;

import com.collabrium.iam.authentication.domain.model.valueobjects.Roles;

public record GetRoleByNameQuery(
    Roles name
) {
}
