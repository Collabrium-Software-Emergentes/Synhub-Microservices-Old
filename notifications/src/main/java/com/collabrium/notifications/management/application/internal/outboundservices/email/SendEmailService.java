package com.collabrium.notifications.management.application.internal.outboundservices.email;

public interface SendEmailService {

  void sendGroupCreatedEmail(
      String to,
      String groupName,
      String groupDescription,
      String groupImage,
      String code
  );

  void sendInvitationAcceptedEmail(
      String to,
      String groupName,
      String groupDescription,
      String groupImage,
      String code
  );

  void sendInvitationCreatedEmail(
      String to,
      String memberUsername,
      String memberName,
      String memberSurname,
      String memberImgUrl,
      String memberEmail
  );
}