package com.collabrium.metrics.management.infrastructure.adapters;

import com.collabrium.metrics.management.application.internal.dto.TaskOverviewDTO;
import com.collabrium.metrics.management.application.internal.outboundservices.ports.PdfGenerationPort;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class ItextPdfAdapter implements PdfGenerationPort {

    @Override
    public byte[] generateMetricsReport(TaskOverviewDTO taskOverview) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Reporte Oficial de Métricas - Collabrium").setBold().setFontSize(16));
        document.add(new Paragraph("A continuación se muestra el resumen de tareas de tu grupo:"));

        // Inyectando los datos reales del DTO
        document.add(new Paragraph("Tipo de Métrica: " + taskOverview.type()));
        document.add(new Paragraph("Total: " + taskOverview.value()));

        if (taskOverview.details() != null) {
            taskOverview.details().forEach((estado, cantidad) -> {
                document.add(new Paragraph(estado + ": " + cantidad));
            });
        }

        document.close();
        return outputStream.toByteArray();
    }
}