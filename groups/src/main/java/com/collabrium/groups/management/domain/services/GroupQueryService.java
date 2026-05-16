package com.collabrium.groups.management.domain.services;

import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.domain.model.queries.GetGroupByCodeQuery;
import com.collabrium.groups.management.domain.model.queries.GetGroupByIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetGroupByLeaderIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetGroupByMemberIdQuery;

import java.util.Optional;

public interface GroupQueryService {

  Optional<Group> handle(GetGroupByLeaderIdQuery query);

  Optional<Group> handle(GetGroupByCodeQuery query);

  Optional<Group> handle(GetGroupByMemberIdQuery query);

  Optional<Group> handle(GetGroupByIdQuery query);
}
