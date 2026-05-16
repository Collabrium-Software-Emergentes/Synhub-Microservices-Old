package com.collabrium.groups.management.application.internal.commandservices;

import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.domain.model.commands.*;
import com.collabrium.groups.management.domain.services.GroupCommandService;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.LeaderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupCommandServiceImpl implements GroupCommandService {

  private final GroupRepository groupRepository;
  private final LeaderRepository leaderRepository;

  public GroupCommandServiceImpl(
      GroupRepository groupRepository,
      LeaderRepository leaderRepository
  ) {

    this.groupRepository = groupRepository;
    this.leaderRepository = leaderRepository;
  }

  @Override
  public Optional<Group> handle(CreateGroupCommand command) {
    return Optional.empty();
  }

  @Override
  public Optional<Group> handle(UpdateGroupCommand command) {
    return Optional.empty();
  }

  @Override
  public void handle(DeleteGroupCommand command) {

  }

  @Override
  public void handle(RemoveMemberFromGroupCommand command) {

  }

  @Override
  public void handle(LeaveGroupCommand command) {

  }
}
