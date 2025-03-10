package atlantafx.sampler.view.dashboard;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Message;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import atlantafx.sampler.model.Role;
import atlantafx.sampler.model.User;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.HashMap;
import java.util.Map;

/**
 * Dashboard for administrators that provides user management functionality.
 */
public class AdminDashboardView extends DashboardView {

    @Override
    protected void initializeDashboard(VBox contentArea) {
        // Add overall stats cards
        contentArea.getChildren().add(createStatsSection());
        
        // Add user management section
        contentArea.getChildren().add(createUserManagementSection());
        
        // Add quick actions section
        contentArea.getChildren().add(createQuickActionsSection());
    }

    private HBox createStatsSection() {
        // Get role counts
        Map<Role, Integer> roleCounts = new HashMap<>();
        for (Role role : Role.values()) {
            roleCounts.put(role, authController.getUserManager().getUsersByRole(role).size());
        }
        
        // Admin users stats
        Tile adminTile = new Tile();
        adminTile.setTitle("Administrators");
        adminTile.setDescription(roleCounts.get(Role.ADMINISTRATOR).toString());
        adminTile.setGraphic(new FontIcon(Feather.SHIELD));
        
        // Doctor users stats
        Tile doctorTile = new Tile();
        doctorTile.setTitle("Doctors");
        doctorTile.setDescription(roleCounts.get(Role.DOCTOR).toString());
        doctorTile.setGraphic(new FontIcon(Feather.USER));
        
        // Patient users stats
        Tile patientTile = new Tile();
        patientTile.setTitle("Patients");
        patientTile.setDescription(roleCounts.get(Role.PATIENT).toString());
        patientTile.setGraphic(new FontIcon(Feather.USERS));
        
        // Laboratory users stats
        Tile labTile = new Tile();
        labTile.setTitle("Laboratories");
        labTile.setDescription(roleCounts.get(Role.LABORATORY).toString());
        labTile.setGraphic(new FontIcon(Feather.ACTIVITY));
        
        // Create stats section layout
        HBox statsSection = new HBox(15, adminTile, doctorTile, patientTile, labTile);
        statsSection.setAlignment(Pos.CENTER);
        HBox.setHgrow(adminTile, Priority.ALWAYS);
        HBox.setHgrow(doctorTile, Priority.ALWAYS);
        HBox.setHgrow(patientTile, Priority.ALWAYS);
        HBox.setHgrow(labTile, Priority.ALWAYS);
        
        return statsSection;
    }
    
    private VBox createUserManagementSection() {
        // Section title
        Label sectionTitle = new Label("User Management");
        sectionTitle.getStyleClass().add(Styles.TITLE_3);
        
        // User table
        TableView<User> userTable = new TableView<>();
        userTable.getStyleClass().add(Styles.STRIPED);
        userTable.setItems(authController.getUserManager().getUsers());
        
        // Add columns
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        
        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> 
            cellData.getValue().firstNameProperty().concat(" ").concat(cellData.getValue().lastNameProperty()));
        
        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        
        TableColumn<User, Role> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        
        // Action column with edit/delete buttons
        TableColumn<User, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("", new FontIcon(Feather.EDIT));
            private final Button deleteBtn = new Button("", new FontIcon(Feather.TRASH));
            
            {
                editBtn.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.ACCENT);
                deleteBtn.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.DANGER);
                
                editBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    // In a real app, this would open an edit dialog
                    showInfoMessage("Edit user: " + user.getUsername());
                });
                
                deleteBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    if (confirmDelete(user)) {
                        authController.getUserManager().deleteUser(user.getUsername());
                        userTable.refresh();
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editBtn, deleteBtn);
                    buttons.setAlignment(Pos.CENTER);
                    setGraphic(buttons);
                }
            }
        });
        
        userTable.getColumns().addAll(usernameColumn, nameColumn, emailColumn, roleColumn, actionsColumn);
        
        // Create add new user button
        Button addUserButton = new Button("Add New User", new FontIcon(Feather.PLUS));
        addUserButton.getStyleClass().add(Styles.ACCENT);
        addUserButton.setOnAction(e -> {
            // In a real app, this would open a new user dialog
            showInfoMessage("Add new user functionality would be implemented here");
        });
        
        // Create card
        Card userManagementCard = new Card();
        userManagementCard.setBody(new VBox(10, userTable, addUserButton));
        VBox.setVgrow(userTable, Priority.ALWAYS);
        
        // Create section layout
        VBox section = new VBox(10, sectionTitle, userManagementCard);
        section.setPadding(new Insets(20, 0, 0, 0));
        
        return section;
    }
    
    private VBox createQuickActionsSection() {
        // Section title
        Label sectionTitle = new Label("Quick Actions");
        sectionTitle.getStyleClass().add(Styles.TITLE_3);
        
        // Quick action buttons
        Button approveModerationBtn = new Button("Approve Pending Content", new FontIcon(Feather.CHECK_CIRCLE));
        approveModerationBtn.getStyleClass().add(Styles.SUCCESS);
        approveModerationBtn.setOnAction(e -> 
            showInfoMessage("Content moderation functionality would be implemented here"));
        
        Button systemSettingsBtn = new Button("System Settings", new FontIcon(Feather.SETTINGS));
        systemSettingsBtn.getStyleClass().add(Styles.WARNING);
        systemSettingsBtn.setOnAction(e -> 
            showInfoMessage("System settings functionality would be implemented here"));
        
        Button backupDataBtn = new Button("Backup Data", new FontIcon(Feather.DATABASE));
        backupDataBtn.getStyleClass().add(Styles.ACCENT);
        backupDataBtn.setOnAction(e -> 
            showInfoMessage("Data backup functionality would be implemented here"));
        
        // Create actions layout
        HBox actions = new HBox(10, approveModerationBtn, systemSettingsBtn, backupDataBtn);
        actions.setPadding(new Insets(10));
        actions.setAlignment(Pos.CENTER_LEFT);
        
        // Create card
        Card actionsCard = new Card();
        actionsCard.setBody(actions);
        
        // Create section layout
        VBox section = new VBox(10, sectionTitle, actionsCard);
        section.setPadding(new Insets(20, 0, 0, 0));
        
        return section;
    }
    
    private boolean confirmDelete(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete User");
        alert.setContentText("Are you sure you want to delete user '" + user.getUsername() + "'?");
        
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        
        return alert.showAndWait().orElse(buttonTypeNo) == buttonTypeYes;
    }
    
    private void showInfoMessage(String text) {
        Message message = new Message("Information", text, new FontIcon(Feather.INFO));
        message.getStyleClass().add(Styles.ACCENT);
        
        Dialog<Void> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(message);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.show();
    }
} 