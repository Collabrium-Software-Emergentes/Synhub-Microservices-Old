package com.collabrium.metrics.management.application.internal.outboundservices.ports;

import com.collabrium.metrics.management.application.internal.dto.TaskOverviewDTO;

public interface PdfGenerationPort {
    byte[] generateMetricsReport(TaskOverviewDTO taskOverview);
}