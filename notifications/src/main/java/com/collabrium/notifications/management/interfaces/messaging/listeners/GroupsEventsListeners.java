package com.collabrium.notifications.management.interfaces.messaging.listeners;

import com.collabrium.notifications.management.domain.model.events.GroupCreatedEvent;
import com.collabrium.notifications.management.domain.model.events.InvitationAcceptedEvent;
import com.collabrium.notifications.management.domain.model.events.InvitationCreatedEvent;
import com.collabrium.notifications.management.domain.services.GroupsMailService;
import com.collabrium.notifications.management.interfaces.messaging.transform.SendGroupCreatedEmailCommandFromEventAssembler;
import com.collabrium.notifications.management.interfaces.messaging.transform.SendInvitationAcceptedEmailCommandFromEventAssembler;
import com.collabrium.notifications.management.interfaces.messaging.transform.SendInvitationCreatedEmailCommandFromEventAssembler;
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
}