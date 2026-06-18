package com.smarthealth.healthcarebackend.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.smarthealth.healthcarebackend.entity.Patient;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {
    public byte[] generatePatientReport(Patient patient) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Patient Report"));
            document.add(new Paragraph("Name: " + patient.getUser().getFullName()));
            document.add(new Paragraph("Date of Birth: " + patient.getDateOfBirth()));
            document.add(new Paragraph("Blood Group: " + patient.getBloodGroup()));
            document.add(new Paragraph("Medical History: " + patient.getMedicalHistory()));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}