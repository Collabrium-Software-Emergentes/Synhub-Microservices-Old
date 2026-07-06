package com.collabrium.notifications.management.interfaces.messaging.listeners;

import com.collabrium.notifications.management.domain.model.events.RequestCreatedEvent;
import com.collabrium.notifications.management.domain.services.RequestsMailService;
import com.collabrium.notifications.management.interfaces.messaging.transform.SendRequestCreatedEmailCommandFromEventAssembler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.collabrium.notifications.shared.infrastructure.rabbitmq.RabbitMQConfig.*;

@Component
public class RequestsEventsListeners {

  private final RequestsMailService requestsMailService;

  public RequestsEventsListeners(
      RequestsMailService requestsMailService
  ) {

    this.requestsMailService = requestsMailService;
  }

  @RabbitListener(queues = REQUEST_CREATED_QUEUE)
  public void handle(RequestCreatedEvent event) {

    var sendRequestCreatedEmailCommand =
        SendRequestCreatedEmailCommandFromEventAssembler.toCommandFromEvent(event);

    requestsMailService.handle(sendRequestCreatedEmailCommand);
  }
}
