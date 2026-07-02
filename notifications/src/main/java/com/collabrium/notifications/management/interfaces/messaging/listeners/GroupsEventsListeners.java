package com.collabrium.notifications.management.interfaces.messaging.listeners;

import com.collabrium.notifications.management.domain.model.events.*;
import com.collabrium.notifications.management.domain.services.GroupsMailService;
import com.collabrium.notifications.management.interfaces.messaging.transform.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.collabrium.notifications.shared.infrastructure.rabbitmq.RabbitMQConfig.*;

@Component
public class GroupsEventsListeners {

  private final GroupsMailService groupsMailService;

  public GroupsEventsListeners(
      GroupsMailService groupsMailService
  ) {

    this.groupsMailService = groupsMailService;
  }

  @RabbitListener(queues = GROUP_CREATED_QUEUE)
  public void handle(GroupCreatedEvent event) {

    var sendGroupCreatedEmailCommand =
        SendGroupCreatedEmailCommandFromEventAssembler.toCommandFromEvent(event);

    groupsMailService.handle(sendGroupCreatedEmailCommand);
  }

  @RabbitListener(queues = INVITATION_ACCEPTED_QUEUE)
  public void handle(InvitationAcceptedEvent event) {

    var sendInvitationAcceptedEmailCommand =
        SendInvitationAcceptedEmailCommandFromEventAssembler.toCommandFromEvent(event);

    groupsMailService.handle(sendInvitationAcceptedEmailCommand);
  }

  @RabbitListener(queues = INVITATION_CREATED_QUEUE)
  public void handle(InvitationCreatedEvent event) {

    var sendInvitationCreatedEmailCommand =
        SendInvitationCreatedEmailCommandFromEventAssembler.toCommandFromEvent(event);

    groupsMailService.handle(sendInvitationCreatedEmailCommand);
  }

  @RabbitListener(queues = MEMBER_REMOVED_FROM_GROUP_QUEUE)
  public void handle(RemoveMemberEvent event) {

    var sendRemoveMemberEmailCommand =
        SendRemoveMemberEmailCommandFromEventAssembler.toCommandFromEvent(event);

    groupsMailService.handle(sendRemoveMemberEmailCommand);
  }

  @RabbitListener(queues = GROUP_DELETED_QUEUE)
  public void handle(GroupDeletedEvent event) {

    var sendGroupDeletedEmailCommand =
        SendGroupDeletedEmailCommandFromEventAssembler.toCommandFromEvent(event);

    groupsMailService.handle(sendGroupDeletedEmailCommand);
  }
}