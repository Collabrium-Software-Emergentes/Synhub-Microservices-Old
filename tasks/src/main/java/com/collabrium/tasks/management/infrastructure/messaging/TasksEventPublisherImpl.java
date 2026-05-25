package com.collabrium.tasks.management.infrastructure.messaging;

import com.collabrium.tasks.management.application.internal.outboundservices.messaging.TasksEventPublisher;
import com.collabrium.tasks.management.domain.model.events.MemberCreatedEvent;
import com.collabrium.tasks.management.domain.model.events.MemberLeftGroupEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import static com.collabrium.tasks.shared.infrastructure.configuration.rabbitmq.RabbitMQConfiguration.*;

@Service
public class TasksEventPublisherImpl implements TasksEventPublisher {

  private final AmqpTemplate rabbitTemplate;

  public TasksEventPublisherImpl(AmqpTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void publishMemberCreated(MemberCreatedEvent event) {

    rabbitTemplate.convertAndSend(
        TASKS_EXCHANGE,
        MEMBER_CREATED_KEY,
        event
    );
  }

  @Override
  public void publishMemberLeftGroup(MemberLeftGroupEvent event) {

    rabbitTemplate.convertAndSend(
        TASKS_EXCHANGE,
        MEMBER_LEFT_GROUP_KEY,
        event
    );
  }
}