package com.collabrium.tasks.management.interfaces.messaging.listeners;

import com.collabrium.tasks.management.domain.model.events.InvitationAcceptedEvent;
import com.collabrium.tasks.management.domain.model.events.MemberRemovedFromGroupEvent;
import com.collabrium.tasks.management.domain.services.MemberCommandService;
import com.collabrium.tasks.management.interfaces.messaging.transform.AssignMemberToGroupCommandFromEventAssembler;
import com.collabrium.tasks.management.interfaces.messaging.transform.RemoveMemberFromGroupCommandFromEventAssembler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.collabrium.tasks.shared.infrastructure.configuration.rabbitmq.RabbitMQConfiguration.INVITATION_ACCEPTED_QUEUE;
import static com.collabrium.tasks.shared.infrastructure.configuration.rabbitmq.RabbitMQConfiguration.MEMBER_REMOVED_FROM_GROUP_QUEUE;

@Component
public class GroupsEventsListener {

  private final MemberCommandService memberCommandService;

  public GroupsEventsListener(
      MemberCommandService memberCommandService
  ) {

    this.memberCommandService = memberCommandService;
  }

  @RabbitListener(queues = INVITATION_ACCEPTED_QUEUE)
  public void handleInvitationAccepted(
      InvitationAcceptedEvent event
  ) {

    var command =
        AssignMemberToGroupCommandFromEventAssembler
            .toCommandFromEvent(event);

    memberCommandService.handle(command);
  }

  @RabbitListener(queues = MEMBER_REMOVED_FROM_GROUP_QUEUE)
  public void handleRemovedMemberFromGroup(
      MemberRemovedFromGroupEvent event
  ) {

    var command =
        RemoveMemberFromGroupCommandFromEventAssembler
            .toCommandFromEvent(event);

    memberCommandService.handle(command);
  }
}