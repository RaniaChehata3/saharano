package atlantafx.sampler.view;

import atlantafx.sampler.controller.AuthController;
import javafx.scene.layout.StackPane;

import java.util.function.Consumer;

/**
 * Container view that manages login and signup views.
 */
public class AuthView extends StackPane {

    private final LoginView loginView;
    private final SignupView signupView;
    private final AuthController authController;
    private Consumer<Void> onAuthSuccess;

    public AuthView() {
        this.authController = AuthController.getInstance();
        
        // Create views
        loginView = new LoginView();
        signupView = new SignupView();
        
        // Set callbacks
        loginView.setOnLoginSuccess(v -> {
            if (onAuthSuccess != null) {
                onAuthSuccess.accept(null);
            }
        });
        
        loginView.setOnSignupRequest(v -> showSignup());
        
        signupView.setOnSignupSuccess(v -> {
            if (onAuthSuccess != null) {
                onAuthSuccess.accept(null);
            }
        });
        
        signupView.setOnLoginRequest(v -> showLogin());
        
        // Start with login view
        getChildren().add(loginView);
    }

    /**
     * Shows the login view.
     */
    public void showLogin() {
        // First remove all views
        getChildren().clear();
        
        // Add login view
        getChildren().add(loginView);
    }

    /**
     * Shows the signup view.
     */
    public void showSignup() {
        // First remove all views
        getChildren().clear();
        
        // Add signup view
        getChildren().add(signupView);
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