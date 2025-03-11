package atlantafx.sampler;

import static java.nio.charset.StandardCharsets.UTF_8;

import atlantafx.sampler.event.BrowseEvent;
import atlantafx.sampler.event.DefaultEventBus;
import atlantafx.sampler.event.HotkeyEvent;
import atlantafx.sampler.event.Listener;
import atlantafx.sampler.theme.ThemeManager;
import atlantafx.sampler.view.MainAppView;
import fr.brouillard.oss.cssfx.CSSFX;
import fr.brouillard.oss.cssfx.api.URIToPathConverter;
import fr.brouillard.oss.cssfx.impl.log.CSSFXLogger;
import fr.brouillard.oss.cssfx.impl.log.CSSFXLogger.LogLevel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Main application launcher using MVC architecture.
 */
public class MVCLauncher extends Application {

    public static final boolean IS_DEV_MODE = "DEV".equalsIgnoreCase(
        Resources.getPropertyOrEnv("atlantafx.mode", "ATLANTAFX_MODE")
    );

    public static final List<KeyCodeCombination> SUPPORTED_HOTKEYS = List.of(
        new KeyCodeCombination(KeyCode.SLASH),
        new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN),
        new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN)
    );

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Thread.currentThread().setUncaughtExceptionHandler(new DefaultExceptionHandler(stage));
        loadApplicationProperties();

        if (IS_DEV_MODE) {
            System.out.println("[WARNING] Application is running in development mode.");
        }

        // Use our new MainAppView with authentication instead of ApplicationWindow
        var root = new MainAppView();

        var antialiasing = Platform.isSupported(ConditionalFeature.SCENE3D)
            ? SceneAntialiasing.BALANCED
            : SceneAntialiasing.DISABLED;
        var scene = new Scene(root, 1200, 800, false, antialiasing);
        scene.setOnKeyPressed(this::dispatchHotkeys);
        
        // Set default stylesheet directly
        String appStyle = getClass().getResource("/atlantafx/sampler/assets/styles/index.css").toExternalForm();
        scene.getStylesheets().add(appStyle);
        
        // Apply default modena style
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);

        stage.setScene(scene);
        stage.setTitle("Medical System - " + System.getProperty("app.name"));
        loadIcons(stage);
        stage.setResizable(true);
        stage.setOnCloseRequest(t -> Platform.exit());

        // register event listeners
        DefaultEventBus.getInstance().subscribe(BrowseEvent.class, this::onBrowseEvent);

        Platform.runLater(() -> {
            stage.show();
            stage.requestFocus();
        });
    }

    private void loadIcons(Stage stage) {
        int iconSize = 16;
        while (iconSize <= 1024) {
            stage.getIcons().add(new Image(Resources.getResourceAsStream("assets/icon-rounded-" + iconSize + ".png")));
            iconSize *= 2;
        }
    }

    private void loadApplicationProperties() {
        Properties properties = new Properties();
        try (InputStreamReader in = new InputStreamReader(Resources.getResourceAsStream("application.properties"),
            UTF_8)) {
            properties.load(in);
            properties.forEach((key, value) -> System.setProperty(
                String.valueOf(key),
                String.valueOf(value)
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void dispatchHotkeys(KeyEvent event) {
        for (KeyCodeCombination k : SUPPORTED_HOTKEYS) {
            if (k.match(event)) {
                DefaultEventBus.getInstance().publish(new HotkeyEvent(k));
                return;
            }
        }
    }

    @Listener
    private void onBrowseEvent(BrowseEvent event) {
        getHostServices().showDocument(event.getUri().toString());
    }
} 