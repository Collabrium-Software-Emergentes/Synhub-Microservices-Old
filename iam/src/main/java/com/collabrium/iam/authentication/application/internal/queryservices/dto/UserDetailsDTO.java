package com.collabrium.iam.authentication.application.internal.queryservices.dto;

import com.collabrium.iam.authentication.domain.model.aggregates.User;
import com.collabrium.iam.shared.infrastructure.clients.groups.resources.LeaderResource;
import com.collabrium.iam.shared.infrastructure.clients.groups.resources.MemberResource;

public record UserDetailsDTO(
    User user,
    LeaderResource leaderResource,
    MemberResource memberResource
) {
}