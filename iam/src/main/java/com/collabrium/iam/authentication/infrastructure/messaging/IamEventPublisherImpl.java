package com.collabrium.iam.authentication.infrastructure.messaging;

import com.collabrium.iam.authentication.application.internal.outboundservices.messaging.IamEventPublisher;
import com.collabrium.iam.authentication.domain.model.events.UserLeaderCreatedEvent;
import com.collabrium.iam.authentication.domain.model.events.UserMemberCreatedEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import static com.collabrium.iam.shared.infrastructure.config.RabbitMQConfig.IAM_EXCHANGE;
import static com.collabrium.iam.shared.infrastructure.config.RabbitMQConfig.USER_LEADER_CREATED_KEY;
import static com.collabrium.iam.shared.infrastructure.config.RabbitMQConfig.USER_MEMBER_CREATED_KEY;

@Service
public class IamEventPublisherImpl implements IamEventPublisher {

  private final AmqpTemplate rabbitTemplate;

  public IamEventPublisherImpl(AmqpTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishUserLeaderCreated(UserLeaderCreatedEvent event) {
    rabbitTemplate.convertAndSend(
        IAM_EXCHANGE,
        USER_LEADER_CREATED_KEY,
        event
    );
  }

  public void publishUserMemberCreated(UserMemberCreatedEvent event) {
    rabbitTemplate.convertAndSend(
        IAM_EXCHANGE,
        USER_MEMBER_CREATED_KEY,
        event
    );
  }
}
