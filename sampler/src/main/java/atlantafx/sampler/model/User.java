package atlantafx.sampler.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a user in the system with authentication details and role information.
 */
public class User {
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final ObjectProperty<Role> role = new SimpleObjectProperty<>();
    
    public User() {
        // Default constructor
    }
    
    public User(String username, String password, String email, String firstName, String lastName, Role role) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setRole(role);
    }
    
    // Username property
    public StringProperty usernameProperty() {
        return username;
    }
    
    public String getUsername() {
        return username.get();
    }
    
    public void setUsername(String username) {
        this.username.set(username);
    }
    
    // Password property
    public StringProperty passwordProperty() {
        return password;
    }
    
    public String getPassword() {
        return password.get();
    }
    
    public void setPassword(String password) {
        this.password.set(password);
    }
    
    // Email property
    public StringProperty emailProperty() {
        return email;
    }
    
    public String getEmail() {
        return email.get();
    }
    
    public void setEmail(String email) {
        this.email.set(email);
    }
    
    // First name property
    public StringProperty firstNameProperty() {
        return firstName;
    }
    
    public String getFirstName() {
        return firstName.get();
    }
    
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }
    
    // Last name property
    public StringProperty lastNameProperty() {
        return lastName;
    }
    
    public String getLastName() {
        return lastName.get();
    }
    
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }
    
    // Role property
    public ObjectProperty<Role> roleProperty() {
        return role;
    }
    
    public Role getRole() {
        return role.get();
    }
    
    public void setRole(Role role) {
        this.role.set(role);
    }
    
    /**
     * Gets the full name of the user.
     *
     * @return The full name (first name + last name)
     */
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
    
    @Override
    public String toString() {
        return "User{" +
                "username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", role=" + getRole() +
                '}';
    }
} 