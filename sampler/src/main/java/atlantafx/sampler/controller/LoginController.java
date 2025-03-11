package atlantafx.sampler.controller;

import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.theme.Styles;
import atlantafx.sampler.model.Role;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Controller for the login view.
 * This controller handles user authentication via the login form.
 */
public class LoginController implements Initializable {
    
    @FXML private TextField usernameField;
    @FXML private PasswordTextField passwordField;
    @FXML private Label errorLabel;
    @FXML private GridPane buttonGrid;
    @FXML private Hyperlink signupLink;
    
    private Consumer<Void> onLoginSuccess;
    private Consumer<Void> onSignupRequest;
    private AuthController authController;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        authController = AuthController.getInstance();
        
        // Set default values for demo purposes
        usernameField.setText("admin");
        passwordField.setText("admin123");
        
        // Create quick access buttons
        createQuickAccessButtons();
    }
    
    /**
     * Handles the login button click event.
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        handleLoginWithCredentials(username, password);
    }
    
    /**
     * Handles signup link click event.
     */
    @FXML
    private void handleSignupRequest() {
        if (onSignupRequest != null) {
            onSignupRequest.accept(null);
        }
    }
    
    /**
     * Creates buttons for quick access to default accounts.
     */
    private void createQuickAccessButtons() {
        // Admin quick access button
        Button adminButton = createRoleButton(Role.ADMINISTRATOR, "admin", "admin123");
        buttonGrid.add(adminButton, 0, 0);
        
        // Doctor quick access button
        Button doctorButton = createRoleButton(Role.DOCTOR, "doctor1", "doctor123");
        buttonGrid.add(doctorButton, 1, 0);
        
        // Patient quick access button
        Button patientButton = createRoleButton(Role.PATIENT, "patient1", "patient123");
        buttonGrid.add(patientButton, 0, 1);
        
        // Laboratory quick access button
        Button labButton = createRoleButton(Role.LABORATORY, "lab1", "lab123");
        buttonGrid.add(labButton, 1, 1);
        
        // Visitor quick access button
        Button visitorButton = createRoleButton(Role.VISITOR, "visitor", "visitor123");
        buttonGrid.add(visitorButton, 0, 2, 2, 1); // Span 2 columns
        GridPane.setHgrow(visitorButton, Priority.ALWAYS);
    }
    
    /**
     * Creates a button for quick access to a role with default credentials.
     */
    private Button createRoleButton(Role role, String username, String password) {
        Button button = new Button(role.getDisplayName());
        button.setPrefWidth(130);
        
        if (role == Role.ADMINISTRATOR) {
            button.getStyleClass().add(Styles.DANGER);
        } else if (role == Role.DOCTOR) {
            button.getStyleClass().add(Styles.SUCCESS);
        } else if (role == Role.PATIENT) {
            button.getStyleClass().add(Styles.WARNING);
        } else if (role == Role.LABORATORY) {
            button.getStyleClass().add(Styles.FLAT);
        } else if (role == Role.VISITOR) {
            button.getStyleClass().add(Styles.ACCENT);
        }
        
        button.setOnAction(e -> {
            System.out.println("Quick access button clicked for role: " + role);
            System.out.println("Using credentials - Username: " + username + ", Password: " + password);
            
            // Fill in credentials
            usernameField.setText(username);
            passwordField.setText(password);
            
            // Directly attempt login
            handleLoginWithCredentials(username, password);
        });
        
        return button;
    }
    
    /**
     * Handles login attempt with provided credentials.
     */
    private void handleLoginWithCredentials(String username, String password) {
        System.out.println("Attempting login with - Username: " + username + ", Password: " + password);
        
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            showError("Username is required");
            System.out.println("Error shown: Username is required");
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            showError("Password is required");
            System.out.println("Error shown: Password is required");
            return;
        }
        
        // Attempt to login
        boolean success = authController.login(username, password);
        System.out.println("Login success: " + success);
        
        if (success) {
            hideError();
            
            // Trigger callback if present
            if (onLoginSuccess != null) {
                onLoginSuccess.accept(null);
            }
        } else {
            showError("Invalid username or password");
            System.out.println("Error shown: Invalid username or password");
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
        System.out.println("Error shown: " + message);
    }
    
    private void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
    
    /**
     * Sets the callback to be executed when login is successful.
     */
    public void setOnLoginSuccess(Consumer<Void> callback) {
        this.onLoginSuccess = callback;
    }
    
    /**
     * Sets the callback to be executed when the signup link is clicked.
     */
    public void setOnSignupRequest(Consumer<Void> callback) {
        this.onSignupRequest = callback;
    }
} 