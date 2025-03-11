package atlantafx.sampler.view.dashboard;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import atlantafx.sampler.component.NavBar;
import atlantafx.sampler.model.MedicalRecord;
import atlantafx.sampler.model.Patient;
import atlantafx.sampler.service.PatientService;
import atlantafx.sampler.util.PatientPdfExporter;
import atlantafx.sampler.view.patient.PatientEditDialog;
import atlantafx.sampler.view.patient.PatientListView;
import atlantafx.sampler.view.patient.PatientRecordView;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;

/**
 * Dashboard for doctors with patient management functionality.
 */
public class DoctorDashboardView extends DashboardView {

    private PatientService patientService;
    private StackPane contentPane;
    private PatientListView patientListView;
    private PatientRecordView patientRecordView;
    
    @Override
    protected void initializeDashboard(VBox contentArea) {
        // Initialize services and components
        this.patientService = PatientService.getInstance();
        this.contentPane = new StackPane();
        this.patientListView = new PatientListView();
        this.patientRecordView = new PatientRecordView();
        
        // Set up patient list view callbacks
        patientListView.setOnPatientSelected(this::handlePatientSelected);
        patientListView.setOnAddPatient(this::handleAddPatient);
        patientListView.setOnEditPatient(this::handleEditPatient);
        patientListView.setOnDeletePatient(this::handleDeletePatient);
        patientListView.setOnExportPatient(this::handleExportPatient);
        
        // Set up patient record view callbacks
        patientRecordView.setOnAddRecord(this::handleAddMedicalRecord);
        patientRecordView.setOnEditRecord(this::handleEditMedicalRecord);
        patientRecordView.setOnDeleteRecord(this::handleDeleteMedicalRecord);
        
        // Create navbar
        NavBar navBar = new NavBar();
        
        // Add buttons to navbar
        navBar.addButton("Patients", Feather.USERS, () -> showView("patients"));
        navBar.addButton("Appointments", Feather.CALENDAR, () -> showView("appointments"));
        navBar.addButton("Messages", Feather.MAIL, () -> showView("messages"));
        navBar.addButton("Reports", Feather.FILE_TEXT, () -> showView("reports"));
        
        // Add spacer and settings button
        navBar.addSpacer();
        navBar.addButton("Settings", Feather.SETTINGS, () -> showView("settings"));
        
        // Create placeholder views for other sections
        BorderPane appointmentsView = createPlaceholderView("Appointments", "Schedule and manage patient appointments");
        BorderPane messagesView = createPlaceholderView("Messages", "Communicate with patients and colleagues");
        BorderPane reportsView = createPlaceholderView("Reports", "Generate and view medical reports");
        BorderPane settingsView = createPlaceholderView("Settings", "Configure your dashboard preferences");
        
        // Add views to content pane (only the active one will be visible)
        contentPane.getChildren().addAll(
            patientListView,
            patientRecordView,
            appointmentsView,
            messagesView,
            reportsView,
            settingsView
        );
        
        // Show patients view by default
        showView("patients");
        
        // Add navbar and content pane to the main content area
        contentArea.getChildren().addAll(navBar, contentPane);
        contentArea.setSpacing(0);
        contentArea.setPadding(new Insets(0));
    }
    
    /**
     * Show the specified view in the content pane.
     */
    private void showView(String viewName) {
        // Hide all views
        contentPane.getChildren().forEach(node -> node.setVisible(false));
        
        // Show the selected view
        switch (viewName) {
            case "patients":
                patientListView.setVisible(true);
                break;
            case "patient_record":
                patientRecordView.setVisible(true);
                break;
            case "appointments":
                contentPane.getChildren().get(2).setVisible(true);
                break;
            case "messages":
                contentPane.getChildren().get(3).setVisible(true);
                break;
            case "reports":
                contentPane.getChildren().get(4).setVisible(true);
                break;
            case "settings":
                contentPane.getChildren().get(5).setVisible(true);
                break;
        }
    }
    
