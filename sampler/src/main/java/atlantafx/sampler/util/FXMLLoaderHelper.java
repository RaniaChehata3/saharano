package atlantafx.sampler.util;

import javafx.fxml.FXMLLoader;

import java.net.URL;

/**
 * Helper class for loading FXML files.
 */
public class FXMLLoaderHelper {
    
    private static final String FXML_BASE_PATH = "/atlantafx/sampler/fxml/";
    
    /**
     * Creates an FXMLLoader for the specified FXML file.
     * 
     * @param fxmlFileName The name of the FXML file (e.g. "login.fxml")
     * @return The FXMLLoader instance
     */
    public static FXMLLoader createLoader(String fxmlFileName) {
        String fxmlPath = FXML_BASE_PATH + fxmlFileName;
        URL fxmlUrl = FXMLLoaderHelper.class.getResource(fxmlPath);
        
        if (fxmlUrl == null) {
            throw new IllegalArgumentException("FXML file not found: " + fxmlPath);
        }
        
        return new FXMLLoader(fxmlUrl);
    }
} 