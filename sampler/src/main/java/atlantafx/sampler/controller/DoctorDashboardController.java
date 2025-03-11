package atlantafx.sampler.controller;

import atlantafx.sampler.service.PatientService;
import atlantafx.sampler.util.FXMLLoaderHelper;
import atlantafx.sampler.view.patient.PatientListView;
import atlantafx.sampler.view.patient.PatientRecordView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the doctor dashboard view.
 */
public class DoctorDashboardController implements Initializable {

    @FXML private VBox patientListContainer;
    @FXML private StackPane patientRecordContainer;
    @FXML private VBox noSelectionMessage;
    @FXML private Label statusLabel;
    
    private PatientService patientService;
    private PatientListView patientListView;
    private PatientRecordView patientRecordView;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        patientService = PatientService.getInstance();
        
        // Initialize views
        initializePatientListView();
        initializePatientRecordView();
        
        // Show no selection message initially
        showView("noSelection");
    }
    
    /**
     * Initialize the patient list view.
     */
    private void initializePatientListView() {
        patientListView = new PatientListView();
        
        // Add to container
        patientListContainer.getChildren().add(patientListView);
        
        // Set up callbacks
        patientListView.setOnPatientSelected(patient -> {
            if (patient != null) {
                patientRecordView.setPatient(patient);
                showView("patientRecords");
                updateStatus("Viewing records for " + patient.getFullName());
            } else {
                showView("noSelection");
                updateStatus("Ready");
            }
        });
        
        patientListView.setOnEditPatient(patient -> {
            handleEditPatient(patient);
        });
        
        patientListView.setOnDeletePatient(patient -> {
            handleDeletePatient(patient);
        });
        
        patientListView.setOnExportPatient(patient -> {
            handleExportPatient(patient);
        });
    }
    
    /**
     * Initialize the patient record view.
     */
    private void initializePatientRecordView() {
        patientRecordView = new PatientRecordView();
        patientRecordContainer.getChildren().add(patientRecordView);
    }
    
    /**
     * Shows a specific view in the patient record container.
     *
     * @param viewName The name of the view to show ("patientRecords" or "noSelection")
     */
    private void showView(String viewName) {
        if (viewName.equals("patientRecords")) {
            patientRecordView.setVisible(true);
            noSelectionMessage.setVisible(false);
        } else {
            patientRecordView.setVisible(false);
            noSelectionMessage.setVisible(true);
        }
    }
    
    /**
     * Updates the status bar message.
     *
     * @param message The message to display
     */
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }
    
    /**
     * Handle editing a patient.
     */
    private void handleEditPatient(atlantafx.sampler.model.Patient patient) {
        // Implementation would show a dialog to edit patient
        updateStatus("Editing patient: " + patient.getFullName());
    }
    
    /**
     * Handle deleting a patient.
     */
    private void handleDeletePatient(atlantafx.sampler.model.Patient patient) {
        // Implementation would confirm deletion and remove patient
        updateStatus("Deleted patient: " + patient.getFullName());
    }
    
    /**
     * Handle exporting a patient's information.
     */
    private void handleExportPatient(atlantafx.sampler.model.Patient patient) {
        // Implementation would export patient data
        updateStatus("Exporting information for: " + patient.getFullName());
    }
} 