    /**
     * Create a placeholder view for sections not yet implemented.
     */
    private BorderPane createPlaceholderView(String title, String description) {
        BorderPane view = new BorderPane();
        view.setPadding(new Insets(20));
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add(Styles.TITLE_3);
        
        Message placeholderMessage = new Message(
            title,
            description + "\n\nThis feature is not yet implemented.",
            new FontIcon(Feather.INFO)
        );
        placeholderMessage.getStyleClass().add(Styles.ACCENT);
        
        Card placeholderCard = new Card();
        placeholderCard.setBody(placeholderMessage);
        
        VBox content = new VBox(10, titleLabel, placeholderCard);
        view.setCenter(content);
        
        return view;
    }
    
    /**
     * Handle patient selection.
     */
    private void handlePatientSelected(Patient patient) {
        if (patient != null) {
            patientRecordView.setPatient(patient);
            showView("patient_record");
        }
    }
    
    /**
     * Handle add patient action.
     */
    private void handleAddPatient(Patient patient) {
        PatientEditDialog dialog = new PatientEditDialog(null);
        Optional<Patient> result = dialog.showAndWait();
        
        result.ifPresent(newPatient -> {
            patientService.addPatient(newPatient);
            patientListView.refreshData();
            showInfoMessage("Patient added successfully");
        });
    }
    
    /**
     * Handle edit patient action.
     */
    private void handleEditPatient(Patient patient) {
        PatientEditDialog dialog = new PatientEditDialog(patient);
        Optional<Patient> result = dialog.showAndWait();
        
        result.ifPresent(updatedPatient -> {
            boolean success = patientService.updatePatient(updatedPatient);
            if (success) {
                patientListView.refreshData();
                
                // If currently viewing this patient's records, refresh the view
                if (patientRecordView.getPatient() != null && 
                    patientRecordView.getPatient().getId().equals(updatedPatient.getId())) {
                    patientRecordView.setPatient(updatedPatient);
                }
                
                showInfoMessage("Patient updated successfully");
            } else {
                showInfoMessage("Failed to update patient");
            }
        });
    }
    
    /**
     * Handle delete patient action.
     */
    private void handleDeletePatient(Patient patient) {
        boolean success = patientService.deletePatient(patient.getId());
        if (success) {
            patientListView.refreshData();
            
            // If currently viewing this patient's records, clear the view
            if (patientRecordView.getPatient() != null && 
                patientRecordView.getPatient().getId().equals(patient.getId())) {
                patientRecordView.setPatient(null);
                showView("patients");
            }
            
            showInfoMessage("Patient deleted successfully");
        } else {
            showInfoMessage("Failed to delete patient");
        }
    }
    
    /**
     * Handle export patient to PDF action.
     */
    private void handleExportPatient(Patient patient) {
        Scene scene = getScene();
        if (scene != null && scene.getWindow() != null) {
            boolean success = PatientPdfExporter.exportPatient(patient, scene.getWindow());
            if (!success) {
                showInfoMessage("PDF export was canceled or failed");
            }
        } else {
            showInfoMessage("Cannot export PDF: window not available");
        }
    }
    
    /**
     * Handle add medical record action.
     */
    private void handleAddMedicalRecord(MedicalRecord record) {
        showInfoMessage("Add medical record functionality not yet implemented");
    }
    
    /**
     * Handle edit medical record action.
     */
    private void handleEditMedicalRecord(MedicalRecord record) {
        showInfoMessage("Edit medical record functionality not yet implemented");
    }
    
    /**
     * Handle delete medical record action.
     */
    private void handleDeleteMedicalRecord(MedicalRecord record) {
        Patient patient = patientRecordView.getPatient();
        if (patient != null) {
            patient.getMedicalRecords().remove(record);
            patientRecordView.setPatient(patient); // Refresh the view
            showInfoMessage("Medical record deleted successfully");
        }
    }
    
    /**
     * Show an information message dialog.
     */
    private void showInfoMessage(String text) {
        Message message = new Message("Information", text, new FontIcon(Feather.INFO));
        message.getStyleClass().add(Styles.ACCENT);
        
        Dialog<Void> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(message);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.show();
    }
} 