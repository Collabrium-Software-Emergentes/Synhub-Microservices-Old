package com.collabrium.notifications.management.application.internal.commandservices;

import com.collabrium.notifications.management.application.internal.outboundservices.email.SendEmailService;
import com.collabrium.notifications.management.domain.model.commands.SendGroupCreatedEmailCommand;
import com.collabrium.notifications.management.domain.model.commands.SendInvitationAcceptedEmailCommand;
import com.collabrium.notifications.management.domain.services.GroupsMailService;
import org.springframework.stereotype.Service;

@Service
public class GroupsMailServiceImpl implements GroupsMailService {

  private final SendEmailService sendEmailService;

  public GroupsMailServiceImpl(
      SendEmailService sendEmailService
  ) {

    this.sendEmailService = sendEmailService;
  }

  @Override
  public void handle(SendGroupCreatedEmailCommand command) {

    sendEmailService.sendGroupCreatedEmail(
        command.leaderEmail(),
        command.groupName(),
        command.groupDescription(),
        command.imgUrl(),
        command.code()
    );
  }

  @Override
  public void handle(SendInvitationAcceptedEmailCommand command) {

  }
}