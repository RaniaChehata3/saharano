package atlantafx.sampler.view;

import atlantafx.sampler.controller.AuthController;
import atlantafx.sampler.view.dashboard.DashboardFactory;
import atlantafx.sampler.view.dashboard.DashboardView;
import javafx.scene.layout.StackPane;

/**
 * Main application view that manages authentication and dashboard views.
 * It switches between the auth view and the appropriate dashboard based on authentication state.
 */
public class MainAppView extends StackPane {

    private final AuthController authController;
    private final DashboardFactory dashboardFactory;
    private final AuthView authView;
    private DashboardView currentDashboard;

    public MainAppView() {
        this.authController = AuthController.getInstance();
        this.dashboardFactory = DashboardFactory.getInstance();
        
        // Create auth view
        authView = new AuthView();
        
        // Set callbacks
        authView.setOnAuthSuccess(v -> showDashboard());
        
        // Listen for authentication state changes
        authController.authenticatedProperty().addListener((obs, wasAuthenticated, isAuthenticated) -> {
            if (isAuthenticated) {
                showDashboard();
            } else {
                showAuth();
            }
        });
        
        // Show initial view based on authentication state
        if (authController.isAuthenticated()) {
            showDashboard();
        } else {
            showAuth();
        }
    }
    
    /**
     * Shows the authentication view.
     */
    private void showAuth() {
        // Remove current dashboard if it exists
        if (currentDashboard != null && getChildren().contains(currentDashboard)) {
            getChildren().remove(currentDashboard);
            currentDashboard = null;
        }
        
        // Add auth view if not already there
        if (!getChildren().contains(authView)) {
            getChildren().add(authView);
        }
    }
    
    /**
     * Shows the appropriate dashboard for the authenticated user.
     */
    private void showDashboard() {
        // Create dashboard for current user
        currentDashboard = dashboardFactory.createDashboard();
        
        // Set logout callback
        currentDashboard.setOnLogout(v -> authController.logout());
        
        // Remove auth view if it exists
        if (getChildren().contains(authView)) {
            getChildren().remove(authView);
        }
        
        // Remove any previous dashboards
        getChildren().removeIf(node -> node instanceof DashboardView && node != currentDashboard);
        
        // Add the new dashboard
        getChildren().add(currentDashboard);
    }
} 