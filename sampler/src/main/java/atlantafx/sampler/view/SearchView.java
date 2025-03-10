package atlantafx.sampler.view;

import atlantafx.sampler.controller.NavigationController;
import atlantafx.sampler.controller.SearchController;
import atlantafx.sampler.layout.NavTree;
import atlantafx.sampler.page.Page;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * View component for search functionality.
 * Responsible for displaying search UI and interacting with the search controller.
 */
public class SearchView extends BorderPane {

    private final SearchController searchController;
    private final NavigationController navigationController;
    private final TextField searchField;
    private final ListView<NavTree.Item> resultsListView;

    public SearchView() {
        this.searchController = SearchController.getInstance();
        this.navigationController = NavigationController.getInstance();
        
        // Create search field
        searchField = new TextField();
        searchField.setPromptText("Search pages...");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> performSearch(newVal));
        
        // Create search button
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> performSearch(searchField.getText()));
        
        // Create search header
        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBox.setPadding(new Insets(10));
        
        // Create results list
        resultsListView = new ListView<>();
        resultsListView.setCellFactory(lv -> new NavTreeItemCell());
        resultsListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                NavTree.Item selectedItem = resultsListView.getSelectionModel().getSelectedItem();
                if (selectedItem != null && !selectedItem.isGroup()) {
                    Class<? extends Page> pageClass = selectedItem.pageClass();
                    if (pageClass != null) {
                        navigationController.navigateToPage(pageClass);
                    }
                }
            }
        });
        
        // Create results container
        VBox resultsContainer = new VBox(10);
        Label resultsLabel = new Label("Results:");
        resultsContainer.getChildren().addAll(resultsLabel, resultsListView);
        resultsContainer.setPadding(new Insets(0, 10, 10, 10));
        VBox.setVgrow(resultsListView, Priority.ALWAYS);
        
        // Set up layout
        setTop(searchBox);
        setCenter(resultsContainer);
    }
    
    /**
     * Perform a search with the given query.
     */
    private void performSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            resultsListView.getItems().clear();
            return;
        }
        
        List<NavTree.Item> results = searchController.searchPages(query);
        resultsListView.getItems().setAll(results);
    }
    
    /**
     * Custom cell for displaying NavTree.Item objects.
     */
    private static class NavTreeItemCell extends javafx.scene.control.ListCell<NavTree.Item> {
        @Override
        protected void updateItem(NavTree.Item item, boolean empty) {
            super.updateItem(item, empty);
            
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Safely get string representation without directly accessing Nav
                try {
                    Object value = item.getValue();
                    if (value != null) {
                        setText(value.toString());
                    } else {
                        setText("Unknown item");
                    }
                } catch (Exception e) {
                    setText("Unknown item");
                }
            }
        }
    }
} 