package com.collabrium.iam.authentication.application.internal.outboundservices.email;

public interface EmailService {

  void sendVerificationEmail(String to, String verificationLink);
}
