package atlantafx.sampler.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

/**
 * Manages user accounts and authentication in the system.
 * Singleton pattern is used to ensure a single instance.
 */
public class UserManager {
    private static UserManager instance;
    
    private final ListProperty<User> users = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<User> currentUser = new SimpleObjectProperty<>();
    
    private UserManager() {
        // Initialize with some demo users
        initializeDemoUsers();
    }
    
    /**
     * Gets the singleton instance of UserManager.
     *
     * @return The UserManager instance
     */
    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    
    /**
     * Initializes the system with demo users for testing.
     */
    private void initializeDemoUsers() {
        // Admin user
        users.add(new User("admin", "admin123", "admin@example.com", "System", "Administrator", Role.ADMINISTRATOR));
        
        // Doctor users
        users.add(new User("doctor1", "doctor123", "doctor1@example.com", "John", "Smith", Role.DOCTOR));
        users.add(new User("doctor2", "doctor123", "doctor2@example.com", "Emily", "Johnson", Role.DOCTOR));
        
        // Patient users
        users.add(new User("patient1", "patient123", "patient1@example.com", "Michael", "Brown", Role.PATIENT));
        users.add(new User("patient2", "patient123", "patient2@example.com", "Sarah", "Davis", Role.PATIENT));
        
        // Laboratory users
        users.add(new User("lab1", "lab123", "lab1@example.com", "Central", "Laboratory", Role.LABORATORY));
        
        // Default a visitor account for demo
        users.add(new User("visitor", "visitor123", "visitor@example.com", "Guest", "User", Role.VISITOR));
    }
    
    /**
     * Attempts to authenticate a user with the given credentials.
     *
     * @param username The username
     * @param password The password
     * @return True if authentication successful, false otherwise
     */
    public boolean authenticate(String username, String password) {
        Optional<User> userOptional = findUserByUsername(username);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                // Set as current user if authentication successful
                setCurrentUser(user);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Creates a new user account.
     *
     * @param user The user to create
     * @return True if creation successful, false if username already exists
     */
    public boolean createUser(User user) {
        // Check if username already exists
        if (findUserByUsername(user.getUsername()).isPresent()) {
            return false;
        }
        
        users.add(user);
        return true;
    }
    
    /**
     * Updates an existing user's information.
     *
     * @param user The user with updated information
     * @return True if update successful, false if user not found
     */
    public boolean updateUser(User user) {
        Optional<User> existingUser = findUserByUsername(user.getUsername());
        
        if (existingUser.isPresent()) {
            // Remove old user and add updated one
            users.remove(existingUser.get());
            users.add(user);
            
            // Update current user if that's the one being updated
            if (getCurrentUser() != null && getCurrentUser().getUsername().equals(user.getUsername())) {
                setCurrentUser(user);
            }
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Deletes a user from the system.
     *
     * @param username The username of the user to delete
     * @return True if deletion successful, false if user not found
     */
    public boolean deleteUser(String username) {
        Optional<User> userOptional = findUserByUsername(username);
        
        if (userOptional.isPresent()) {
            // Logout if current user is being deleted
            if (getCurrentUser() != null && getCurrentUser().getUsername().equals(username)) {
                logout();
            }
            
            return users.remove(userOptional.get());
        }
        
        return false;
    }
    
    /**
     * Finds a user by their username.
     *
     * @param username The username to search for
     * @return An Optional containing the user if found, empty otherwise
     */
    public Optional<User> findUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }
    
    /**
     * Gets the current authenticated user.
     *
     * @return The current user
     */
    public User getCurrentUser() {
        return currentUser.get();
    }
    
    /**
     * Gets the current user property.
     *
     * @return The current user property
     */
    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }
    
    /**
     * Sets the current authenticated user.
     *
     * @param user The user to set as current
     */
    public void setCurrentUser(User user) {
        currentUser.set(user);
    }
    
    /**
     * Logs out the current user.
     */
    public void logout() {
        currentUser.set(null);
    }
    
    /**
     * Gets the list of all users.
     *
     * @return The observable list of users
     */
    public ObservableList<User> getUsers() {
        return users.get();
    }
    
    /**
     * Gets the users list property.
     *
     * @return The users list property
     */
    public ListProperty<User> usersProperty() {
        return users;
    }
    
    /**
     * Gets users filtered by role.
     *
     * @param role The role to filter by
     * @return List of users with the specified role
     */
    public ObservableList<User> getUsersByRole(Role role) {
        return users.filtered(user -> user.getRole() == role);
    }
} 