package atlantafx.sampler.view.dashboard;

import atlantafx.base.theme.Styles;
import atlantafx.sampler.controller.AuthController;
import atlantafx.sampler.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.Consumer;

/**
 * Base dashboard view that contains common functionality for all role-specific dashboards.
 */
public abstract class DashboardView extends BorderPane {

    protected final AuthController authController;
    protected final User currentUser;
    protected Consumer<Void> onLogout;

    public DashboardView() {
        this.authController = AuthController.getInstance();
        this.currentUser = authController.getCurrentUser();
        
        // Set up header with user info and logout button
        setTop(createHeader());
        
        // Set up main content area
        VBox contentArea = new VBox(20);
        contentArea.setPadding(new Insets(20));
        setCenter(contentArea);
        
        // Initialize dashboard content
        initializeDashboard(contentArea);
    }

    /**
     * Creates the dashboard header with user info and logout button.
     *
     * @return The header node
     */
    protected HBox createHeader() {
        // User info label
        Label userInfoLabel = new Label("Welcome, " + currentUser.getFullName());
        userInfoLabel.getStyleClass().add(Styles.TITLE_4);
        
        // User role label
        Label roleLabel = new Label(currentUser.getRole().getDisplayName());
        roleLabel.getStyleClass().addAll(Styles.TEXT_CAPTION, Styles.TEXT_MUTED);
        
        VBox userInfo = new VBox(5, userInfoLabel, roleLabel);
        
        // Logout button
        Button logoutButton = new Button("Logout", new FontIcon(Feather.LOG_OUT));
        logoutButton.getStyleClass().add(Styles.BUTTON_OUTLINED);
        logoutButton.setOnAction(e -> {
            authController.logout();
            if (onLogout != null) {
                onLogout.accept(null);
            }
        });
        
        // Create header layout
        HBox header = new HBox(20, userInfo, logoutButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.getStyleClass().add(Styles.ACCENT);
        HBox.setHgrow(userInfo, Priority.ALWAYS);
        
        return header;
    }
    
    /**
     * Abstract method that each role-specific dashboard must implement to
     * initialize its unique content.
     *
     * @param contentArea The VBox container for dashboard content
     */
    protected abstract void initializeDashboard(VBox contentArea);
    
    /**
     * Sets the callback to be executed when the user logs out.
     *
     * @param callback The callback function
     */
    public void setOnLogout(Consumer<Void> callback) {
        this.onLogout = callback;
    }
} 