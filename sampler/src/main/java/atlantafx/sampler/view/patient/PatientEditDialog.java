package atlantafx.sampler.view.patient;

import atlantafx.base.theme.Styles;
import atlantafx.sampler.model.Patient;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Dialog for adding and editing patient information.
 */
public class PatientEditDialog extends Dialog<Patient> {

    private final TextField firstNameField = new TextField();
    private final TextField lastNameField = new TextField();
    private final DatePicker dateOfBirthPicker = new DatePicker();
    private final ComboBox<String> genderComboBox = new ComboBox<>();
    private final TextField phoneField = new TextField();
    private final TextField emailField = new TextField();
    private final TextArea addressField = new TextArea();
    private final ComboBox<String> bloodTypeComboBox = new ComboBox<>();
    
    private final Patient patient;
    private final boolean isNewPatient;

    /**
     * Create a new PatientEditDialog.
     *
     * @param patient The patient to edit, or null to create a new patient
     */
    public PatientEditDialog(Patient patient) {
        this.isNewPatient = patient == null;
        this.patient = isNewPatient ? new Patient() : patient;
        
        // Configure dialog
        setTitle(isNewPatient ? "Add New Patient" : "Edit Patient");
        setHeaderText(isNewPatient ? "Enter patient information" : "Edit patient information");
        
        // Set up form fields
        firstNameField.setPromptText("First Name");
        lastNameField.setPromptText("Last Name");
        dateOfBirthPicker.setPromptText("Date of Birth");
        genderComboBox.setPromptText("Gender");
        phoneField.setPromptText("Phone Number");
        emailField.setPromptText("Email Address");
        addressField.setPromptText("Address");
        bloodTypeComboBox.setPromptText("Blood Type");
        
        // Configure gender dropdown
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        
        // Configure blood type dropdown
        bloodTypeComboBox.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        
        // Configure date picker
        dateOfBirthPicker.setConverter(new StringConverter<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            
            @Override
            public String toString(LocalDate date) {
                return date != null ? formatter.format(date) : "";
            }
            
            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() 
                       ? LocalDate.parse(string, formatter) 
                       : null;
            }
        });
        
        // Set initial values if editing an existing patient
        if (!isNewPatient) {
            firstNameField.setText(patient.getFirstName());
            lastNameField.setText(patient.getLastName());
            dateOfBirthPicker.setValue(patient.getDateOfBirth());
            genderComboBox.setValue(patient.getGender());
            phoneField.setText(patient.getPhone());
            emailField.setText(patient.getEmail());
            addressField.setText(patient.getAddress());
            bloodTypeComboBox.setValue(patient.getBloodType());
        }
        
        // Create form layout
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(20));
        
        // Add form fields to grid
        int row = 0;
        formGrid.add(new Label("First Name:"), 0, row);
        formGrid.add(firstNameField, 1, row++);
        
        formGrid.add(new Label("Last Name:"), 0, row);
        formGrid.add(lastNameField, 1, row++);
        
        formGrid.add(new Label("Date of Birth:"), 0, row);
        formGrid.add(dateOfBirthPicker, 1, row++);
        
        formGrid.add(new Label("Gender:"), 0, row);
        formGrid.add(genderComboBox, 1, row++);
        
        formGrid.add(new Label("Phone:"), 0, row);
        formGrid.add(phoneField, 1, row++);
        
        formGrid.add(new Label("Email:"), 0, row);
        formGrid.add(emailField, 1, row++);
        
        formGrid.add(new Label("Blood Type:"), 0, row);
        formGrid.add(bloodTypeComboBox, 1, row++);
        
        formGrid.add(new Label("Address:"), 0, row);
        formGrid.add(addressField, 1, row++);
        
        // Make address field expand to fill available space
        addressField.setPrefRowCount(3);
        GridPane.setHgrow(addressField, Priority.ALWAYS);
        GridPane.setVgrow(addressField, Priority.ALWAYS);
        
        // Set up button types
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Set the dialog content
        getDialogPane().setContent(formGrid);
        
        // Convert result to patient object when OK is clicked
        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // Validate inputs before accepting
                if (!validateInputs()) {
                    return null;
                }
                
                // Update patient object with form values
                patient.setFirstName(firstNameField.getText());
                patient.setLastName(lastNameField.getText());
                patient.setDateOfBirth(dateOfBirthPicker.getValue());
                patient.setGender(genderComboBox.getValue());
                patient.setPhone(phoneField.getText());
                patient.setEmail(emailField.getText());
                patient.setAddress(addressField.getText());
                patient.setBloodType(bloodTypeComboBox.getValue());
                
                return patient;
            }
            return null;
        });
        
        // Add some styling
        getDialogPane().getStyleClass().add(Styles.ELEVATED_2);
        getDialogPane().setPrefWidth(500);
    }
    
    /**
     * Validate the user inputs.
     *
     * @return true if inputs are valid, false otherwise
     */
    private boolean validateInputs() {
        StringBuilder errorMessage = new StringBuilder();
        
        if (firstNameField.getText().trim().isEmpty()) {
            errorMessage.append("First name is required.\n");
        }
        
        if (lastNameField.getText().trim().isEmpty()) {
            errorMessage.append("Last name is required.\n");
        }
        
        if (dateOfBirthPicker.getValue() == null) {
            errorMessage.append("Date of birth is required.\n");
        } else if (dateOfBirthPicker.getValue().isAfter(LocalDate.now())) {
            errorMessage.append("Date of birth cannot be in the future.\n");
        }
        
        if (genderComboBox.getValue() == null || genderComboBox.getValue().trim().isEmpty()) {
            errorMessage.append("Gender is required.\n");
        }
        
        if (phoneField.getText().trim().isEmpty()) {
            errorMessage.append("Phone number is required.\n");
        }
        
        if (emailField.getText().trim().isEmpty()) {
            errorMessage.append("Email is required.\n");
        } else if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
            errorMessage.append("Email format is invalid.\n");
        }
        
        if (bloodTypeComboBox.getValue() == null || bloodTypeComboBox.getValue().trim().isEmpty()) {
            errorMessage.append("Blood type is required.\n");
        }
        
        if (addressField.getText().trim().isEmpty()) {
            errorMessage.append("Address is required.\n");
        }
        
        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
} 