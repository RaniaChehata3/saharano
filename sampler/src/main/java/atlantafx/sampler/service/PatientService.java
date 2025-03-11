package atlantafx.sampler.service;

import atlantafx.sampler.model.MedicalRecord;
import atlantafx.sampler.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Service class for patient management and medical records.
 */
public class PatientService {
    private static PatientService instance;
    private final ObservableList<Patient> allPatients = FXCollections.observableArrayList();
    
    private PatientService() {
        initializeWithDummyData();
    }
    
    public static synchronized PatientService getInstance() {
        if (instance == null) {
            instance = new PatientService();
        }
        return instance;
    }
    
    /**
     * Get all patients in the system
     */
    public ObservableList<Patient> getAllPatients() {
        return allPatients;
    }
    
    /**
     * Get filtered patients based on a predicate
     */
    public FilteredList<Patient> getFilteredPatients(Predicate<Patient> predicate) {
        return new FilteredList<>(allPatients, predicate);
    }
    
    /**
     * Find a patient by ID
     */
    public Optional<Patient> findPatientById(String id) {
        return allPatients.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }
    
    /**
     * Add a new patient
     */
    public void addPatient(Patient patient) {
        allPatients.add(patient);
    }
    
    /**
     * Update an existing patient
     */
    public boolean updatePatient(Patient patient) {
        for (int i = 0; i < allPatients.size(); i++) {
            if (allPatients.get(i).getId().equals(patient.getId())) {
                allPatients.set(i, patient);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Delete a patient
     */
    public boolean deletePatient(String patientId) {
        return allPatients.removeIf(p -> p.getId().equals(patientId));
    }
    
    /**
     * Add a medical record to a patient
     */
    public boolean addMedicalRecord(String patientId, MedicalRecord record) {
        Optional<Patient> patient = findPatientById(patientId);
        if (patient.isPresent()) {
            patient.get().addMedicalRecord(record);
            return true;
        }
        return false;
    }
    
    /**
     * Initialize with some dummy data for development/testing
     */
    private void initializeWithDummyData() {
        // Create dummy patients
        Patient patient1 = new Patient(
            "John", "Smith", 
            LocalDate.of(1980, Month.APRIL, 15), 
            "Male", 
            "555-123-4567", 
            "john.smith@example.com", 
            "123 Main St, Anytown, USA", 
            "O+"
        );
        
        Patient patient2 = new Patient(
            "Emily", "Johnson", 
            LocalDate.of(1992, Month.JULY, 22), 
            "Female", 
            "555-987-6543", 
            "emily.johnson@example.com", 
            "456 Oak Ave, Somecity, USA", 
            "A-"
        );
        
        Patient patient3 = new Patient(
            "Michael", "Williams", 
            LocalDate.of(1975, Month.OCTOBER, 10), 
            "Male", 
            "555-456-7890", 
            "michael.williams@example.com", 
            "789 Pine St, Othertown, USA", 
            "B+"
        );
        
        Patient patient4 = new Patient(
            "Sarah", "Brown", 
            LocalDate.of(1988, Month.JANUARY, 5), 
            "Female", 
            "555-234-5678", 
            "sarah.brown@example.com", 
            "321 Elm St, Somewhere, USA", 
            "AB+"
        );
        
        // Add medical records to patients
        patient1.addMedicalRecord(new MedicalRecord(
            "Dr. Johnson", 
            "Hypertension", 
            "Headaches, dizziness, elevated blood pressure (150/95)", 
            "Prescribed lisinopril 10mg daily", 
            "Patient advised to reduce sodium intake and increase physical activity",
            "Lisinopril 10mg, 30 tablets, take 1 daily",
            "Check-up"
        ));
        
        patient1.addMedicalRecord(new MedicalRecord(
            "Dr. Smith", 
            "Upper respiratory infection", 
            "Sore throat, cough, nasal congestion, mild fever (100.2°F)", 
            "Prescribed amoxicillin 500mg TID for 10 days", 
            "Patient advised to rest and increase fluid intake",
            "Amoxicillin 500mg, 30 tablets, take 1 three times daily",
            "Illness"
        ));
        
        patient2.addMedicalRecord(new MedicalRecord(
            "Dr. Davis", 
            "Annual physical", 
            "No current complaints", 
            "Routine bloodwork ordered", 
            "All vitals within normal limits",
            "None",
            "Check-up"
        ));
        
        patient3.addMedicalRecord(new MedicalRecord(
            "Dr. Wilson", 
            "Type 2 Diabetes", 
            "Polyuria, polydipsia, fatigue, blurred vision", 
            "Prescribed metformin 500mg BID", 
            "Patient referred to nutritionist for dietary guidance",
            "Metformin 500mg, 60 tablets, take 1 twice daily",
            "Chronic"
        ));
        
        patient4.addMedicalRecord(new MedicalRecord(
            "Dr. Anderson", 
            "Migraine", 
            "Severe headache, photophobia, nausea", 
            "Prescribed sumatriptan 50mg as needed", 
            "Patient advised to identify and avoid triggers",
            "Sumatriptan 50mg, 9 tablets, take 1 as needed for migraine",
            "Illness"
        ));
        
        // Add patients to the list
        allPatients.addAll(patient1, patient2, patient3, patient4);
    }
} 