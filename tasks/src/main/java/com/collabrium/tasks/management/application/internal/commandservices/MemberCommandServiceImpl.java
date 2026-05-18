package com.collabrium.tasks.management.application.internal.commandservices;

import com.collabrium.tasks.management.application.internal.outboundservices.messaging.TasksEventPublisher;
import com.collabrium.tasks.management.domain.exceptions.InvalidMemberException;
import com.collabrium.tasks.management.domain.model.aggregates.Member;
import com.collabrium.tasks.management.domain.model.commands.AddGroupToMemberCommand;
import com.collabrium.tasks.management.domain.model.commands.CreateMemberCommand;
import com.collabrium.tasks.management.domain.model.commands.DeleteMembersByGroupIdCommand;
import com.collabrium.tasks.management.domain.model.commands.RemoveMemberFromGroupCommand;
import com.collabrium.tasks.management.domain.model.events.MemberCreatedEvent;
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
  private final TasksEventPublisher tasksEventPublisher;

  public MemberCommandServiceImpl(
      MemberRepository memberRepository,
      TaskRepository taskRepository,
      TasksEventPublisher tasksEventPublisher
  ) {

    this.memberRepository = memberRepository;
    this.taskRepository = taskRepository;
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
  public Optional<Member> handle(AddGroupToMemberCommand command) {

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

  private void validateAddGroupCommand(AddGroupToMemberCommand command) {

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
}