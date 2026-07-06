package com.collabrium.requests.management.infrastructure.messaging;

import com.collabrium.requests.management.application.internal.outboundservices.messaging.RequestsEventPublisher;
import com.collabrium.requests.management.domain.model.events.RequestCreatedEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import static com.collabrium.requests.shared.infrastructure.configuration.rabbitmq.RabbitMQConfiguration.*;

@Service
public class RequestsEventPublisherImpl implements RequestsEventPublisher {

  private final AmqpTemplate rabbitTemplate;

  public RequestsEventPublisherImpl(
      AmqpTemplate rabbitTemplate
  ) {

    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void publishRequestCreated(RequestCreatedEvent event) {

    rabbitTemplate.convertAndSend(
        REQUESTS_EXCHANGE,
        REQUEST_CREATED_KEY,
        event
    );
  }
}
