package atlantafx.sampler.view;

import atlantafx.sampler.controller.NavigationController;
import atlantafx.sampler.model.ApplicationModel;
import atlantafx.sampler.page.Page;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

/**
 * Main view component of the application.
 * Responsible for displaying the UI and responding to model changes.
 */
public class MainView extends BorderPane {

    private final ApplicationModel model;
    private final NavigationController navigationController;
    private final StackPane contentArea;
    private final Map<Class<? extends Page>, Page> pageCache = new HashMap<>();

    public MainView() {
        this.model = ApplicationModel.getInstance();
        this.navigationController = NavigationController.getInstance();
        this.contentArea = new StackPane();
        
        setCenter(contentArea);
        
        // Listen for changes in the selected page
        model.selectedPageProperty().addListener(this::onSelectedPageChanged);
        
        // Listen for changes in the view layer
        model.currentViewLayerProperty().addListener(this::onViewLayerChanged);
    }
    
    /**
     * Handle changes to the selected page.
     */
    private void onSelectedPageChanged(ObservableValue<? extends Class<? extends Page>> observable, 
                                      Class<? extends Page> oldValue, 
                                      Class<? extends Page> newValue) {
        if (newValue == null) {
            contentArea.getChildren().clear();
            return;
        }
        
        // Get or create the page
        Page page = getOrCreatePage(newValue);
        
        // Update the view based on the current layer
        updateContentForCurrentLayer(page);
    }
    
    /**
     * Handle changes to the view layer.
     */
    private void onViewLayerChanged(ObservableValue<? extends ApplicationModel.ViewLayer> observable,
                                   ApplicationModel.ViewLayer oldValue,
                                   ApplicationModel.ViewLayer newValue) {
        Class<? extends Page> currentPageClass = model.getSelectedPage();
        if (currentPageClass == null) {
            return;
        }
        
        Page page = getOrCreatePage(currentPageClass);
        updateContentForCurrentLayer(page);
    }
    
    /**
     * Get a page from cache or create a new instance.
     */
    private Page getOrCreatePage(Class<? extends Page> pageClass) {
        return pageCache.computeIfAbsent(pageClass, cls -> {
            try {
                return cls.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create page: " + cls.getName(), e);
            }
        });
    }
    
    /**
     * Update the content area based on the current view layer.
     */
    private void updateContentForCurrentLayer(Page page) {
        ApplicationModel.ViewLayer currentLayer = model.getCurrentViewLayer();
        
        Node content;
        if (currentLayer == ApplicationModel.ViewLayer.PAGE) {
            content = page.getView();
        } else {
            // In a real implementation, this would show the source code view
            content = new SourceCodeView(page.getClass());
        }
        
        contentArea.getChildren().setAll(content);
    }
    
    /**
     * Placeholder for a source code view component.
     */
    private static class SourceCodeView extends StackPane {
        public SourceCodeView(Class<? extends Page> pageClass) {
            // In a real implementation, this would display the source code
            // of the given page class
        }
    }
} 