package com.collabrium.groups.management.application.internal.queryservices;

import com.collabrium.groups.management.application.internal.dto.MemberDetailsDTO;
import com.collabrium.groups.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.groups.management.application.internal.dto.LeaderDetailsDTO;
import com.collabrium.groups.management.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.groups.management.domain.exceptions.InvalidGroupException;
import com.collabrium.groups.management.domain.exceptions.InvalidLeaderException;
import com.collabrium.groups.management.domain.exceptions.UserNotFoundException;
import com.collabrium.groups.management.domain.model.queries.GetLeaderDetailsByUserIdQuery;
import com.collabrium.groups.management.domain.model.queries.GetMembersOfMyGroupQuery;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.collabrium.groups.management.infrastructure.persistence.jpa.repositories.LeaderRepository;
import com.collabrium.groups.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaderDetailsQueryService {

  private final LeaderRepository leaderRepository;
  private final GroupRepository groupRepository;
  private final IamQueryPort iamQueryPort;
  private final TasksQueryPort tasksQueryPort;

  public LeaderDetailsQueryService(
      LeaderRepository leaderRepository,
      GroupRepository groupRepository,
      IamQueryPort iamQueryPort,
      TasksQueryPort tasksQueryPort
  ) {

    this.leaderRepository = leaderRepository;
    this.groupRepository = groupRepository;
    this.iamQueryPort = iamQueryPort;
    this.tasksQueryPort = tasksQueryPort;
  }

  public Optional<LeaderDetailsDTO> handle(
      GetLeaderDetailsByUserIdQuery query
  ) {

    var user = iamQueryPort.getUserOnlyById(query.userId());

    if (user == null || user.leaderId() == null) {
      return Optional.empty();
    }

    var optionalLeader = leaderRepository.findById(user.leaderId());

    return optionalLeader.map(leader ->
        new LeaderDetailsDTO(
            leader.getId(),
            user.username(),
            user.name(),
            user.surname(),
            user.imgUrl(),
            user.email(),
            leader.getFormattedAverageSolutionTime(),
            leader.getSolvedRequests()
        )
    );
  }

  public List<MemberDetailsDTO> handle(
      GetMembersOfMyGroupQuery query
  ) {

    var user =
        getExistingLeaderUser(
            query.userId()
        );

    var group =
        groupRepository
            .findByLeaderId(user.leaderId())
            .orElseThrow(() ->
                InvalidGroupException
                    .forLeaderWithoutGroup(
                        user.leaderId()
                    )
            );

    return tasksQueryPort
        .getMembersByGroupId(group.getId())
        .stream()
        .map(member ->
            new MemberDetailsDTO(
                member.id(),
                member.username(),
                member.name(),
                member.surname(),
                member.imgUrl()
            )
        )
        .toList();
  }

  private UserOnlyResource getExistingLeaderUser(
      Long userId
  ) {

    var user =
        iamQueryPort.getUserOnlyById(
            userId
        );

    if (user == null) {
      throw new UserNotFoundException(
          userId
      );
    }

    if (user.leaderId() == null) {
      throw InvalidLeaderException
          .forUserIsNotLeader(
              userId
          );
    }

    return user;
  }
}
