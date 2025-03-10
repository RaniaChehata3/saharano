package atlantafx.sampler.view.dashboard;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Message;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.sampler.model.Role;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Dashboard for patients with features like appointment booking and symptom checking.
 */
public class PatientDashboardView extends DashboardView {

    @Override
    protected void initializeDashboard(VBox contentArea) {
        // Add quick stats
        contentArea.getChildren().add(createStatsSection());
        
        // Add upcoming appointments section
        contentArea.getChildren().add(createAppointmentsSection());
        
        // Add symptom checker section
        contentArea.getChildren().add(createSymptomCheckerSection());
    }

    private HBox createStatsSection() {
        // Upcoming appointments stats
        Tile appointmentsTile = new Tile();
        appointmentsTile.setTitle("Upcoming Appointments");
        appointmentsTile.setDescription("2");
        appointmentsTile.setGraphic(new FontIcon(Feather.CALENDAR));
        
        // Medical records stats
        Tile recordsTile = new Tile();
        recordsTile.setTitle("Medical Records");
        recordsTile.setDescription("12");
        recordsTile.setGraphic(new FontIcon(Feather.FILE_TEXT));
        
        // Last checkup stats
        Tile checkupTile = new Tile();
        checkupTile.setTitle("Last Checkup");
        checkupTile.setDescription("2 months ago");
        checkupTile.setGraphic(new FontIcon(Feather.ACTIVITY));
        
        // Prescriptions stats
        Tile prescriptionsTile = new Tile();
        prescriptionsTile.setTitle("Active Prescriptions");
        prescriptionsTile.setDescription("3");
        prescriptionsTile.setGraphic(new FontIcon(Feather.CLIPBOARD));
        
        // Create stats section layout
        HBox statsSection = new HBox(15, appointmentsTile, recordsTile, checkupTile, prescriptionsTile);
        statsSection.setAlignment(Pos.CENTER);
        HBox.setHgrow(appointmentsTile, Priority.ALWAYS);
        HBox.setHgrow(recordsTile, Priority.ALWAYS);
        HBox.setHgrow(checkupTile, Priority.ALWAYS);
        HBox.setHgrow(prescriptionsTile, Priority.ALWAYS);
        
        return statsSection;
    }
    
    private VBox createAppointmentsSection() {
        // Section title
        Label sectionTitle = new Label("Upcoming Appointments");
        sectionTitle.getStyleClass().add(Styles.TITLE_3);
        
        // Appointments table
        TableView<Appointment> appointmentsTable = new TableView<>();
        appointmentsTable.getStyleClass().add(Styles.STRIPED);
        appointmentsTable.setItems(createDummyAppointments());
        
        // Add columns
        TableColumn<Appointment, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        
        TableColumn<Appointment, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        
        TableColumn<Appointment, String> doctorColumn = new TableColumn<>("Doctor");
        doctorColumn.setCellValueFactory(cellData -> cellData.getValue().doctorProperty());
        
        TableColumn<Appointment, String> purposeColumn = new TableColumn<>("Purpose");
        purposeColumn.setCellValueFactory(cellData -> cellData.getValue().purposeProperty());
        
        TableColumn<Appointment, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        
        // Action column with cancel button
        TableColumn<Appointment, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button cancelBtn = new Button("Cancel");
            
            {
                cancelBtn.getStyleClass().add(Styles.DANGER);
                cancelBtn.setOnAction(event -> {
                    Appointment appointment = getTableView().getItems().get(getIndex());
                    showConfirmationDialog(
                        "Cancel Appointment",
                        "Are you sure you want to cancel your appointment with Dr. " + 
                            appointment.getDoctor() + " on " + appointment.getDate() + "?",
                        () -> {
                            getTableView().getItems().remove(getIndex());
                            showInfoMessage("Appointment successfully canceled");
                        }
                    );
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(cancelBtn);
                }
            }
        });
        
        appointmentsTable.getColumns().addAll(
            dateColumn, timeColumn, doctorColumn, purposeColumn, statusColumn, actionsColumn
        );
        
        // Create new appointment button
        Button newAppointmentBtn = new Button("Schedule New Appointment", new FontIcon(Feather.PLUS));
        newAppointmentBtn.getStyleClass().add(Styles.ACCENT);
        newAppointmentBtn.setOnAction(e -> showInfoMessage("Appointment scheduling form would be shown here"));
        
