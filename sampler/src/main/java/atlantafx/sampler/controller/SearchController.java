package atlantafx.sampler.controller;

import atlantafx.sampler.layout.NavTree;
import atlantafx.sampler.model.ApplicationModel;
import atlantafx.sampler.page.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller responsible for handling search functionality.
 * Part of the MVC architecture to separate search logic from UI.
 */
public class SearchController {

    private static SearchController instance;
    private final ApplicationModel model;

    private SearchController() {
        this.model = ApplicationModel.getInstance();
    }

    public static synchronized SearchController getInstance() {
        if (instance == null) {
            instance = new SearchController();
        }
        return instance;
    }

    /**
     * Search for pages matching the given filter.
     *
     * @param filter The search filter text
     * @return A list of NavTree.Item objects matching the filter
     */
    public List<NavTree.Item> searchPages(String filter) {
        if (filter == null || filter.trim().isEmpty()) {
            return List.of();
        }

        List<NavTree.Item> results = new ArrayList<>();
        String lowerCaseFilter = filter.toLowerCase();
        
        // Get all page navigation items
        Map<Class<? extends Page>, NavTree.Item> pageNavMap = getAllPageNavItems();
        
        // Filter items based on their string representation
        for (NavTree.Item item : pageNavMap.values()) {
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
    
    /**
     * Get all page navigation items from the model.
     * This is a helper method that would need to be implemented based on how
     * the navigation items are stored in the actual application.
     */
    private Map<Class<? extends Page>, NavTree.Item> getAllPageNavItems() {
        // In a real implementation, this would return all page navigation items
        // For now, we'll use a placeholder implementation
        return Map.of();
    }
} 