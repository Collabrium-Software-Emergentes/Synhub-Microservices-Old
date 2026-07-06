package com.collabrium.iam.authentication.infrastructure.email;

import com.collabrium.iam.authentication.application.internal.outboundservices.email.EmailService;
import jakarta.mail.MessagingException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;

  @Value("${spring.mail.from:no-reply@localhost}")
  private String from;

  @Override
  public void sendVerificationEmail(String to, String verificationLink) {
    var mimeMessage = mailSender.createMimeMessage();

    try {
      var helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject("Verify your email");

      var context = new Context();
      context.setVariable("verificationLink", verificationLink);
      var htmlContent = templateEngine.process("email-verification", context);

      helper.setText(htmlContent, true);
      mailSender.send(mimeMessage);
    } catch (MessagingException exception) {
      throw new IllegalStateException(buildDetailedErrorMessage("MessagingException", exception), exception);
    } catch (MailException exception) {
      throw new IllegalStateException(buildDetailedErrorMessage("MailException", exception), exception);
    }
  }

  private String buildDetailedErrorMessage(String type, Exception exception) {
    var rootCause = exception.getCause();
    var rootCauseMessage = rootCause != null && rootCause.getMessage() != null
        ? rootCause.getClass().getSimpleName() + ": " + rootCause.getMessage()
        : "no root cause message";

    var message = exception.getMessage() != null ? exception.getMessage() : "no exception message";

    return "Failed to send verification email via Brevo. "
        + "Type=" + type
        + ", message=" + message
        + ", rootCause=" + rootCauseMessage;
  }
}
