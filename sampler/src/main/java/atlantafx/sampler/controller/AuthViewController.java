package atlantafx.sampler.controller;

import atlantafx.sampler.util.FXMLLoaderHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Controller for the authentication view.
 * This controller is responsible for managing the login and signup views.
 */
public class AuthViewController implements Initializable {

    @FXML
    private StackPane root;
    
    private Parent loginView;
    private LoginController loginController;
    
    private Consumer<Void> onAuthSuccess;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Load the login view
            FXMLLoader loginLoader = FXMLLoaderHelper.createLoader("login.fxml");
            loginView = loginLoader.load();
            loginController = loginLoader.getController();
            
            // Set callbacks for login view
            loginController.setOnLoginSuccess(this::handleLoginSuccess);
            
            // Just show login as signup isn't needed with quick access buttons
            showLogin();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load login view", e);
        }
    }
    
    /**
     * Shows the login view.
     */
    public void showLogin() {
        // Clear the current content and add login view
        root.getChildren().clear();
        root.getChildren().add(loginView);
    }
    
    /**
     * Handles successful login.
     */
    private void handleLoginSuccess(Void v) {
        if (onAuthSuccess != null) {
            onAuthSuccess.accept(null);
        }
    }
    
    /**
     * Sets the callback for authentication success.
     */
    public void setOnAuthSuccess(Consumer<Void> callback) {
        this.onAuthSuccess = callback;
    }
} 