package com.collabrium.notifications.management.application.internal.outboundservices.email;

public interface SendEmailService {

  void sendGroupCreatedEmail(
      String to,
      String groupName,
      String groupDescription,
      String groupImage,
      String code
  );
}