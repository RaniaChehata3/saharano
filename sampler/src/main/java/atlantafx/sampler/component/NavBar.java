package atlantafx.sampler.component;

import atlantafx.base.theme.Styles;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * A navigation bar component for application navigation.
 */
public class NavBar extends HBox {
    
    private final ObservableList<NavButton> buttons = FXCollections.observableArrayList();
    private final ObjectProperty<NavButton> activeButton = new SimpleObjectProperty<>();
    
    public NavBar() {
        setSpacing(10);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("nav-bar");
        
        // Add style specific to this component
        getStyleClass().add(Styles.STRIPED);
        getStyleClass().add(Styles.ACCENT);
    }
    
    /**
     * Add a button to the navigation bar.
     *
     * @param text The text to display on the button
     * @param icon The icon to display on the button
     * @param action The action to perform when the button is clicked
     * @return The created NavButton
     */
    public NavButton addButton(String text, Feather icon, Runnable action) {
        NavButton button = new NavButton(text, icon, action);
        buttons.add(button);
        getChildren().add(button);
        
        // If this is the first button, make it active
        if (buttons.size() == 1) {
            setActiveButton(button);
        }
        
        return button;
    }
    
    /**
     * Add a spacer to push elements to the right.
     */
    public void addSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        getChildren().add(spacer);
    }
    
    /**
     * Set the active button.
     *
     * @param button The button to set as active
     */
    public void setActiveButton(NavButton button) {
        if (buttons.contains(button)) {
            // Deactivate the current active button
            if (activeButton.get() != null) {
                activeButton.get().setActive(false);
            }
            
            // Activate the new button
            button.setActive(true);
            activeButton.set(button);
        }
    }
    
    /**
     * Get the active button.
     *
     * @return The active button
     */
    public NavButton getActiveButton() {
        return activeButton.get();
    }
    
    /**
     * Get the active button property.
     *
     * @return The active button property
     */
    public ObjectProperty<NavButton> activeButtonProperty() {
        return activeButton;
    }
    
    /**
     * A button component for the navigation bar.
     */
    public class NavButton extends Button {
        private final Runnable action;
        private boolean isActive = false;
        
        public NavButton(String text, Feather icon, Runnable action) {
            super(text, new FontIcon(icon));
            this.action = action;
            
            getStyleClass().add(Styles.BUTTON_OUTLINED);
            setPrefHeight(40);
            
            setOnAction(e -> {
                setActiveButton(this);
                if (action != null) {
                    action.run();
                }
            });
        }
        
        /**
         * Set whether this button is active.
         *
         * @param active True if the button should be active
         */
        public void setActive(boolean active) {
            isActive = active;
            
            if (active) {
                getStyleClass().add(Styles.ACCENT);
            } else {
                getStyleClass().remove(Styles.ACCENT);
            }
        }
        
        /**
         * Check if this button is active.
         *
         * @return True if the button is active
         */
        public boolean isActive() {
            return isActive;
        }
    }
} 