package com.collabrium.notifications.management.application.internal.commandservices;

import com.collabrium.notifications.management.application.internal.outboundservices.email.SendEmailService;
import com.collabrium.notifications.management.domain.model.commands.*;
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

    sendEmailService.sendInvitationAcceptedEmail(
        command.memberEmail(),
        command.groupName(),
        command.groupDescription(),
        command.groupImgUrl(),
        command.groupCode()
    );
  }

  @Override
  public void handle(SendInvitationCreatedEmailCommand command) {

    sendEmailService.sendInvitationCreatedEmail(
        command.leaderEmail(),
        command.memberUsername(),
        command.memberName(),
        command.memberSurname(),
        command.memberImgUrl(),
        command.memberEmail()
    );
  }

  @Override
  public void handle(SendRemoveMemberEmailCommand command) {

    sendEmailService.sendRemoveMemberFromGroupEmail(
        command.memberEmail(),
        command.groupName(),
        command.groupImageUrl(),
        command.groupCode(),
        command.leaderEmail()
    );
  }

  @Override
  public void handle(SendGroupDeletedEmailCommand command) {

    for (String email : command.membersEmails()) {

      sendEmailService.sendGroupDeletedEmail(
          email,
          command.groupName(),
          command.groupDescription(),
          command.groupCode(),
          command.leaderEmail()
      );
    }
  }
}