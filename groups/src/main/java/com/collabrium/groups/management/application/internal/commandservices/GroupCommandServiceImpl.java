package com.collabrium.groups.management.application.internal.commandservices;

import com.collabrium.groups.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.groups.management.domain.exceptions.GroupAlreadyExistsException;
import com.collabrium.groups.management.domain.exceptions.GroupNotFoundException;
import com.collabrium.groups.management.domain.exceptions.LeaderNotFoundException;
import com.collabrium.groups.management.domain.model.aggregates.Group;
import com.collabrium.groups.management.domain.model.commands.*;
import com.collabrium.groups.management.domain.model.valueobjects.GroupCode;
import com.collabrium.groups.management.domain.services.GroupCommandService;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.LeaderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupCommandServiceImpl implements GroupCommandService {

  private final GroupRepository groupRepository;
  private final IamQueryPort iamQueryPort;
  private final LeaderRepository leaderRepository;

  public GroupCommandServiceImpl(
      GroupRepository groupRepository,
      IamQueryPort iamQueryPort,
      LeaderRepository leaderRepository
  ) {

    this.groupRepository = groupRepository;
    this.iamQueryPort = iamQueryPort;
    this.leaderRepository = leaderRepository;
  }

  @Override
  public Optional<Group> handle(CreateGroupCommand command) {

    var user = iamQueryPort.getUserOnlyById(command.userId());

    if (user == null || user.leaderId() == null) {
      return Optional.empty();
    }

    Long leaderId = user.leaderId();

    var leader = leaderRepository
        .findById(leaderId)
        .orElseThrow(() ->
            new LeaderNotFoundException(leaderId)
        );

    if (groupRepository.findByLeaderId(leaderId).isPresent()) {

      throw new GroupAlreadyExistsException(
          "Leader already owns a group"
      );
    }

    GroupCode groupCode;

    do {

      groupCode = GroupCode.generate();

    } while (groupRepository.existsByCode(groupCode));

    var group = new Group(
        command.name(),
        command.description(),
        command.imgUrl(),
        leader,
        groupCode
    );

    var savedGroup =
        groupRepository.save(group);

    return Optional.of(savedGroup);
  }

  @Override
  public Optional<Group> handle(UpdateGroupCommand command) {

    var user = iamQueryPort.getUserOnlyById(command.userId());

    if (user == null || user.leaderId() == null) {
      throw new LeaderNotFoundException(command.userId());
    }

    Long leaderId = user.leaderId();

    var group = groupRepository
        .findByLeaderId(leaderId)
        .orElseThrow(() ->
            GroupNotFoundException.forLeader(leaderId)
        );

    group.updateInformation(command);

    var updatedGroup =
        groupRepository.save(group);

    return Optional.of(updatedGroup);
  }

  @Override
  public void handle(DeleteGroupCommand command) {

  }

  @Override
  public void handle(RemoveMemberFromGroupCommand command) {

  }

  @Override
  public void handle(LeaveGroupCommand command) {

    var group =
        groupRepository
            .findById(command.groupId())
            .orElseThrow(() ->
                GroupNotFoundException.forId(
                    command.groupId()
                )
            );

    group.decreaseMemberCount();

    groupRepository.save(group);
  }
}
