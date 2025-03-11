package atlantafx.sampler.util;

import atlantafx.sampler.model.MedicalRecord;
import atlantafx.sampler.model.Patient;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility for exporting patient information to a well-formatted document.
 * This is a simplified implementation that creates a structured text file.
 */
public class PatientPdfExporter {
    
    // Formatters
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");

    /**
     * Export a patient's information to a PDF-like document.
     *
     * @param patient The patient to export
     * @param window The parent window for the file chooser
     * @return true if export was successful, false otherwise
     */
    public static boolean exportPatient(Patient patient, Window window) {
        // Configure file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Patient Report");
        fileChooser.setInitialFileName(patient.getFullName().replace(" ", "_") + ".pdf");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        
        // Show save dialog
        File file = fileChooser.showSaveDialog(window);
        if (file == null) {
            return false; // User canceled
        }
        
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            
            // Generate structured document content
            generateDocument(writer, patient);
            
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Successful");
            alert.setHeaderText("Patient Report Created");
            alert.setContentText("Patient information has been exported to:\n" + file.getAbsolutePath());
            alert.showAndWait();
            
            return true;
        } catch (IOException e) {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Export Failed");
            alert.setHeaderText("Could not create file");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
            
            return false;
        }
    }
    
    /**
     * Generate a structured document with patient information.
     *
     * @param writer The writer to output the document
     * @param patient The patient whose data to export
     */
    private static void generateDocument(PrintWriter writer, Patient patient) {
        // Add title
        writeCentered(writer, "PATIENT MEDICAL RECORD");
        writeCentered(writer, "=======================");
        writer.println();
        
        // Add patient information section
        writeHeading(writer, "PATIENT INFORMATION");
        
        // Write patient details
        writeField(writer, "Name", patient.getFullName());
        writeField(writer, "Date of Birth", patient.getDateOfBirth().format(DATE_FORMATTER) + 
                           " (Age: " + patient.getAge() + ")");
        writeField(writer, "Gender", patient.getGender());
        writeField(writer, "Blood Type", patient.getBloodType());
        writer.println();
        
        writeSubheading(writer, "Contact Information");
        writeField(writer, "Phone", patient.getPhone());
        writeField(writer, "Email", patient.getEmail());
        writeField(writer, "Address", patient.getAddress());
        writer.println();
        
        // Add medical records section
        writeHeading(writer, "MEDICAL HISTORY");
        
        List<MedicalRecord> records = patient.getMedicalRecords();
        
        if (records.isEmpty()) {
            writer.println("No medical records available.");
        } else {
            for (int i = 0; i < records.size(); i++) {
                MedicalRecord record = records.get(i);
                
                // Record header
                writeSubheading(writer, "Record #" + (i + 1) + " (" + record.getRecordType() + ")");
                
                // Record details
                writeField(writer, "Date", record.getDateTime().format(DATETIME_FORMATTER));
                writeField(writer, "Doctor", record.getDoctorName());
                writeField(writer, "Diagnosis", record.getDiagnosis());
                writeField(writer, "Symptoms", record.getSymptoms());
                writeField(writer, "Treatment", record.getTreatment());
                writeField(writer, "Prescriptions", record.getPrescriptions());
                writeField(writer, "Notes", record.getNotes());
                writer.println();
            }
        }
        
        // Add footer
        writer.println("----------------------------------------------------------------------");
        writer.println("This document is confidential and for medical use only.");
        writer.println("Generated on: " + java.time.LocalDateTime.now().format(DATETIME_FORMATTER));
    }
    
    /**
     * Write centered text to the document.
     */
    private static void writeCentered(PrintWriter writer, String text) {
        int padding = (80 - text.length()) / 2;
        if (padding > 0) {
            writer.print(" ".repeat(padding));
        }
        writer.println(text);
    }
    
    /**
     * Write a section heading to the document.
     */
    private static void writeHeading(PrintWriter writer, String heading) {
        writer.println(heading);
        writer.println("-".repeat(heading.length()));
        writer.println();
    }
    
    /**
     * Write a subheading to the document.
     */
    private static void writeSubheading(PrintWriter writer, String heading) {
        writer.println(heading);
        writer.println("~".repeat(heading.length()));
    }
    
    /**
     * Write a field with label and value to the document.
     */
    private static void writeField(PrintWriter writer, String label, String value) {
        writer.print(label + ": ");
        
        if (value == null || value.isEmpty()) {
            writer.println("N/A");
            return;
        }
        
        // Handle multiline values
        String[] lines = value.split("\n");
        writer.println(lines[0]);
        
        // If there are additional lines, indent them
        for (int i = 1; i < lines.length; i++) {
            writer.println("    " + lines[i]);
        }
    }
} 