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
  public void handle(CreateMemberCommand command) {

    validateCreateCommand(command);

    var member = new Member(command);

    var savedMember = memberRepository.save(member);

    var memberCreatedEvent =
        new MemberCreatedEvent(
            command.userId(),
            savedMember.getId()
        );

    tasksEventPublisher.publishMemberCreated(
        memberCreatedEvent
    );

  }

  @Override
  public Optional<Member> handle(AssignMemberToGroupCommand command) {

    validateAddGroupCommand(command);

    var member = memberRepository
        .findById(command.memberId())
        .orElseThrow(() ->
            InvalidMemberException.forMemberNotFound(
                command.memberId()
            )
        );

    member.assignGroup(
        new GroupId(command.groupId())
    );

    var updatedMember =
        memberRepository.save(member);

    return Optional.of(updatedMember);
  }

  @Override
  @Transactional
  public Optional<Member> handle(RemoveMemberFromGroupCommand command) {

    validateRemoveGroupCommand(command);

    var member = memberRepository
        .findById(command.memberId())
        .orElseThrow(() ->
            InvalidMemberException.forMemberNotFound(
                command.memberId()
            )
        );

    detachMemberFromGroup(member);

    return Optional.of(member);
  }

  @Override
  public void handle(DeleteMembersByGroupIdCommand command) {

    validateDeleteMembersCommand(command);

    var groupId =
        new GroupId(command.groupId());

    var members =
        memberRepository.findMembersByGroupId(groupId);

    for (var member : members) {
      detachMemberFromGroup(member);
    }
  }

  @Override
  @Transactional
  public void handle(LeaveGroupCommand command) {

    validateLeaveGroupCommand(command);

    var member =
        getExistingMemberFromUser(
            command.userId()
        );

    var groupId =
        member.getGroupId();

    if (groupId == null) {
      throw InvalidMemberException
          .forMemberWithoutGroup(
              member.getId()
          );
    }

    detachMemberFromGroup(member);

    var memberLeftGroupEvent =
        new MemberLeftGroupEvent(
            groupId.value()
        );

    tasksEventPublisher.publishMemberLeftGroup(
        memberLeftGroupEvent
    );
  }

  private void detachMemberFromGroup(Member member) {

    taskRepository.deleteAllByMember_Id(
        member.getId()
    );

    member.removeGroup();

    memberRepository.save(member);
  }

  private void validateCreateCommand(CreateMemberCommand command) {

    if (command == null) {
      throw InvalidMemberException.forNullCreateCommand();
    }
  }

  private void validateAddGroupCommand(AssignMemberToGroupCommand command) {

    if (command == null) {
      throw InvalidMemberException.forNullAddGroupCommand();
    }
  }

  private void validateRemoveGroupCommand(RemoveMemberFromGroupCommand command) {

    if (command == null) {
      throw InvalidMemberException.forNullRemoveGroupCommand();
    }
  }

  private void validateDeleteMembersCommand(DeleteMembersByGroupIdCommand command) {

    if (command == null) {
      throw InvalidMemberException.forNullDeleteMembersCommand();
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

  private Member getExistingMemberFromUser(
      Long userId
  ) {

    var user =
        iamQueryPort.getUserOnlyById(userId);

    if (user == null) {
      throw UserNotFoundException
          .forId(userId);
    }

    if (user.memberId() == null) {
      throw InvalidMemberException
          .forUserIsNotMember(userId);
    }

    return memberRepository
        .findById(user.memberId())
        .orElseThrow(() ->
            InvalidMemberException
                .forMemberNotFound(
                    user.memberId()
                )
        );
  }
}