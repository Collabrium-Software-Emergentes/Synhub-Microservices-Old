package com.collabrium.notifications.management.infrastructure.email;

import com.collabrium.notifications.management.application.internal.outboundservices.email.SendEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendEmailServiceImpl implements SendEmailService {

  @Value("${app.mail.from}")
  private String from;

  private final EmailTemplateFactory emailTemplateFactory;
  private final JavaMailSender mailSender;

  public SendEmailServiceImpl(
      EmailTemplateFactory emailTemplateFactory,
      JavaMailSender mailSender
  ) {

    this.emailTemplateFactory = emailTemplateFactory;
    this.mailSender = mailSender;
  }

  @Override
  public void sendGroupCreatedEmail(
      String to,
      String groupName,
      String groupDescription,
      String groupImage,
      String code
  ) {

    try {

      MimeMessage message = mailSender.createMimeMessage();

      MimeMessageHelper helper =
          new MimeMessageHelper(
              message,
              MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              "UTF-8"
          );

      String html =
          emailTemplateFactory
              .buildGroupCreatedEmailTemplate(
                  groupName,
                  groupDescription,
                  groupImage,
                  code
              );

      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject(
          "Tu grupo ya está listo"
      );

      helper.setText(html, true);

      mailSender.send(message);

    } catch (MessagingException e) {
      throw new RuntimeException(
          "Error sending email",
          e
      );
    }
  }

  @Override
  public void sendInvitationAcceptedEmail(
      String to,
      String groupName,
      String groupDescription,
      String groupImage,
      String code
  ) {

    try {

      MimeMessage message = mailSender.createMimeMessage();

      MimeMessageHelper helper =
          new MimeMessageHelper(
              message,
              MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              "UTF-8"
          );

      String html =
          emailTemplateFactory
              .buildInvitationAcceptedEmailTemplate(
                  groupName,
                  groupDescription,
                  groupImage,
                  code
              );

      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject(
          "Welcome to " + groupName + "! 🎉"
      );

      helper.setText(html, true);

      mailSender.send(message);

    } catch (MessagingException e) {

      throw new RuntimeException(
          "Error sending invitation accepted email",
          e
      );
    }
  }

  @Override
  public void sendInvitationCreatedEmail(
      String to,
      String memberUsername,
      String memberName,
      String memberSurname,
      String memberImgUrl,
      String memberEmail
  ) {

    try {

      MimeMessage message = mailSender.createMimeMessage();

      MimeMessageHelper helper =
          new MimeMessageHelper(
              message,
              MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              "UTF-8"
          );

      String html =
          emailTemplateFactory
              .buildInvitationCreatedEmailTemplate(
                  memberUsername,
                  memberName,
                  memberSurname,
                  memberImgUrl,
                  memberEmail
              );

      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject(
          "New membership request from @" + memberUsername
      );

      helper.setText(html, true);

      mailSender.send(message);

    } catch (MessagingException e) {

      throw new RuntimeException(
          "Error sending invitation created email",
          e
      );
    }
  }
}