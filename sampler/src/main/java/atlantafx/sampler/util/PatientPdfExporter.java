package atlantafx.sampler.util;

import atlantafx.sampler.model.MedicalRecord;
import atlantafx.sampler.model.Patient;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility for exporting patient information to PDF format.
 * Note: This is a simplified implementation that simulates PDF generation.
 * In a real application, you would use a PDF library like iText, Apache PDFBox, or OpenPDF.
 */
public class PatientPdfExporter {

    /**
     * Export a patient's information to a PDF file.
     *
     * @param patient The patient to export
     * @param window The parent window for the file chooser
     * @return true if export was successful, false otherwise
     */
    public static boolean exportPatient(Patient patient, Window window) {
        // Configure file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Patient PDF");
        fileChooser.setInitialFileName(patient.getFullName().replace(" ", "_") + ".pdf");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        
        // Show save dialog
        File file = fileChooser.showSaveDialog(window);
        if (file == null) {
            return false; // User canceled
        }
        
        try {
            // Generate PDF content (in a real app, use a PDF library)
            String pdfContent = generatePatientPdfContent(patient);
            
            // Write to file
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(pdfContent.getBytes());
            }
            
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Successful");
            alert.setHeaderText("Patient PDF Created");
            alert.setContentText("Patient information has been exported to:\n" + file.getAbsolutePath());
            alert.showAndWait();
            
            return true;
        } catch (IOException e) {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Export Failed");
            alert.setHeaderText("Could not create PDF file");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
            
            return false;
        }
    }
    
    /**
     * Generate the PDF content for a patient.
     * This is a simplified example that returns a text representation.
     * In a real application, you would use a PDF library to create an actual PDF.
     */
    private static String generatePatientPdfContent(Patient patient) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
        
        StringBuilder content = new StringBuilder();
        
        // Add PDF header
        content.append("PATIENT MEDICAL RECORD\n");
        content.append("====================\n\n");
        
        // Add patient information
        content.append("PATIENT INFORMATION\n");
        content.append("-------------------\n");
        content.append("Name: ").append(patient.getFullName()).append("\n");
        content.append("Date of Birth: ").append(patient.getDateOfBirth().format(dateFormatter))
               .append(" (Age: ").append(patient.getAge()).append(")\n");
        content.append("Gender: ").append(patient.getGender()).append("\n");
        content.append("Blood Type: ").append(patient.getBloodType()).append("\n");
        content.append("Contact Information:\n");
        content.append("  - Phone: ").append(patient.getPhone()).append("\n");
        content.append("  - Email: ").append(patient.getEmail()).append("\n");
        content.append("  - Address: ").append(patient.getAddress()).append("\n\n");
        
        // Add medical records
        List<MedicalRecord> records = patient.getMedicalRecords();
        
        content.append("MEDICAL HISTORY\n");
        content.append("--------------\n");
        
        if (records.isEmpty()) {
            content.append("No medical records available.\n\n");
        } else {
            for (int i = 0; i < records.size(); i++) {
                MedicalRecord record = records.get(i);
                
                content.append("Record #").append(i + 1).append(" (")
                       .append(record.getRecordType()).append(")\n");
                content.append("Date: ").append(record.getDateTime().format(dateTimeFormatter)).append("\n");
                content.append("Doctor: ").append(record.getDoctorName()).append("\n");
                content.append("Diagnosis: ").append(record.getDiagnosis()).append("\n");
                content.append("Symptoms: ").append(record.getSymptoms()).append("\n");
                content.append("Treatment: ").append(record.getTreatment()).append("\n");
                content.append("Prescriptions: ").append(record.getPrescriptions()).append("\n");
                content.append("Notes: ").append(record.getNotes()).append("\n\n");
            }
        }
        
        // Add footer
        content.append("--------------------------------------------------\n");
        content.append("This document is confidential and for medical use only.\n");
        content.append("Generated on: ").append(java.time.LocalDateTime.now()
                .format(dateTimeFormatter)).append("\n");
        
        return content.toString();
    }
} 