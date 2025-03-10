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
 * Dashboard for laboratories with test management functionality.
 * This is a placeholder implementation that would be expanded in a real application.
 */
public class LaboratoryDashboardView extends DashboardView {

    @Override
    protected void initializeDashboard(VBox contentArea) {
        // Add placeholder content
        Label placeholderLabel = new Label("Laboratory Dashboard");
        placeholderLabel.getStyleClass().add(Styles.TITLE_3);
        
        Message placeholderMessage = new Message(
            "Laboratory Dashboard",
            "This is a placeholder for the laboratory dashboard. In a real application, this would include " +
            "test management, result reporting, appointment scheduling, and other laboratory-specific features.",
            new FontIcon(Feather.ACTIVITY)
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