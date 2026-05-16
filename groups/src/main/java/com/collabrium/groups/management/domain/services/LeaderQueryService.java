package com.collabrium.groups.management.domain.services;

import com.collabrium.groups.management.domain.model.aggregates.Leader;
import com.collabrium.groups.management.domain.model.queries.GetLeaderByIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetLeaderByUsernameQuery;

import java.util.Optional;

public interface LeaderQueryService {

  Optional<Leader> handle(GetLeaderByIdQuery query);

  //Optional<LeaderWithUserInfo> handle(GetLeaderByUsernameQuery query, String authorizationHeader);
}
