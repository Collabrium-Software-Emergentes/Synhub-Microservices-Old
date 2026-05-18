package com.collabrium.tasks.management.interfaces.messaging.listeners;

import com.collabrium.tasks.management.domain.model.events.UserMemberCreatedEvent;
import com.collabrium.tasks.management.domain.services.MemberCommandService;
import com.collabrium.tasks.management.interfaces.messaging.transform.CreateMemberCommandFromEventAssembler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.collabrium.tasks.shared.infrastructure.configuration.rabbitmq.RabbitMQConfiguration.USER_MEMBER_CREATED_QUEUE;

@Component
public class IamEventsListener {

  private final MemberCommandService memberCommandService;

  public IamEventsListener(
      MemberCommandService memberCommandService
  ) {

    this.memberCommandService = memberCommandService;
  }

  @RabbitListener(queues = USER_MEMBER_CREATED_QUEUE)
  public void handle(UserMemberCreatedEvent event) {

    var createMemberCommand = CreateMemberCommandFromEventAssembler.toCommandFromEvent(event);

    memberCommandService.handle(createMemberCommand);
  }
}