        // Create appointments card
        Card appointmentsCard = new Card();
        appointmentsCard.setBody(new VBox(10, appointmentsTable, newAppointmentBtn));
        VBox.setVgrow(appointmentsTable, Priority.ALWAYS);
        
        // Create section layout
        VBox section = new VBox(10, sectionTitle, appointmentsCard);
        section.setPadding(new Insets(20, 0, 0, 0));
        
        return section;
    }
    
    private VBox createSymptomCheckerSection() {
        // Section title
        Label sectionTitle = new Label("Symptom Checker");
        sectionTitle.getStyleClass().add(Styles.TITLE_3);
        
        // Symptom checker description
        Label description = new Label(
            "Select the part of your body where you feel discomfort, " +
            "and we'll help you identify possible conditions and recommend next steps."
        );
        description.setWrapText(true);
        
        // Body part selection ComboBox
        ComboBox<String> bodyPartComboBox = new ComboBox<>();
        bodyPartComboBox.getItems().addAll(
            "Head", "Chest", "Abdomen", "Back", "Arms", "Legs"
        );
        bodyPartComboBox.setPromptText("Select body part");
        
        // Symptom selection (would appear after body part selection)
        ListView<String> symptomsList = new ListView<>();
        symptomsList.setDisable(true);
        
        // Example symptoms for head
        bodyPartComboBox.setOnAction(e -> {
            if (bodyPartComboBox.getValue() != null) {
                symptomsList.setDisable(false);
                
                if (bodyPartComboBox.getValue().equals("Head")) {
                    symptomsList.getItems().setAll(
                        "Headache", "Dizziness", "Blurred vision", 
                        "Ear pain", "Sore throat", "Facial pain"
                    );
                } else {
                    symptomsList.getItems().setAll(
                        "Pain", "Numbness", "Swelling", "Redness", "Itching"
                    );
                }
            }
        });
        
        // Check symptoms button
        Button checkSymptomsBtn = new Button("Check Symptoms", new FontIcon(Feather.SEARCH));
        checkSymptomsBtn.getStyleClass().add(Styles.ACCENT);
        checkSymptomsBtn.setDisable(true);
        
        // Enable button when symptoms are selected
        symptomsList.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> checkSymptomsBtn.setDisable(newVal == null)
        );
        
        checkSymptomsBtn.setOnAction(e -> {
            String bodyPart = bodyPartComboBox.getValue();
            String symptom = symptomsList.getSelectionModel().getSelectedItem();
            
            if (bodyPart != null && symptom != null) {
                showSymptomResults(bodyPart, symptom);
            }
        });
        
        // Layout
        VBox symptomCheckerContent = new VBox(10,
            description,
            new Label("Step 1: Select body part"),
            bodyPartComboBox,
            new Label("Step 2: Select symptoms"),
            symptomsList,
            checkSymptomsBtn
        );
        symptomCheckerContent.setPadding(new Insets(10));
        
        // Create card
        Card symptomCheckerCard = new Card();
        symptomCheckerCard.setBody(symptomCheckerContent);
        
        // Create section layout
        VBox section = new VBox(10, sectionTitle, symptomCheckerCard);
        section.setPadding(new Insets(20, 0, 0, 0));
        
        return section;
    }
    
    private void showSymptomResults(String bodyPart, String symptom) {
        // In a real application, this would use a medical database or API
        // to provide actual medical information
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label diagnosisTitle = new Label("Possible Conditions");
        diagnosisTitle.getStyleClass().add(Styles.TITLE_3);
        
        VBox conditionsBox = new VBox(10);
        
        if (bodyPart.equals("Head") && symptom.equals("Headache")) {
            conditionsBox.getChildren().addAll(
                createConditionCard("Tension Headache", 
                    "Common headache with mild to moderate pain, often described as feeling like a tight band around the head.", 
                    "Rest, over-the-counter pain relievers, and stress management",
                    Styles.ACCENT),
                    
                createConditionCard("Migraine", 
                    "Severe, throbbing headache often accompanied by nausea, vomiting, and sensitivity to light and sound.", 
                    "Rest in a dark, quiet room, prescription medications, avoid triggers",
                    Styles.WARNING),
                    
                createConditionCard("Sinus Headache", 
                    "Pain and pressure around the sinuses, often with nasal congestion.", 
                    "Decongestants, nasal sprays, warm compress on the face",
                    Styles.ACCENT)
            );
        } else {
            conditionsBox.getChildren().add(
                createConditionCard("General Condition", 
                    "Based on your symptoms, we recommend consulting with a doctor for a proper diagnosis.", 
                    "Schedule an appointment with your primary care physician",
                    Styles.WARNING)
            );
        }
        
        Button findDoctorBtn = new Button("Find Doctors Near Me", new FontIcon(Feather.MAP_PIN));
        findDoctorBtn.getStyleClass().add(Styles.ACCENT);
        findDoctorBtn.setOnAction(e -> showInfoMessage("Doctor location map would be shown here"));
        
        content.getChildren().addAll(diagnosisTitle, conditionsBox, findDoctorBtn);
        
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Symptom Results");
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.getDialogPane().setPrefWidth(500);
        dialog.show();
    }
    
    private Card createConditionCard(String title, String description, String treatment, String styleClass) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add(Styles.TITLE_4);
        
        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        
        Label treatmentTitle = new Label("Recommended Treatment:");
        treatmentTitle.getStyleClass().add(Styles.TEXT_BOLD);
        
        Label treatmentLabel = new Label(treatment);
        treatmentLabel.setWrapText(true);
        
        VBox content = new VBox(5, titleLabel, descLabel, treatmentTitle, treatmentLabel);
        content.setPadding(new Insets(10));
        
        Card card = new Card();
        card.setBody(content);
        card.getStyleClass().add(styleClass);
        
        return card;
    }
    
    private javafx.collections.ObservableList<Appointment> createDummyAppointments() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        
        return javafx.collections.FXCollections.observableArrayList(
            new Appointment(
                today.plusDays(2).format(dateFormatter),
                LocalTime.of(10, 30).format(timeFormatter),
                "Johnson",
                "Annual Check-up",
                "Confirmed"
            ),
            new Appointment(
                today.plusDays(5).format(dateFormatter),
                LocalTime.of(14, 15).format(timeFormatter),
                "Smith",
                "Blood Test",
                "Pending"
            )
        );
    }
    
    private void showInfoMessage(String text) {
        Message message = new Message("Information", text, new FontIcon(Feather.INFO));
        message.getStyleClass().add(Styles.ACCENT);
        
        Dialog<Void> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(message);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.show();
    }
    
    private void showConfirmationDialog(String title, String message, Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        
        alert.showAndWait().ifPresent(type -> {
            if (type == buttonTypeYes) {
                onConfirm.run();
            }
        });
    }
    
    /**
     * Helper class to represent an appointment in the patient dashboard.
     */
    public static class Appointment {
        private final javafx.beans.property.StringProperty date = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.StringProperty time = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.StringProperty doctor = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.StringProperty purpose = new javafx.beans.property.SimpleStringProperty();
        private final javafx.beans.property.StringProperty status = new javafx.beans.property.SimpleStringProperty();
        
        public Appointment(String date, String time, String doctor, String purpose, String status) {
            setDate(date);
            setTime(time);
            setDoctor(doctor);
            setPurpose(purpose);
            setStatus(status);
        }
        
        public javafx.beans.property.StringProperty dateProperty() { return date; }
        public String getDate() { return date.get(); }
        public void setDate(String date) { this.date.set(date); }
        
        public javafx.beans.property.StringProperty timeProperty() { return time; }
        public String getTime() { return time.get(); }
        public void setTime(String time) { this.time.set(time); }
        
        public javafx.beans.property.StringProperty doctorProperty() { return doctor; }
        public String getDoctor() { return doctor.get(); }
        public void setDoctor(String doctor) { this.doctor.set(doctor); }
        
        public javafx.beans.property.StringProperty purposeProperty() { return purpose; }
        public String getPurpose() { return purpose.get(); }
        public void setPurpose(String purpose) { this.purpose.set(purpose); }
        
        public javafx.beans.property.StringProperty statusProperty() { return status; }
        public String getStatus() { return status.get(); }
        public void setStatus(String status) { this.status.set(status); }
    }
} 