package com.collabrium.notifications.management.application.internal.commandservices;

import com.collabrium.notifications.management.application.internal.outboundservices.email.SendEmailService;
import com.collabrium.notifications.management.domain.model.commands.SendRequestCreatedEmailCommand;
import com.collabrium.notifications.management.domain.services.RequestsMailService;
import org.springframework.stereotype.Service;

@Service
public class RequestsMailServiceImpl implements RequestsMailService {

  private final SendEmailService sendEmailService;

  public RequestsMailServiceImpl(
      SendEmailService sendEmailService
  ) {

    this.sendEmailService = sendEmailService;
  }

  @Override
  public void handle(SendRequestCreatedEmailCommand command) {

    sendEmailService.sendRequestCreatedEmail(
        command.leaderEmail(),
        command.memberUsername(),
        command.memberName(),
        command.memberSurname(),
        command.taskTitle(),
        command.requestDescription(),
        command.requestType(),
        command.imageUrl()
    );
  }
}
