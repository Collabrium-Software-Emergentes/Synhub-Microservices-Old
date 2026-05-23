package com.collabrium.tasks.management.application.internal.commandservices;

import com.collabrium.tasks.management.application.internal.dto.TaskDetailsDTO;
import com.collabrium.tasks.management.application.internal.mappers.TaskDetailsDTOAssembler;
import com.collabrium.tasks.management.application.internal.outboundservices.ports.GroupsQueryPort;
import com.collabrium.tasks.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.tasks.management.domain.exceptions.InvalidTaskException;
import com.collabrium.tasks.management.domain.exceptions.MemberNotFoundException;
import com.collabrium.tasks.management.domain.exceptions.UserNotFoundException;
import com.collabrium.tasks.management.domain.model.aggregates.Member;
import com.collabrium.tasks.management.domain.model.aggregates.Task;
import com.collabrium.tasks.management.domain.model.commands.CreateTaskCommand;
import com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories.MemberRepository;
import com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories.TaskRepository;
import com.collabrium.tasks.shared.infrastructure.clients.groups.resources.GroupOnlyResource;
import com.collabrium.tasks.shared.infrastructure.clients.iam.resources.UserOnlyResource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskDetailsCommandService {

  private final TaskRepository taskRepository;
  private final MemberRepository memberRepository;
  private final IamQueryPort iamQueryPort;
  private final GroupsQueryPort groupsQueryPort;

  public TaskDetailsCommandService(
      TaskRepository taskRepository,
      MemberRepository memberRepository,
      IamQueryPort iamQueryPort, GroupsQueryPort groupsQueryPort) {

    this.taskRepository = taskRepository;
    this.memberRepository = memberRepository;
    this.iamQueryPort = iamQueryPort;
    this.groupsQueryPort = groupsQueryPort;
  }

  public Optional<TaskDetailsDTO> handle(CreateTaskCommand command) {

    var leaderUser =
        iamQueryPort.getUserOnlyById(
            command.userId()
        );

    validateLeaderUser(
        command.userId(),
        leaderUser
    );

    var member =
        memberRepository
            .findById(command.memberId())
            .orElseThrow(() ->
                MemberNotFoundException.forId(
                    command.memberId()
                )
            );

    validateMemberGroup(member);

    var group =
        validateGroupExists(member);

    validateLeaderBelongsToGroup(
        leaderUser,
        group
    );

    var memberUser =
        iamQueryPort.getUserByMemberId(
            command.memberId()
        );

    if (memberUser == null) {
      throw UserNotFoundException.forMember(
          command.memberId()
      );
    }

    var task = new Task(command);

    task.setMember(member);
    task.setGroupId(member.getGroupId());

    var savedTask =
        taskRepository.save(task);

    var dto =
        TaskDetailsDTOAssembler.toDTO(
            savedTask,
            member,
            memberUser
        );

    return Optional.of(dto);
  }

  private void validateLeaderUser(
      Long userId,
      UserOnlyResource user
  ) {

    if (user == null) {
      throw UserNotFoundException.forId(userId);
    }

    if (user.leaderId() == null) {
      throw InvalidTaskException.forUserIsNotLeader(userId);
    }
  }

  private void validateMemberGroup(
      Member member
  ) {

    if (member.getGroupId() == null) {
      throw InvalidTaskException.forMemberWithoutGroup(
          member.getId()
      );
    }
  }

  private GroupOnlyResource validateGroupExists(
      Member member
  ) {

    var groupId =
        member.getGroupId().value();

    var group =
        groupsQueryPort.getGroupOnlyById(
            groupId
        );

    if (group == null) {
      throw InvalidTaskException.forGroupNotFound(
          groupId
      );
    }

    return group;
  }

  private void validateLeaderBelongsToGroup(
      UserOnlyResource leaderUser,
      GroupOnlyResource group
  ) {

    if (
        group.leaderId() == null ||
            !group.leaderId().equals(
                leaderUser.leaderId()
            )
    ) {

      throw InvalidTaskException
          .forLeaderNotBelongingToGroup(
              leaderUser.id(),
              group.id()
          );
    }
  }
}
