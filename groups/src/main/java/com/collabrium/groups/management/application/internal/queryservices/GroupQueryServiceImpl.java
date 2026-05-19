package com.collabrium.groups.management.application.internal.queryservices;

import com.collabrium.groups.management.application.internal.ports.IamQueryPort;
import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.domain.model.queries.GetGroupByCodeQuery;
import com.collabrium.groups.management.domain.model.queries.GetGroupByLeaderIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetGroupByMemberIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetGroupByUserIdQuery;
import com.collabrium.groups.management.domain.services.GroupQueryService;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupQueryServiceImpl implements GroupQueryService {

  private final GroupRepository groupRepository;
  private final IamQueryPort iamQueryPort;

  public GroupQueryServiceImpl(
      GroupRepository groupRepository,
      IamQueryPort iamQueryPort
  ) {

    this.groupRepository = groupRepository;
    this.iamQueryPort = iamQueryPort;
  }

  @Override
  public Optional<Group> handle(GetGroupByLeaderIdQuery query) {
    return Optional.empty();
  }

  @Override
  public Optional<Group> handle(GetGroupByCodeQuery query) {
    return Optional.empty();
  }

  @Override
  public Optional<Group> handle(GetGroupByMemberIdQuery query) {
    return Optional.empty();
  }

  @Override
  public Optional<Group> handle(GetGroupByUserIdQuery query) {

    var user = iamQueryPort.getUserOnlyById(query.userId());

    if (user == null || user.leaderId() == null) {
      return Optional.empty();
    }

    return groupRepository.findByLeaderId(user.leaderId());
  }
}