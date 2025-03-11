package atlantafx.sampler.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a medical record entry for a patient.
 */
public class MedicalRecord {
    private final StringProperty id = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> dateTime = new SimpleObjectProperty<>();
    private final StringProperty doctorName = new SimpleStringProperty();
    private final StringProperty diagnosis = new SimpleStringProperty();
    private final StringProperty symptoms = new SimpleStringProperty();
    private final StringProperty treatment = new SimpleStringProperty();
    private final StringProperty notes = new SimpleStringProperty();
    private final StringProperty prescriptions = new SimpleStringProperty();
    private final StringProperty recordType = new SimpleStringProperty();
    
    public MedicalRecord() {
        this.id.set(UUID.randomUUID().toString());
        this.dateTime.set(LocalDateTime.now());
    }
    
    public MedicalRecord(String doctorName, String diagnosis, String symptoms, String treatment, 
                        String notes, String prescriptions, String recordType) {
        this();
        this.doctorName.set(doctorName);
        this.diagnosis.set(diagnosis);
        this.symptoms.set(symptoms);
        this.treatment.set(treatment);
        this.notes.set(notes);
        this.prescriptions.set(prescriptions);
        this.recordType.set(recordType);
    }
    
    // ID property
    public StringProperty idProperty() { return id; }
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }
    
    // Date and time property
    public ObjectProperty<LocalDateTime> dateTimeProperty() { return dateTime; }
    public LocalDateTime getDateTime() { return dateTime.get(); }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime.set(dateTime); }
    
    // Doctor name property
    public StringProperty doctorNameProperty() { return doctorName; }
    public String getDoctorName() { return doctorName.get(); }
    public void setDoctorName(String doctorName) { this.doctorName.set(doctorName); }
    
    // Diagnosis property
    public StringProperty diagnosisProperty() { return diagnosis; }
    public String getDiagnosis() { return diagnosis.get(); }
    public void setDiagnosis(String diagnosis) { this.diagnosis.set(diagnosis); }
    
    // Symptoms property
    public StringProperty symptomsProperty() { return symptoms; }
    public String getSymptoms() { return symptoms.get(); }
    public void setSymptoms(String symptoms) { this.symptoms.set(symptoms); }
    
    // Treatment property
    public StringProperty treatmentProperty() { return treatment; }
    public String getTreatment() { return treatment.get(); }
    public void setTreatment(String treatment) { this.treatment.set(treatment); }
    
    // Notes property
    public StringProperty notesProperty() { return notes; }
    public String getNotes() { return notes.get(); }
    public void setNotes(String notes) { this.notes.set(notes); }
    
    // Prescriptions property
    public StringProperty prescriptionsProperty() { return prescriptions; }
    public String getPrescriptions() { return prescriptions.get(); }
    public void setPrescriptions(String prescriptions) { this.prescriptions.set(prescriptions); }
    
    // Record type property
    public StringProperty recordTypeProperty() { return recordType; }
    public String getRecordType() { return recordType.get(); }
    public void setRecordType(String recordType) { this.recordType.set(recordType); }
    
    @Override
    public String toString() {
        return recordType.get() + " - " + diagnosis.get() + " (" + dateTime.get().toLocalDate() + ")";
    }
} 