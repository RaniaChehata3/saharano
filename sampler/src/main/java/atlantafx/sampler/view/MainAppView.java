package atlantafx.sampler.view;

import atlantafx.base.layout.DeckPane;
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
    private final DeckPane deckPane;
    private final AuthView authView;
    private DashboardView currentDashboard;

    public MainAppView() {
        this.authController = AuthController.getInstance();
        this.dashboardFactory = DashboardFactory.getInstance();
        
        // Create views
        authView = new AuthView();
        
        // Set up deck pane for swapping views
        deckPane = new DeckPane(authView);
        
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
        
        // Add to parent container
        getChildren().add(deckPane);
        
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
        if (currentDashboard != null) {
            deckPane.getChildren().remove(currentDashboard);
            currentDashboard = null;
        }
        
        // Show auth view
        deckPane.setTopNode(authView);
    }
    
    /**
     * Shows the appropriate dashboard for the authenticated user.
     */
    private void showDashboard() {
        // Create dashboard for current user
        currentDashboard = dashboardFactory.createDashboard();
        
        // Set logout callback
        currentDashboard.setOnLogout(v -> authController.logout());
        
        // Add to deck pane if not already there
        if (!deckPane.getChildren().contains(currentDashboard)) {
            deckPane.getChildren().add(currentDashboard);
        }
        
        // Show dashboard
        deckPane.setTopNode(currentDashboard);
    }
} 