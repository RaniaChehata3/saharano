package atlantafx.sampler.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a patient in the medical system.
 */
public class Patient {
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dateOfBirth = new SimpleObjectProperty<>();
    private final StringProperty gender = new SimpleStringProperty();
    private final StringProperty phone = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty bloodType = new SimpleStringProperty();
    private final ObservableList<MedicalRecord> medicalRecords = FXCollections.observableArrayList();
    
    public Patient() {
        this.id.set(UUID.randomUUID().toString());
    }
    
    public Patient(String firstName, String lastName, LocalDate dateOfBirth, String gender, 
                  String phone, String email, String address, String bloodType) {
        this();
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.dateOfBirth.set(dateOfBirth);
        this.gender.set(gender);
        this.phone.set(phone);
        this.email.set(email);
        this.address.set(address);
        this.bloodType.set(bloodType);
    }
    
    // ID property
    public StringProperty idProperty() { return id; }
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }
    
    // First name property
    public StringProperty firstNameProperty() { return firstName; }
    public String getFirstName() { return firstName.get(); }
    public void setFirstName(String firstName) { this.firstName.set(firstName); }
    
    // Last name property
    public StringProperty lastNameProperty() { return lastName; }
    public String getLastName() { return lastName.get(); }
    public void setLastName(String lastName) { this.lastName.set(lastName); }
    
    // Full name convenience method
    public String getFullName() { return getFirstName() + " " + getLastName(); }
    
    // Date of birth property
    public ObjectProperty<LocalDate> dateOfBirthProperty() { return dateOfBirth; }
    public LocalDate getDateOfBirth() { return dateOfBirth.get(); }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth.set(dateOfBirth); }
    
    // Gender property
    public StringProperty genderProperty() { return gender; }
    public String getGender() { return gender.get(); }
    public void setGender(String gender) { this.gender.set(gender); }
    
    // Phone property
    public StringProperty phoneProperty() { return phone; }
    public String getPhone() { return phone.get(); }
    public void setPhone(String phone) { this.phone.set(phone); }
    
    // Email property
    public StringProperty emailProperty() { return email; }
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    
    // Address property
    public StringProperty addressProperty() { return address; }
    public String getAddress() { return address.get(); }
    public void setAddress(String address) { this.address.set(address); }
    
    // Blood type property
    public StringProperty bloodTypeProperty() { return bloodType; }
    public String getBloodType() { return bloodType.get(); }
    public void setBloodType(String bloodType) { this.bloodType.set(bloodType); }
    
    // Medical records list
    public ObservableList<MedicalRecord> getMedicalRecords() { return medicalRecords; }
    
    // Add a medical record
    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
    }
    
    // Calculate age from date of birth
    public int getAge() {
        if (dateOfBirth.get() == null) {
            return 0;
        }
        return LocalDate.now().getYear() - dateOfBirth.get().getYear();
    }
    
    @Override
    public String toString() {
        return getFullName();
    }
} 