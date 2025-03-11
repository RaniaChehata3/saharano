package atlantafx.sampler.view.patient;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.sampler.model.MedicalRecord;
import atlantafx.sampler.model.Patient;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

/**
 * View component for displaying a patient's medical records.
 */
public class PatientRecordView extends BorderPane {
    
    private final ObjectProperty<Patient> patient = new SimpleObjectProperty<>();
    private final TableView<MedicalRecord> recordsTable;
    private final ObjectProperty<MedicalRecord> selectedRecord = new SimpleObjectProperty<>();
    private final VBox detailsPane;
    private Consumer<MedicalRecord> onAddRecord;
    private Consumer<MedicalRecord> onEditRecord;
    private Consumer<MedicalRecord> onDeleteRecord;
    
    public PatientRecordView() {
        setPadding(new Insets(20));
        
        // Create patient info section (top)
        Card patientInfoCard = new Card();
        patientInfoCard.getStyleClass().add(Styles.ACCENT);
        
        Label patientNameLabel = new Label("No patient selected");
        patientNameLabel.getStyleClass().add(Styles.TITLE_3);
        
        Label patientDetailsLabel = new Label("");
        patientDetailsLabel.getStyleClass().add(Styles.TEXT_SUBTLE);
        
        // Button to add new record
        Button addRecordButton = new Button("Add Record", new FontIcon(Feather.PLUS));
        addRecordButton.getStyleClass().add(Styles.BUTTON_OUTLINED);
        addRecordButton.setOnAction(e -> {
            if (patient.get() != null && onAddRecord != null) {
                onAddRecord.accept(null);
            }
        });
        
        HBox patientInfoHeader = new HBox(10, patientNameLabel, addRecordButton);
        patientInfoHeader.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(patientNameLabel, Priority.ALWAYS);
        
        VBox patientInfoContent = new VBox(5, patientInfoHeader, patientDetailsLabel);
        patientInfoContent.setPadding(new Insets(10));
        patientInfoCard.setBody(patientInfoContent);
        
        setTop(patientInfoCard);
        BorderPane.setMargin(patientInfoCard, new Insets(0, 0, 10, 0));
        
        // Create records table (left)
        recordsTable = new TableView<>();
        recordsTable.getStyleClass().add(Styles.STRIPED);
        recordsTable.setPlaceholder(new Label("No records available"));
        
        recordsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedRecord.set(newVal);
            updateDetailView();
        });
        
        // Set up table columns
        TableColumn<MedicalRecord, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(data -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            return new SimpleStringProperty(data.getValue().getDateTime().toLocalDate().format(formatter));
        });
        dateColumn.setPrefWidth(120);
        
        TableColumn<MedicalRecord, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(data -> data.getValue().recordTypeProperty());
        typeColumn.setPrefWidth(100);
        
        TableColumn<MedicalRecord, String> diagnosisColumn = new TableColumn<>("Diagnosis");
        diagnosisColumn.setCellValueFactory(data -> data.getValue().diagnosisProperty());
        diagnosisColumn.setPrefWidth(200);
        
        TableColumn<MedicalRecord, String> doctorColumn = new TableColumn<>("Doctor");
        doctorColumn.setCellValueFactory(data -> data.getValue().doctorNameProperty());
        doctorColumn.setPrefWidth(150);
        
        recordsTable.getColumns().addAll(dateColumn, typeColumn, diagnosisColumn, doctorColumn);
        
        Card recordsCard = new Card();
        recordsCard.setBody(recordsTable);
        recordsCard.setPrefWidth(600);
        
        setLeft(recordsCard);
        BorderPane.setMargin(recordsCard, new Insets(0, 10, 0, 0));
        
        // Create record details pane (center)
        detailsPane = new VBox(10);
        detailsPane.setPadding(new Insets(10));
        detailsPane.setAlignment(Pos.TOP_LEFT);
        
        Label noSelectionLabel = new Label("Select a record to view details");
        noSelectionLabel.getStyleClass().add(Styles.TEXT_MUTED);
        detailsPane.getChildren().add(noSelectionLabel);
        
        Card detailsCard = new Card();
        detailsCard.setBody(detailsPane);
        
        setCenter(detailsCard);
        
        // Bind patient property
        patient.addListener((obs, oldVal, newVal) -> {
            updatePatientView();
        });
    }
    
    private void updatePatientView() {
        Patient p = patient.get();
        
        if (p != null) {
            // Update patient info card
            Label nameLabel = (Label) ((HBox) ((VBox) ((Card) getTop()).getBody()).getChildren().get(0)).getChildren().get(0);
            Label detailsLabel = (Label) ((VBox) ((Card) getTop()).getBody()).getChildren().get(1);
            
            nameLabel.setText(p.getFullName());
            detailsLabel.setText(String.format(
                "Age: %d | Gender: %s | Blood Type: %s | Phone: %s | Email: %s",
                p.getAge(), p.getGender(), p.getBloodType(), p.getPhone(), p.getEmail()
            ));
            
            // Update records table
            recordsTable.setItems(p.getMedicalRecords());
            
            // Clear selection
            recordsTable.getSelectionModel().clearSelection();
            selectedRecord.set(null);
            updateDetailView();
        } else {
            // No patient selected
            Label nameLabel = (Label) ((HBox) ((VBox) ((Card) getTop()).getBody()).getChildren().get(0)).getChildren().get(0);
            Label detailsLabel = (Label) ((VBox) ((Card) getTop()).getBody()).getChildren().get(1);
            
            nameLabel.setText("No patient selected");
            detailsLabel.setText("");
            
            // Clear records table
            recordsTable.setItems(null);
            
            // Clear selection
            selectedRecord.set(null);
            updateDetailView();
        }
    }
    
    private void updateDetailView() {
        detailsPane.getChildren().clear();
        
        MedicalRecord record = selectedRecord.get();
        
        if (record != null) {
            // Add title with date
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            
            String formattedDate = record.getDateTime().toLocalDate().format(dateFormatter);
            String formattedTime = record.getDateTime().toLocalTime().format(timeFormatter);
            
            Label titleLabel = new Label(record.getDiagnosis());
            titleLabel.getStyleClass().add(Styles.TITLE_3);
            
            Label subtitleLabel = new Label(formattedDate + " at " + formattedTime);
            subtitleLabel.getStyleClass().add(Styles.TEXT_SUBTLE);
            
            // Add doctor and record type
            Tile doctorTile = new Tile("Doctor", record.getDoctorName());
            doctorTile.setGraphic(new FontIcon(Feather.USER));
            
            Tile typeTile = new Tile("Record Type", record.getRecordType());
            typeTile.setGraphic(new FontIcon(Feather.FILE_TEXT));
            
            HBox infoTiles = new HBox(10, doctorTile, typeTile);
            
            // Add action buttons
            Button editButton = new Button("Edit", new FontIcon(Feather.EDIT));
            editButton.getStyleClass().add(Styles.BUTTON_OUTLINED);
            editButton.setOnAction(e -> {
                if (onEditRecord != null) {
                    onEditRecord.accept(record);
                }
            });
            
            Button deleteButton = new Button("Delete", new FontIcon(Feather.TRASH_2));
            deleteButton.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.DANGER);
            deleteButton.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Record");
                alert.setHeaderText("Delete Medical Record");
                alert.setContentText("Are you sure you want to delete this medical record?");
                
                alert.showAndWait().ifPresent(buttonType -> {
                    if (buttonType == ButtonType.OK && onDeleteRecord != null) {
                        onDeleteRecord.accept(record);
                    }
                });
            });
            
            HBox actionButtons = new HBox(10, editButton, deleteButton);
            actionButtons.setAlignment(Pos.CENTER_RIGHT);
            HBox.setHgrow(actionButtons, Priority.ALWAYS);
            
            HBox headerWithActions = new HBox(10);
            VBox headerLabels = new VBox(5, titleLabel, subtitleLabel);
            headerWithActions.getChildren().addAll(headerLabels, actionButtons);
            HBox.setHgrow(headerLabels, Priority.ALWAYS);
            
            // Add main record details
            TitledPane symptomsPane = createDetailSection("Symptoms", record.getSymptoms());
            TitledPane diagnosisPane = createDetailSection("Diagnosis", record.getDiagnosis());
            TitledPane treatmentPane = createDetailSection("Treatment", record.getTreatment());
            TitledPane prescriptionsPane = createDetailSection("Prescriptions", record.getPrescriptions());
            TitledPane notesPane = createDetailSection("Notes", record.getNotes());
            
            // Add all components to details pane
            detailsPane.getChildren().addAll(
                headerWithActions,
                infoTiles,
                new Separator(),
                symptomsPane,
                diagnosisPane,
                treatmentPane,
                prescriptionsPane,
                notesPane
            );
        } else {
            // No record selected
            Label noSelectionLabel = new Label("Select a record to view details");
            noSelectionLabel.getStyleClass().add(Styles.TEXT_MUTED);
            detailsPane.getChildren().add(noSelectionLabel);
        }
    }
    
    private TitledPane createDetailSection(String title, String content) {
        Label contentLabel = new Label(content);
        contentLabel.setWrapText(true);
        
        TitledPane pane = new TitledPane(title, contentLabel);
        pane.setExpanded(false);
        
        return pane;
    }
    
    public ObjectProperty<Patient> patientProperty() {
        return patient;
    }
    
    public Patient getPatient() {
        return patient.get();
    }
    
    public void setPatient(Patient patient) {
        this.patient.set(patient);
    }
    
    public ObjectProperty<MedicalRecord> selectedRecordProperty() {
        return selectedRecord;
    }
    
    public MedicalRecord getSelectedRecord() {
        return selectedRecord.get();
    }
    
    public void setOnAddRecord(Consumer<MedicalRecord> callback) {
        this.onAddRecord = callback;
    }
    
    public void setOnEditRecord(Consumer<MedicalRecord> callback) {
        this.onEditRecord = callback;
    }
    
    public void setOnDeleteRecord(Consumer<MedicalRecord> callback) {
        this.onDeleteRecord = callback;
    }
    
    // Helper class for string property in table columns
    private static class SimpleStringProperty extends javafx.beans.property.SimpleStringProperty {
        public SimpleStringProperty(String initialValue) {
            super(initialValue);
        }
    }
} 