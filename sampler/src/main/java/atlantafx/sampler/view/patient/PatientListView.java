package atlantafx.sampler.view.patient;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import atlantafx.sampler.component.SearchBox;
import atlantafx.sampler.model.Patient;
import atlantafx.sampler.service.PatientService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.Consumer;

/**
 * View component for displaying and filtering a list of patients.
 */
public class PatientListView extends VBox {
    
    private final PatientService patientService;
    private final TableView<Patient> patientsTable;
    private final FilteredList<Patient> filteredPatients;
    private final ObjectProperty<Patient> selectedPatient = new SimpleObjectProperty<>();
    private final SearchBox searchBox;
    private Consumer<Patient> onPatientSelected;
    private Consumer<Patient> onAddPatient;
    private Consumer<Patient> onEditPatient;
    private Consumer<Patient> onDeletePatient;
    private Consumer<Patient> onExportPatient;
    
    public PatientListView() {
        this.patientService = PatientService.getInstance();
        this.filteredPatients = new FilteredList<>(patientService.getAllPatients(), p -> true);
        
        setSpacing(10);
        setPadding(new Insets(20));
        
        // Create header with title and add button
        Label titleLabel = new Label("Patients");
        titleLabel.getStyleClass().add(Styles.TITLE_3);
        
        Button addButton = new Button("Add Patient", new FontIcon(Feather.PLUS));
        addButton.getStyleClass().add(Styles.BUTTON_OUTLINED);
        addButton.setOnAction(e -> {
            if (onAddPatient != null) {
                onAddPatient.accept(null);
            }
        });
        
        HBox header = new HBox(10, titleLabel, addButton);
        header.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        
        // Create search and filter controls
        searchBox = new SearchBox();
        searchBox.setPromptText("Search patients...");
        searchBox.textProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters(newValue);
        });
        
        ComboBox<String> filterByComboBox = new ComboBox<>();
        filterByComboBox.getItems().addAll("All", "Name", "Address", "Blood Type");
        filterByComboBox.setValue("All");
        filterByComboBox.setPromptText("Filter by...");
        filterByComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters(searchBox.getText());
        });
        
        HBox searchAndFilterBox = new HBox(10, searchBox, filterByComboBox);
        searchAndFilterBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        
        // Create patients table
        patientsTable = new TableView<>();
        patientsTable.setItems(filteredPatients);
        patientsTable.getStyleClass().add(Styles.STRIPED);
        patientsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedPatient.set(newVal);
            if (newVal != null && onPatientSelected != null) {
                onPatientSelected.accept(newVal);
            }
        });
        
        // Set up table columns
        TableColumn<Patient, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> {
            Patient patient = data.getValue();
            return new SimpleStringProperty(patient.getFullName());
        });
        nameColumn.setPrefWidth(150);
        
        TableColumn<Patient, String> ageGenderColumn = new TableColumn<>("Age/Gender");
        ageGenderColumn.setCellValueFactory(data -> {
            Patient patient = data.getValue();
            return new SimpleStringProperty(patient.getAge() + " / " + patient.getGender());
        });
        ageGenderColumn.setPrefWidth(100);
        
        TableColumn<Patient, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(data -> data.getValue().phoneProperty());
        phoneColumn.setPrefWidth(120);
        
        TableColumn<Patient, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> data.getValue().emailProperty());
        emailColumn.setPrefWidth(200);
        
        TableColumn<Patient, String> bloodTypeColumn = new TableColumn<>("Blood Type");
        bloodTypeColumn.setCellValueFactory(data -> data.getValue().bloodTypeProperty());
        bloodTypeColumn.setPrefWidth(80);
        
        // Actions column
        TableColumn<Patient, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("", new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button("", new FontIcon(Feather.TRASH_2));
            private final Button exportButton = new Button("", new FontIcon(Feather.FILE_TEXT));
            
            {
                // Set up button styles
                editButton.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.ACCENT);
                deleteButton.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.DANGER);
                exportButton.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
                
                // Set tooltips
                editButton.setTooltip(new Tooltip("Edit Patient"));
                deleteButton.setTooltip(new Tooltip("Delete Patient"));
                exportButton.setTooltip(new Tooltip("Export to PDF"));
                
                // Set actions
                editButton.setOnAction(e -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    if (onEditPatient != null) {
                        onEditPatient.accept(patient);
                    }
                });
                
                deleteButton.setOnAction(e -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Patient");
                    alert.setHeaderText("Delete Patient");
                    alert.setContentText("Are you sure you want to delete " + patient.getFullName() + "?");
                    
                    alert.showAndWait().ifPresent(buttonType -> {
                        if (buttonType == ButtonType.OK && onDeletePatient != null) {
                            onDeletePatient.accept(patient);
                        }
                    });
                });
                
                exportButton.setOnAction(e -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    if (onExportPatient != null) {
                        onExportPatient.accept(patient);
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, editButton, deleteButton, exportButton));
                }
            }
        });
        actionsColumn.setPrefWidth(130);
        
        patientsTable.getColumns().addAll(
            nameColumn, ageGenderColumn, phoneColumn, emailColumn, bloodTypeColumn, actionsColumn
        );
        VBox.setVgrow(patientsTable, Priority.ALWAYS);
        
        // Create card to contain the table
        Card tableCard = new Card();
        VBox tableContainer = new VBox(10, searchAndFilterBox, patientsTable);
        tableContainer.setPadding(new Insets(10));
        tableCard.setBody(tableContainer);
        
        // Add all components to the main container
        getChildren().addAll(header, tableCard);
    }
    
    private void applyFilters(String searchText) {
        filteredPatients.setPredicate(patient -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            
            String lowerCaseSearch = searchText.toLowerCase();
            
            return patient.getFirstName().toLowerCase().contains(lowerCaseSearch) ||
                   patient.getLastName().toLowerCase().contains(lowerCaseSearch) ||
                   patient.getEmail().toLowerCase().contains(lowerCaseSearch) ||
                   patient.getPhone().toLowerCase().contains(lowerCaseSearch) ||
                   patient.getAddress().toLowerCase().contains(lowerCaseSearch) ||
                   patient.getBloodType().toLowerCase().contains(lowerCaseSearch);
        });
    }
    
    public void refreshData() {
        patientsTable.refresh();
    }
    
    public ObjectProperty<Patient> selectedPatientProperty() {
        return selectedPatient;
    }
    
    public Patient getSelectedPatient() {
        return selectedPatient.get();
    }
    
    public void selectPatient(Patient patient) {
        patientsTable.getSelectionModel().select(patient);
    }
    
    public void setOnPatientSelected(Consumer<Patient> callback) {
        this.onPatientSelected = callback;
    }
    
    public void setOnAddPatient(Consumer<Patient> callback) {
        this.onAddPatient = callback;
    }
    
    public void setOnEditPatient(Consumer<Patient> callback) {
        this.onEditPatient = callback;
    }
    
    public void setOnDeletePatient(Consumer<Patient> callback) {
        this.onDeletePatient = callback;
    }
    
    public void setOnExportPatient(Consumer<Patient> callback) {
        this.onExportPatient = callback;
    }
    
    // Class for string property in table columns
    private static class SimpleStringProperty extends javafx.beans.property.SimpleStringProperty {
        public SimpleStringProperty(String initialValue) {
            super(initialValue);
        }
    }
} 