package atlantafx.sampler.view;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.theme.Styles;
import atlantafx.sampler.controller.AuthController;
import atlantafx.sampler.model.Role;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;

import java.util.function.Consumer;

/**
 * View component for user registration.
 */
public class SignupView extends StackPane {

    private final AuthController authController;
    private final TextField usernameField;
    private final PasswordTextField passwordField;
    private final PasswordTextField confirmPasswordField;
    private final TextField emailField;
    private final TextField firstNameField;
    private final TextField lastNameField;
    private final ComboBox<Role> roleComboBox;
    private final Label errorLabel;
    private Consumer<Void> onSignupSuccess;
    private Consumer<Void> onLoginRequest;

    public SignupView() {
        this.authController = AuthController.getInstance();
        
        // Create form fields
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        
        passwordField = new PasswordTextField();
        passwordField.setPromptText("Password");
        
        confirmPasswordField = new PasswordTextField();
        confirmPasswordField.setPromptText("Confirm Password");
        
        emailField = new TextField();
        emailField.setPromptText("Email");
        
        firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        
        lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(Role.values());
        // Default to PATIENT for new signups
        roleComboBox.setValue(Role.PATIENT);
        roleComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Role role) {
                return role != null ? role.getDisplayName() : "";
            }

            @Override
            public Role fromString(String string) {
                return null; // Not needed for ComboBox
            }
        });
        
        // Error message label
        errorLabel = new Label();
        errorLabel.getStyleClass().add(Styles.DANGER);
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        
        // Signup button
        Button signupButton = new Button("Sign Up");
        signupButton.getStyleClass().add(Styles.ACCENT);
        signupButton.setPrefWidth(300);
        signupButton.setOnAction(e -> handleSignup());
        
        // Create form layout using GridPane for better alignment
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);
        
        // Add form fields to grid
        formGrid.add(new Label("Username:"), 0, 0);
        formGrid.add(usernameField, 1, 0);
        
        formGrid.add(new Label("Password:"), 0, 1);
        formGrid.add(passwordField, 1, 1);
        
        formGrid.add(new Label("Confirm Password:"), 0, 2);
        formGrid.add(confirmPasswordField, 1, 2);
        
        formGrid.add(new Label("Email:"), 0, 3);
        formGrid.add(emailField, 1, 3);
        
        formGrid.add(new Label("First Name:"), 0, 4);
        formGrid.add(firstNameField, 1, 4);
        
        formGrid.add(new Label("Last Name:"), 0, 5);
        formGrid.add(lastNameField, 1, 5);
        
        formGrid.add(new Label("Role:"), 0, 6);
        formGrid.add(roleComboBox, 1, 6);
        
        // Create main content box
        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(
                createHeader(),
                formGrid,
                errorLabel,
                signupButton,
                createFooter()
        );
        contentBox.setPadding(new Insets(20));
        
        // Create card
        Card signupCard = new Card();
        signupCard.setBody(contentBox);
        
        // Set up container
        getChildren().add(signupCard);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);
    }

    private TextFlow createHeader() {
        Text titleText = new Text("Create an Account");
        titleText.getStyleClass().add(Styles.TITLE_2);
        
        Text subtitleText = new Text("\nPlease fill in your information to register");
        subtitleText.getStyleClass().add(Styles.TEXT_SUBTLE);
        
        TextFlow header = new TextFlow(titleText, subtitleText);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));
        
        return header;
    }
    
    private HBox createFooter() {
        Text loginText = new Text("Already have an account? ");
        
        Hyperlink loginLink = new Hyperlink("Log in");
        loginLink.setOnAction(e -> {
            if (onLoginRequest != null) {
                onLoginRequest.accept(null);
            }
        });
        
        HBox footer = new HBox(loginText, loginLink);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20, 0, 0, 0));
        
        return footer;
    }
    
    private void handleSignup() {
        // Get form values
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        Role role = roleComboBox.getValue();
        
        // Validate inputs
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords don't match");
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            showError("Please enter a valid email address");
            return;
        }
        
        // Attempt registration
        boolean success = authController.register(username, password, email, firstName, lastName, role);
        
        if (success) {
            hideError();
            if (onSignupSuccess != null) {
                onSignupSuccess.accept(null);
            }
        } else {
            showError("Username already exists. Please choose a different one.");
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
     * Sets the callback to be executed when signup is successful.
     *
     * @param callback The callback function
     */
    public void setOnSignupSuccess(Consumer<Void> callback) {
        this.onSignupSuccess = callback;
    }
    
    /**
     * Sets the callback to be executed when the login link is clicked.
     *
     * @param callback The callback function
     */
    public void setOnLoginRequest(Consumer<Void> callback) {
        this.onLoginRequest = callback;
    }
} 