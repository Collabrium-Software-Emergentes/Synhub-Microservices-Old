package com.collabrium.tasks.management.application.internal.commandservices;

import com.collabrium.tasks.management.application.internal.outboundservices.messaging.TasksEventPublisher;
import com.collabrium.tasks.management.application.internal.outboundservices.ports.IamQueryPort;
import com.collabrium.tasks.management.domain.exceptions.InvalidMemberException;
import com.collabrium.tasks.management.domain.exceptions.UserNotFoundException;
import com.collabrium.tasks.management.domain.model.aggregates.Member;
import com.collabrium.tasks.management.domain.model.commands.*;
import com.collabrium.tasks.management.domain.model.events.MemberCreatedEvent;
import com.collabrium.tasks.management.domain.model.events.MemberLeftGroupEvent;
import com.collabrium.tasks.management.domain.model.valueobjects.GroupId;
import com.collabrium.tasks.management.domain.services.MemberCommandService;
import com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories.MemberRepository;
import com.collabrium.tasks.management.infrastructure.persistence.jpa.repositories.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberCommandServiceImpl implements MemberCommandService {

  private final MemberRepository memberRepository;
  private final TaskRepository taskRepository;
  private final IamQueryPort iamQueryPort;
  private final TasksEventPublisher tasksEventPublisher;

  public MemberCommandServiceImpl(
      MemberRepository memberRepository,
      TaskRepository taskRepository,
      IamQueryPort iamQueryPort,
      TasksEventPublisher tasksEventPublisher
  ) {

    this.memberRepository = memberRepository;
    this.taskRepository = taskRepository;
    this.iamQueryPort = iamQueryPort;
    this.tasksEventPublisher = tasksEventPublisher;
  }

  @Override
  public void handle(
      CreateMemberCommand command
  ) {

    validateCreateCommand(command);

    var member =
        new Member(command);

    var savedMember =
        memberRepository.save(member);

    tasksEventPublisher.publishMemberCreated(
        new MemberCreatedEvent(
            command.userId(),
            savedMember.getId()
        )
    );
  }

  @Override
  @Transactional
  public Optional<Member> handle(
      AssignMemberToGroupCommand command
  ) {

    validateAssignGroupCommand(command);

    var member =
        getExistingMember(command.memberId());

    member.assignGroup(
        new GroupId(command.groupId())
    );

    return Optional.of(
        memberRepository.save(member)
    );
  }

  @Override
  @Transactional
  public Optional<Member> handle(
      RemoveMemberFromGroupCommand command
  ) {

    validateRemoveGroupCommand(command);

    var member =
        getExistingMember(command.memberId());

    detachMemberAndDeleteTasks(member);

    return Optional.of(member);
  }

  @Override
  @Transactional
  public void handle(
      LeaveGroupCommand command
  ) {

    validateLeaveGroupCommand(command);

    var member =
        getExistingMemberFromUser(command.userId());

    var groupId =
        member.getGroupId();

    if (groupId == null) {
      throw InvalidMemberException
          .forMemberWithoutGroup(member.getId());
    }

    detachMemberAndDeleteTasks(member);

    tasksEventPublisher.publishMemberLeftGroup(
        new MemberLeftGroupEvent(groupId.value())
    );
  }

  @Override
  @Transactional
  public void handle(
      DeleteGroupDataCommand command
  ) {

    validateDeleteGroupDataCommand(command);

    var groupId =
        new GroupId(command.groupId());

    var members =
        memberRepository.findMembersByGroupId(groupId);

    members.forEach(this::detachMemberFromGroup);

    taskRepository.deleteAllByGroupId(groupId);
  }

  private void detachMemberFromGroup(
      Member member
  ) {

    member.removeGroup();

    memberRepository.save(member);
  }

  private void detachMemberAndDeleteTasks(
      Member member
  ) {

    taskRepository.deleteAllByMember_Id(
        member.getId()
    );

    detachMemberFromGroup(member);
  }

  private Member getExistingMember(
      Long memberId
  ) {

    return memberRepository
        .findById(memberId)
        .orElseThrow(() ->
            InvalidMemberException.forMemberNotFound(memberId)
        );
  }

  private Member getExistingMemberFromUser(
      Long userId
  ) {

    var user =
        iamQueryPort.getUserOnlyById(userId);

    if (user == null) {
      throw UserNotFoundException.forId(userId);
    }

    if (user.memberId() == null) {
      throw InvalidMemberException
          .forUserIsNotMember(userId);
    }

    return getExistingMember(user.memberId());
  }

  private void validateCreateCommand(
      CreateMemberCommand command
  ) {

    if (command == null) {
      throw InvalidMemberException
          .forNullCreateCommand();
    }
  }

  private void validateAssignGroupCommand(
      AssignMemberToGroupCommand command
  ) {

    if (command == null) {
      throw InvalidMemberException
          .forNullAddGroupCommand();
    }
  }

  private void validateRemoveGroupCommand(
      RemoveMemberFromGroupCommand command
  ) {

    if (command == null) {
      throw InvalidMemberException
          .forNullRemoveGroupCommand();
    }
  }

  private void validateLeaveGroupCommand(
      LeaveGroupCommand command
  ) {

    if (command == null) {
      throw InvalidMemberException
          .forNullLeaveGroupCommand();
    }

    if (command.userId() == null) {
      throw InvalidMemberException
          .forNullUserId();
    }
  }

  private void validateDeleteGroupDataCommand(
      DeleteGroupDataCommand command
  ) {

    if (command == null) {
      throw InvalidMemberException
          .forNullDeleteMembersCommand();
    }

    if (command.groupId() == null) {
      throw InvalidMemberException
          .forNullGroupId();
    }
  }
}
