package atlantafx.sampler.view;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.theme.Styles;
import atlantafx.sampler.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.Consumer;

/**
 * View component for user login.
 */
public class LoginView extends StackPane {

    private final AuthController authController;
    private final TextField usernameField;
    private final PasswordTextField passwordField;
    private final Label errorLabel;
    private Consumer<Void> onLoginSuccess;
    private Consumer<Void> onSignupRequest;

    public LoginView() {
        this.authController = AuthController.getInstance();
        
        // Create form fields
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefWidth(300);
        
        passwordField = new PasswordTextField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(300);
        
        // Error message label
        errorLabel = new Label();
        errorLabel.getStyleClass().add(Styles.DANGER);
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        
        // Login button
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add(Styles.ACCENT);
        loginButton.setPrefWidth(300);
        loginButton.setOnAction(e -> handleLogin());
        
        // Create form layout
        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.CENTER);
        formBox.getChildren().addAll(
                createHeader(),
                new VBox(5, new Label("Username"), usernameField),
                new VBox(5, new Label("Password"), passwordField),
                errorLabel,
                loginButton,
                createFooter()
        );
        formBox.setPadding(new Insets(20));
        
        // Create card
        Card loginCard = new Card();
        loginCard.setBody(formBox);
        
        // Set up container
        getChildren().add(loginCard);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);
        
        // Demo credentials for convenience
        usernameField.setText("admin");
        passwordField.setText("admin123");
    }

    private TextFlow createHeader() {
        Text titleText = new Text("Login");
        titleText.getStyleClass().add(Styles.TITLE_2);
        
        Text subtitleText = new Text("\nPlease enter your credentials to continue");
        subtitleText.getStyleClass().add(Styles.TEXT_SUBTLE);
        
        TextFlow header = new TextFlow(titleText, subtitleText);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        return header;
    }
    
    private HBox createFooter() {
        Text signupText = new Text("Don't have an account? ");
        
        Hyperlink signupLink = new Hyperlink("Sign up");
        signupLink.setOnAction(e -> {
            if (onSignupRequest != null) {
                onSignupRequest.accept(null);
            }
        });
        
        HBox footer = new HBox(signupText, signupLink);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20, 0, 0, 0));
        
        return footer;
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }
        
        // Attempt login
        boolean success = authController.login(username, password);
        
        if (success) {
            hideError();
            if (onLoginSuccess != null) {
                onLoginSuccess.accept(null);
            }
        } else {
            showError("Invalid username or password");
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
    
    private void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
    
    /**
     * Sets the callback to be executed when login is successful.
     *
     * @param callback The callback function
     */
    public void setOnLoginSuccess(Consumer<Void> callback) {
        this.onLoginSuccess = callback;
    }
    
    /**
     * Sets the callback to be executed when the signup link is clicked.
     *
     * @param callback The callback function
     */
    public void setOnSignupRequest(Consumer<Void> callback) {
        this.onSignupRequest = callback;
    }
} 