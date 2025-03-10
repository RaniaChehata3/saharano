package atlantafx.sampler.model;

import atlantafx.sampler.page.Page;
import atlantafx.sampler.layout.NavTree;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main application model that holds the core application state.
 * Separated from UI concerns as part of MVC architecture.
 */
public class ApplicationModel {

    public enum ViewLayer {
        PAGE,
        SOURCE_CODE
    }

    private static ApplicationModel instance;

    // Application state properties
    private final ReadOnlyObjectWrapper<Class<? extends Page>> selectedPage = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<ViewLayer> currentViewLayer = new ReadOnlyObjectWrapper<>(ViewLayer.PAGE);
    private final ReadOnlyObjectWrapper<NavTree.Item> navTreeRoot = new ReadOnlyObjectWrapper<>();
    private final Map<Class<? extends Page>, NavTree.Item> pageNavMap;

    private ApplicationModel(Map<Class<? extends Page>, NavTree.Item> pageNavMap, NavTree.Item navTreeRoot) {
        this.pageNavMap = pageNavMap;
        this.navTreeRoot.set(navTreeRoot);
    }

    public static synchronized ApplicationModel getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ApplicationModel has not been initialized");
        }
        return instance;
    }

    public static synchronized void initialize(Map<Class<? extends Page>, NavTree.Item> pageNavMap, NavTree.Item navTreeRoot) {
        if (instance != null) {
            throw new IllegalStateException("ApplicationModel has already been initialized");
        }
        instance = new ApplicationModel(pageNavMap, navTreeRoot);
    }

    // Properties getters
    public ReadOnlyObjectProperty<Class<? extends Page>> selectedPageProperty() {
        return selectedPage.getReadOnlyProperty();
    }

    public Class<? extends Page> getSelectedPage() {
        return selectedPage.get();
    }

    public void setSelectedPage(Class<? extends Page> pageClass) {
        selectedPage.set(pageClass);
    }

    public ReadOnlyObjectProperty<ViewLayer> currentViewLayerProperty() {
        return currentViewLayer.getReadOnlyProperty();
    }

    public ViewLayer getCurrentViewLayer() {
        return currentViewLayer.get();
    }

    public void setCurrentViewLayer(ViewLayer layer) {
        currentViewLayer.set(layer);
    }

    public ReadOnlyObjectProperty<NavTree.Item> navTreeRootProperty() {
        return navTreeRoot.getReadOnlyProperty();
    }

    public NavTree.Item getNavTreeRoot() {
        return navTreeRoot.get();
    }

    public NavTree.Item getTreeItemForPage(Class<? extends Page> pageClass) {
        return pageNavMap.get(pageClass);
    }

    public List<NavTree.Item> findPagesBySearch(String filter) {
        List<NavTree.Item> results = new ArrayList<>();
        String lowerCaseFilter = filter.toLowerCase();
        
        for (NavTree.Item item : pageNavMap.values()) {
            // Use the string representation for searching
            // Safely access item's text representation without directly accessing Nav
            try {
                Object value = item.getValue();
                if (value != null) {
                    String searchText = value.toString().toLowerCase();
                    if (searchText.contains(lowerCaseFilter)) {
                        results.add(item);
                    }
                }
            } catch (Exception e) {
                // Skip if any access issues
            }
        }
        
        return results;
    }
} 