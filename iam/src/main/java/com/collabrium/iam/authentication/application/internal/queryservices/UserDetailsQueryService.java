package com.collabrium.iam.authentication.application.internal.queryservices;

import com.collabrium.iam.authentication.application.internal.outboundservices.ports.GroupsQueryPort;
import com.collabrium.iam.authentication.application.internal.outboundservices.ports.TasksQueryPort;
import com.collabrium.iam.authentication.application.internal.queryservices.dto.UserDetailsDTO;
import com.collabrium.iam.authentication.domain.model.aggregates.User;
import com.collabrium.iam.authentication.domain.model.queries.GetAllUsersQuery;
import com.collabrium.iam.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.collabrium.iam.shared.infrastructure.clients.groups.resources.LeaderResource;
import com.collabrium.iam.shared.infrastructure.clients.groups.resources.MemberResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsQueryService {

  private final UserRepository userRepository;
  private final GroupsQueryPort groupsQueryPort;
  private final TasksQueryPort tasksQueryPort;

  public UserDetailsQueryService(
      UserRepository userRepository,
      GroupsQueryPort groupsQueryPort,
      TasksQueryPort tasksQueryPort
  ) {
    this.userRepository = userRepository;
    this.groupsQueryPort = groupsQueryPort;
    this.tasksQueryPort = tasksQueryPort;
  }

  public List<UserDetailsDTO> handle(GetAllUsersQuery query) {

    var users = userRepository.findAllWithRoles();

    return users.stream()
        .map(this::buildUserDetails)
        .toList();
  }

  private UserDetailsDTO buildUserDetails(User user) {

    LeaderResource leaderResource = null;
    MemberResource memberResource = null;

    if (user.getLeaderId() != null) {
      leaderResource = groupsQueryPort.getLeaderById(user.getLeaderId().value());
    }

    if (user.getMemberId() != null) {
      memberResource = tasksQueryPort.getMemberById(user.getMemberId().value());
    }

    return new UserDetailsDTO(
        user.getId(),
        user.getUsername(),
        user.getName(),
        user.getSurname(),
        user.getImgUrl(),
        user.getEmail(),
        user.getRoles()
            .stream()
            .map(role -> role.getName().name())
            .toList(),
        leaderResource,
        memberResource
    );
  }
}