package atlantafx.sampler.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the navigation bar component.
 */
public class NavbarController implements Initializable {

    @FXML private Label userLabel;
    @FXML private Label roleLabel;
    @FXML private Button logoutButton;
    
    private AuthController authController;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        authController = AuthController.getInstance();
        
        // Update UI with current user info
        updateUserInfo();
        
        // Listen for authentication state changes
        authController.authenticatedProperty().addListener((obs, wasAuthenticated, isAuthenticated) -> {
            updateUserInfo();
        });
    }
    
    /**
     * Updates the UI with current user information.
     */
    private void updateUserInfo() {
        if (authController.isAuthenticated()) {
            var user = authController.getCurrentUser();
            userLabel.setText(user.getFullName());
            roleLabel.setText(user.getRole().getDisplayName());
        } else {
            userLabel.setText("");
            roleLabel.setText("");
        }
    }
    
    /**
     * Handles the logout button click.
     */
    @FXML
    private void handleLogout() {
        authController.logout();
    }
} 