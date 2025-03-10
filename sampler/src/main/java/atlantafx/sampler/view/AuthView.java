package atlantafx.sampler.view;

import atlantafx.base.layout.DeckPane;
import atlantafx.sampler.controller.AuthController;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.function.Consumer;

/**
 * Container view that manages login and signup views.
 */
public class AuthView extends StackPane {

    private final DeckPane deckPane;
    private final LoginView loginView;
    private final SignupView signupView;
    private final AuthController authController;
    private Consumer<Void> onAuthSuccess;

    public AuthView() {
        this.authController = AuthController.getInstance();
        
        // Create views
        loginView = new LoginView();
        signupView = new SignupView();
        
        // Set up deck pane for swapping views
        deckPane = new DeckPane(loginView, signupView);
        deckPane.setTopNode(loginView); // Set login as the initial view
        
        // Set callbacks
        loginView.setOnLoginSuccess(v -> {
            if (onAuthSuccess != null) {
                onAuthSuccess.accept(null);
            }
        });
        
        loginView.setOnSignupRequest(v -> deckPane.setTopNode(signupView));
        
        signupView.setOnSignupSuccess(v -> {
            if (onAuthSuccess != null) {
                onAuthSuccess.accept(null);
            }
        });
        
        signupView.setOnLoginRequest(v -> deckPane.setTopNode(loginView));
        
        // Add to parent container
        getChildren().add(deckPane);
    }

    /**
     * Shows the login view.
     */
    public void showLogin() {
        deckPane.setTopNode(loginView);
    }

    /**
     * Shows the signup view.
     */
    public void showSignup() {
        deckPane.setTopNode(signupView);
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