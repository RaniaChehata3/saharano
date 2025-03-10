package atlantafx.sampler.controller;

import atlantafx.sampler.model.ApplicationModel;
import atlantafx.sampler.page.Page;
import atlantafx.sampler.layout.NavTree;

/**
 * Controller responsible for handling navigation within the application.
 * Part of the MVC architecture to separate navigation logic from UI.
 */
public class NavigationController {

    private static NavigationController instance;
    private final ApplicationModel model;

    private NavigationController() {
        this.model = ApplicationModel.getInstance();
    }

    public static synchronized NavigationController getInstance() {
        if (instance == null) {
            instance = new NavigationController();
        }
        return instance;
    }

    /**
     * Navigate to a specific page.
     *
     * @param pageClass The class of the page to navigate to
     */
    public void navigateToPage(Class<? extends Page> pageClass) {
        model.setSelectedPage(pageClass);
    }

    /**
     * Switch between view layers (e.g., between page view and source code view).
     *
     * @param layer The view layer to switch to
     */
    public void switchViewLayer(ApplicationModel.ViewLayer layer) {
        model.setCurrentViewLayer(layer);
    }

    /**
     * Toggle between page view and source code view.
     */
    public void toggleSourceCodeView() {
        ApplicationModel.ViewLayer currentLayer = model.getCurrentViewLayer();
        ApplicationModel.ViewLayer newLayer = (currentLayer == ApplicationModel.ViewLayer.PAGE) 
            ? ApplicationModel.ViewLayer.SOURCE_CODE 
            : ApplicationModel.ViewLayer.PAGE;
        
        model.setCurrentViewLayer(newLayer);
    }

    /**
     * Get the tree item corresponding to a page class.
     *
     * @param pageClass The class of the page
     * @return The corresponding NavTree.Item
     */
    public NavTree.Item getTreeItemForPage(Class<? extends Page> pageClass) {
        return model.getTreeItemForPage(pageClass);
    }
} 