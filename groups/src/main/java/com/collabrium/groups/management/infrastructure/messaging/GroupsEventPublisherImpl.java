package com.collabrium.groups.management.infrastructure.messaging;

import com.collabrium.groups.management.application.internal.outboundservices.messaging.GroupsEventPublisher;
import com.collabrium.groups.management.domain.model.events.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import static com.collabrium.groups.shared.infrastructure.configuration.rabbitmq.RabbitMQConfiguration.*;

@Service
public class GroupsEventPublisherImpl implements GroupsEventPublisher {

  private final AmqpTemplate rabbitTemplate;

  public GroupsEventPublisherImpl(
      AmqpTemplate rabbitTemplate
  ) {

    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void publishLeaderCreated(LeaderCreatedEvent event) {

    rabbitTemplate.convertAndSend(
        GROUPS_EXCHANGE,
        LEADER_CREATED_KEY,
        event
    );
  }

  @Override
  public void publishInvitationAccepted(InvitationAcceptedEvent event) {

    rabbitTemplate.convertAndSend(
        GROUPS_EXCHANGE,
        INVITATION_ACCEPTED_KEY,
        event
    );
  }

  @Override
  public void publishMemberRemovedFromGroup(RemoveMemberEvent event) {

    rabbitTemplate.convertAndSend(
        GROUPS_EXCHANGE,
        MEMBER_REMOVED_FROM_GROUP_KEY,
        event
    );
  }

  @Override
  public void publishGroupDeleted(GroupDeletedEvent event) {

    rabbitTemplate.convertAndSend(
        GROUPS_EXCHANGE,
        GROUP_DELETED_KEY,
        event
    );
  }

  @Override
  public void publishGroupCreated(GroupCreatedEvent event) {

    rabbitTemplate.convertAndSend(
        GROUPS_EXCHANGE,
        GROUP_CREATED_KEY,
        event
    );
  }

  @Override
  public void publishInvitationCreated(InvitationCreatedEvent event) {

    rabbitTemplate.convertAndSend(
        GROUPS_EXCHANGE,
        INVITATION_CREATED_KEY,
        event
    );
  }
}