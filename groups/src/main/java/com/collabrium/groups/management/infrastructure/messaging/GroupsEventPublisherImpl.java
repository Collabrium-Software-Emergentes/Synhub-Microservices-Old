package com.collabrium.groups.management.infrastructure.messaging;

import com.collabrium.groups.management.application.internal.outboundservices.messaging.GroupsEventPublisher;
import com.collabrium.groups.management.domain.model.events.LeaderCreatedEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import static com.collabrium.groups.shared.infrastructure.config.RabbitMQConfig.GROUPS_EXCHANGE;
import static com.collabrium.groups.shared.infrastructure.config.RabbitMQConfig.LEADER_CREATED_KEY;

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
}
