package atlantafx.sampler.controller;

import atlantafx.sampler.util.FXMLLoaderHelper;
import atlantafx.sampler.view.dashboard.DashboardFactory;
import atlantafx.sampler.view.dashboard.DashboardView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the main application view.
 * This controller manages authentication and dashboard views.
 */
public class MainAppController implements Initializable {

    @FXML
    private StackPane root;
    
    private AuthController authController;
    private DashboardFactory dashboardFactory;
    
    private Parent authView;
    private DashboardView currentDashboard;
    private AuthViewController authViewController;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        authController = AuthController.getInstance();
        dashboardFactory = DashboardFactory.getInstance();
        
        try {
            // Load auth view
            FXMLLoader authLoader = FXMLLoaderHelper.createLoader("auth.fxml");
            authView = authLoader.load();
            authViewController = authLoader.getController();
            
            // Set callback for auth success
            authViewController.setOnAuthSuccess(v -> {
                if (authController.isAuthenticated()) {
                    showDashboard();
                }
            });
            
            // Listen for authentication state changes
            authController.authenticatedProperty().addListener((obs, wasAuthenticated, isAuthenticated) -> {
                if (isAuthenticated) {
                    showDashboard();
                } else {
                    showAuth();
                }
            });
            
            // Start with auth view
            showAuth();
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to load authentication view", e);
        }
    }
    
    /**
     * Shows the authentication view.
     */
    private void showAuth() {
        // Remove current dashboard if it exists
        if (currentDashboard != null) {
            root.getChildren().clear();
            currentDashboard = null;
        }
        
        // Show auth view
        root.getChildren().clear();
        root.getChildren().add(authView);
    }
    
    /**
     * Shows the appropriate dashboard for the authenticated user.
     */
    private void showDashboard() {
        // Get the appropriate dashboard for the user's role
        currentDashboard = dashboardFactory.createDashboardForCurrentUser();
        
        if (currentDashboard != null) {
            // Show the dashboard
            root.getChildren().clear();
            root.getChildren().add(currentDashboard);
        }
    }
} 