package atlantafx.sampler.view;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.theme.Styles;
import atlantafx.sampler.controller.AuthController;
import atlantafx.sampler.model.Role;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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
        
        // Create quick access buttons
        VBox quickAccessBox = createQuickAccessButtons();
        
        // Create form layout
        VBox formBox = new VBox(10);
        formBox.setAlignment(Pos.CENTER);
        formBox.getChildren().addAll(
                createHeader(),
                new VBox(5, new Label("Username"), usernameField),
                new VBox(5, new Label("Password"), passwordField),
                errorLabel,
                loginButton,
                createFooter(),
                new Label(""),  // Spacer
                new Label("Quick Access"),
                quickAccessBox
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
    
    /**
     * Creates buttons for quick access to default accounts.
     */
    private VBox createQuickAccessButtons() {
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);
        buttonGrid.setAlignment(Pos.CENTER);
        
        // Admin quick access button
        Button adminButton = createRoleButton(Role.ADMINISTRATOR, "admin", "admin123");
        buttonGrid.add(adminButton, 0, 0);
        
        // Doctor quick access button
        Button doctorButton = createRoleButton(Role.DOCTOR, "doctor1", "doctor123");
        buttonGrid.add(doctorButton, 1, 0);
        
        // Patient quick access button
        Button patientButton = createRoleButton(Role.PATIENT, "patient1", "patient123");
        buttonGrid.add(patientButton, 0, 1);
        
        // Laboratory quick access button
        Button labButton = createRoleButton(Role.LABORATORY, "lab1", "lab123");
        buttonGrid.add(labButton, 1, 1);
        
        // Visitor quick access button
        Button visitorButton = createRoleButton(Role.VISITOR, "visitor", "visitor123");
        buttonGrid.add(visitorButton, 0, 2, 2, 1); // Span 2 columns
        GridPane.setHgrow(visitorButton, Priority.ALWAYS);
        
        // Container for the grid
        VBox container = new VBox(10);
        container.getChildren().add(buttonGrid);
        container.setAlignment(Pos.CENTER);
        
        Text noteText = new Text("Click any role button to login instantly");
        noteText.getStyleClass().add(Styles.TEXT_CAPTION);
        container.getChildren().add(noteText);
        
        return container;
    }
    
    /**
     * Creates a button for quick access to a role with default credentials.
     *
     * @param role The role for quick access
     * @param username The default username for the role
     * @param password The default password for the role
     * @return The created button
     */
    private Button createRoleButton(Role role, String username, String password) {
        Button button = new Button(role.getDisplayName());
        button.setPrefWidth(130);
        
        if (role == Role.ADMINISTRATOR) {
            button.getStyleClass().add(Styles.DANGER);
        } else if (role == Role.DOCTOR) {
            button.getStyleClass().add(Styles.SUCCESS);
        } else if (role == Role.PATIENT) {
            button.getStyleClass().add(Styles.WARNING);
        } else if (role == Role.LABORATORY) {
            button.getStyleClass().add(Styles.FLAT);
        } else if (role == Role.VISITOR) {
            button.getStyleClass().add(Styles.ACCENT);
        }
        
        button.setOnAction(e -> {
            System.out.println("Quick access button clicked for role: " + role);
            System.out.println("Using credentials - Username: " + username + ", Password: " + password);
            
            // Fill in credentials
            usernameField.setText(username);
            passwordField.setText(password);
            
            // Directly attempt login - don't simulate click as it might cause issues
            handleLoginWithCredentials(username, password);
        });
        
        return button;
    }
    
    /**
     * Handles login attempt with provided credentials.
     * 
     * @param username The username to use
     * @param password The password to use
     */
    private void handleLoginWithCredentials(String username, String password) {
        System.out.println("Attempting login with - Username: " + username + ", Password: " + password);
        
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            showError("Username is required");
            System.out.println("Error shown: Username is required");
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            showError("Password is required");
            System.out.println("Error shown: Password is required");
            return;
        }
        
        // Attempt to login
        boolean success = authController.login(username, password);
        System.out.println("Login success: " + success);
        
        if (success) {
            hideError();
            
            // Trigger callback if present
            if (onLoginSuccess != null) {
                onLoginSuccess.accept(null);
            }
        } else {
            showError("Invalid username or password");
            System.out.println("Error shown: Invalid username or password");
        }
    }
    
    /**
     * Handles login button click.
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        handleLoginWithCredentials(username, password);
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
        System.out.println("Error shown: " + message);
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