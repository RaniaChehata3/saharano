package atlantafx.sampler.controller;

import atlantafx.sampler.model.Role;
import atlantafx.sampler.model.User;
import atlantafx.sampler.model.UserManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Controller responsible for handling user authentication operations.
 */
public class AuthController {
    private static AuthController instance;
    private final UserManager userManager;
    
    // Property to track login state for UI binding
    private final BooleanProperty authenticated = new SimpleBooleanProperty(false);
    
    private AuthController() {
        userManager = UserManager.getInstance();
        // Update authenticated property when current user changes
        userManager.currentUserProperty().addListener((obs, oldUser, newUser) -> 
            authenticated.set(newUser != null));
    }
    
    /**
     * Gets the singleton instance of the AuthController.
     *
     * @return The AuthController instance
     */
    public static synchronized AuthController getInstance() {
        if (instance == null) {
            instance = new AuthController();
        }
        return instance;
    }
    
    /**
     * Attempts to log in a user with the provided credentials.
     *
     * @param username The username
     * @param password The password
     * @return True if login successful, false otherwise
     */
    public boolean login(String username, String password) {
        boolean success = userManager.authenticate(username, password);
        return success;
    }
    
    /**
     * Registers a new user account.
     *
     * @param username The username
     * @param password The password
     * @param email The email address
     * @param firstName The first name
     * @param lastName The last name
     * @param role The user role
     * @return True if registration successful, false if username already exists
     */
    public boolean register(String username, String password, String email, 
                            String firstName, String lastName, Role role) {
        User newUser = new User(username, password, email, firstName, lastName, role);
        return userManager.createUser(newUser);
    }
    
    /**
     * Updates the information of an existing user.
     *
     * @param user The user with updated information
     * @return True if update successful, false if user not found
     */
    public boolean updateUser(User user) {
        return userManager.updateUser(user);
    }
    
    /**
     * Logs out the current user.
     */
    public void logout() {
        userManager.logout();
    }
    
    /**
     * Gets the currently authenticated user.
     *
     * @return The current user or null if not authenticated
     */
    public User getCurrentUser() {
        return userManager.getCurrentUser();
    }
    
    /**
     * Checks if a user is currently authenticated.
     *
     * @return True if a user is logged in, false otherwise
     */
    public boolean isAuthenticated() {
        return userManager.getCurrentUser() != null;
    }
    
    /**
     * Gets the authenticated property for binding.
     *
     * @return The authenticated property
     */
    public BooleanProperty authenticatedProperty() {
        return authenticated;
    }
    
    /**
     * Validates if the current user has the specified role.
     *
     * @param role The role to check
     * @return True if the current user has the role, false otherwise
     */
    public boolean hasRole(Role role) {
        User currentUser = userManager.getCurrentUser();
        return currentUser != null && currentUser.getRole() == role;
    }
    
    /**
     * Gets the UserManager instance.
     *
     * @return The UserManager
     */
    public UserManager getUserManager() {
        return userManager;
    }
} 