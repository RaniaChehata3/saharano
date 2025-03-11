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
        System.out.println("UserManager initialized with " + users.size() + " users");
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
        User admin = new User("admin", "admin123", "admin@example.com", "System", "Administrator", Role.ADMINISTRATOR);
        users.add(admin);
        
        // Doctor users
        User doctor1 = new User("doctor1", "doctor123", "doctor1@example.com", "John", "Smith", Role.DOCTOR);
        users.add(doctor1);
        
        User doctor2 = new User("doctor2", "doctor123", "doctor2@example.com", "Emily", "Johnson", Role.DOCTOR);
        users.add(doctor2);
        
        // Patient users
        User patient1 = new User("patient1", "patient123", "patient1@example.com", "Michael", "Brown", Role.PATIENT);
        users.add(patient1);
        
        User patient2 = new User("patient2", "patient123", "patient2@example.com", "Sarah", "Davis", Role.PATIENT);
        users.add(patient2);
        
        // Laboratory users
        User lab = new User("lab1", "lab123", "lab1@example.com", "Central", "Laboratory", Role.LABORATORY);
        users.add(lab);
        
        // Default a visitor account for demo
        User visitor = new User("visitor", "visitor123", "visitor@example.com", "Guest", "User", Role.VISITOR);
        users.add(visitor);
        
        // Log the users that were created
        for (User user : users) {
            System.out.println("Created user: " + user.getUsername() + " with password: " + user.getPassword() + " and role: " + user.getRole());
        }
    }
    
    /**
     * Attempts to authenticate a user with the given credentials.
     *
     * @param username The username
     * @param password The password
     * @return True if authentication successful, false otherwise
     */
    public boolean authenticate(String username, String password) {
        System.out.println("Attempting to authenticate user: " + username);
        
        Optional<User> userOptional = findUserByUsername(username);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("Found user: " + user.getUsername() + " with password: " + user.getPassword());
            
            if (user.getPassword().equals(password)) {
                // Set as current user if authentication successful
                setCurrentUser(user);
                System.out.println("Authentication successful for user: " + username);
                return true;
            } else {
                System.out.println("Password mismatch for user: " + username);
            }
        } else {
            System.out.println("User not found: " + username);
        }
        
        return false;
    }
    
    /**
     * Finds a user by username.
     *
     * @param username The username to search for
     * @return An Optional containing the user if found, empty otherwise
     */
    public Optional<User> findUserByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        
        return users.stream()
                .filter(u -> username.equals(u.getUsername()))
                .findFirst();
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
        
        // Add user to the list
        users.add(user);
        
        // Set as current user
        setCurrentUser(user);
        
        return true;
    }
    
    /**
     * Updates an existing user's information.
     *
     * @param user The user with updated information
     * @return True if update successful, false if user not found
     */
    public boolean updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                
                // Update current user if it's the same user
                if (currentUser.get() != null && 
                    currentUser.get().getUsername().equals(user.getUsername())) {
                    setCurrentUser(user);
                }
                
                return true;
            }
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
     * Logs out the current user.
     */
    public void logout() {
        setCurrentUser(null);
    }
    
    /**
     * Gets the currently authenticated user.
     *
     * @return The current user or null if not authenticated
     */
    public User getCurrentUser() {
        return currentUser.get();
    }
    
    /**
     * Gets the current user property for binding.
     *
     * @return The current user property
     */
    public ObjectProperty<User> currentUserProperty() {
        return currentUser;
    }
    
    /**
     * Sets the current user.
     *
     * @param user The user to set as current
     */
    public void setCurrentUser(User user) {
        currentUser.set(user);
    }
    
    /**
     * Gets the list of users.
     *
     * @return The observable list of users
     */
    public ObservableList<User> getUsers() {
        return users.get();
    }
    
    /**
     * Gets the users property for binding.
     *
     * @return The users property
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