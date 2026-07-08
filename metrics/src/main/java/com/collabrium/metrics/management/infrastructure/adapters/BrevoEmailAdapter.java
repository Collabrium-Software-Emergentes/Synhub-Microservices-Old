package com.collabrium.metrics.management.infrastructure.adapters;

import com.collabrium.metrics.management.application.internal.outboundservices.ports.EmailNotificationPort;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class BrevoEmailAdapter implements EmailNotificationPort {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}") // Asegúrate de que esto coincida con tu application.yml
    private String fromEmail;

    public BrevoEmailAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendReport(String toEmail, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Tu Reporte de Métricas");
            helper.setText("Hola, adjunto encontrarás el PDF con tus métricas gracias a Feli de UPC tu acompañante incondicional.");

            // Adjuntar el PDF
            helper.addAttachment("Reporte_Metricas.pdf", new ByteArrayResource(pdfBytes));

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        }
    }
}