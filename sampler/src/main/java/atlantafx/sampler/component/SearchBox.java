package atlantafx.sampler.component;

import atlantafx.base.theme.Styles;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * A custom search box component with a search icon.
 */
public class SearchBox extends HBox {
    
    private final TextField textField;
    
    public SearchBox() {
        super(5);
        
        // Create search icon
        FontIcon searchIcon = new FontIcon(Feather.SEARCH);
        
        // Create text field
        textField = new TextField();
        textField.setPromptText("Search...");
        
        // Set up layout
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(searchIcon, textField);
        
        // Set styles
        getStyleClass().add("search-box");
        textField.getStyleClass().add(Styles.FLAT);
        
        // Make text field fill available space
        textField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textField, javafx.scene.layout.Priority.ALWAYS);
    }
    
    /**
     * Get the text property of the text field.
     */
    public javafx.beans.property.StringProperty textProperty() {
        return textField.textProperty();
    }
    
    /**
     * Get the text of the text field.
     */
    public String getText() {
        return textField.getText();
    }
    
    /**
     * Set the text of the text field.
     */
    public void setText(String text) {
        textField.setText(text);
    }
    
    /**
     * Get the prompt text property of the text field.
     */
    public javafx.beans.property.StringProperty promptTextProperty() {
        return textField.promptTextProperty();
    }
    
    /**
     * Get the prompt text of the text field.
     */
    public String getPromptText() {
        return textField.getPromptText();
    }
    
    /**
     * Set the prompt text of the text field.
     */
    public void setPromptText(String text) {
        textField.setPromptText(text);
    }
    
    /**
     * Clear the text field.
     */
    public void clear() {
        textField.clear();
    }
} 