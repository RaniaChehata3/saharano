package atlantafx.sampler.view;

import atlantafx.sampler.controller.AuthController;
import javafx.scene.layout.StackPane;

import java.util.function.Consumer;

/**
 * Container view that manages authentication UI.
 */
public class AuthView extends StackPane {

    private final LoginView loginView;
    private final AuthController authController;
    private Consumer<Void> onAuthSuccess;

    public AuthView() {
        this.authController = AuthController.getInstance();
        
        // Create login view
        loginView = new LoginView();
        
        // Set success callback
        loginView.setOnLoginSuccess(v -> {
            if (onAuthSuccess != null) {
                onAuthSuccess.accept(null);
            }
        });
        
        // Disable signup since we have direct access now
        loginView.setOnSignupRequest(null);
        
        // Add login view to the container
        getChildren().add(loginView);
    }

    /**
     * Shows the login view.
     */
    public void showLogin() {
        // Make sure login view is shown
        getChildren().clear();
        getChildren().add(loginView);
    }

    /**
     * Sets the callback to be executed when authentication is successful.
     *
     * @param callback The callback function
     */
    public void setOnAuthSuccess(Consumer<Void> callback) {
        this.onAuthSuccess = callback;
    }
} 