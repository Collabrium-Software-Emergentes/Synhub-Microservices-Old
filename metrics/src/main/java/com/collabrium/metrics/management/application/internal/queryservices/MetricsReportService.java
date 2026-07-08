package com.collabrium.metrics.management.application.internal.queryservices;

import com.collabrium.metrics.management.application.internal.dto.TaskOverviewDTO;
import com.collabrium.metrics.management.application.internal.outboundservices.ports.EmailNotificationPort;
import com.collabrium.metrics.management.application.internal.outboundservices.ports.PdfGenerationPort;
import org.springframework.stereotype.Service;

@Service
public class MetricsReportService {

    private final PdfGenerationPort pdfGenerationPort;
    private final EmailNotificationPort emailNotificationPort;

    public MetricsReportService(PdfGenerationPort pdfGenerationPort, EmailNotificationPort emailNotificationPort) {
        this.pdfGenerationPort = pdfGenerationPort;
        this.emailNotificationPort = emailNotificationPort;
    }

    public byte[] getPdfReport(TaskOverviewDTO taskOverview) {
        return pdfGenerationPort.generateMetricsReport(taskOverview);
    }

    public void sendPdfReportByEmail(String email, TaskOverviewDTO taskOverview) {
        byte[] pdfBytes = pdfGenerationPort.generateMetricsReport(taskOverview);
        emailNotificationPort.sendReport(email, pdfBytes);
    }
}