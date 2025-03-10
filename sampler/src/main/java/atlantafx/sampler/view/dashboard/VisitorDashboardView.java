package atlantafx.sampler.view.dashboard;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Dashboard for visitors with limited functionality.
 * This is a placeholder implementation that would be expanded in a real application.
 */
public class VisitorDashboardView extends DashboardView {

    @Override
    protected void initializeDashboard(VBox contentArea) {
        // Add placeholder content
        Label placeholderLabel = new Label("Visitor Dashboard");
        placeholderLabel.getStyleClass().add(Styles.TITLE_3);
        
        Message placeholderMessage = new Message(
            "Visitor Dashboard",
            "This is a placeholder for the visitor dashboard. In a real application, this would include " +
            "public information, articles, and options to register as a patient.",
            new FontIcon(Feather.USERS)
        );
        placeholderMessage.getStyleClass().add(Styles.ACCENT);
        
        Card placeholderCard = new Card();
        placeholderCard.setBody(placeholderMessage);
        
        contentArea.getChildren().addAll(placeholderLabel, placeholderCard);
        contentArea.setPadding(new Insets(20));
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