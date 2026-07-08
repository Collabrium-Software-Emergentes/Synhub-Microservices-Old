package com.collabrium.metrics.management.application.internal.outboundservices.ports;

public interface EmailNotificationPort {
    void sendReport(String toEmail, byte[] pdfBytes);
}