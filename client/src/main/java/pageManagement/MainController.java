package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import java.io.IOException;
import java.io.File;
import java.net.URL;

/**
 * Class handling the switches between the main scenes (Login, Home, Discussion).
 */
public class MainController {
    // Logging
    private static final Logger log = LogManager.getLogger(MainController.class);

    private static Stage mainStage;
    private static Scene loginScene;
    private static Scene homeScene;
    private static Scene discussionScene;
    private static String currentScene;

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    public static Scene getLoginScene() {
        return loginScene;
    }

    public static Scene getHomeScene() {
        return homeScene;
    }

    public static Scene getDiscussionScene() {
        return discussionScene;
    }

    public static String getCurrentScene() {
        // NB : here "currentScene" is a simple descriptive String, not a Scene object !
        return currentScene;
    }

    public static void setCurrentScene(String scene) {
        currentScene = scene;
    }

    /**
     * Loads the 3 main FXML files (login, home, discussion), so that the associated scenes
     * can be easily reused (statically) throughout the code.
     *
     * @throws IOException If error when FXMLLoader.load() is called
     */
    public static void initializeMainScenes() throws IOException {
        URL loginURL = new File("src/main/pages/login.fxml").toURI().toURL();
        Parent loginRoot = FXMLLoader.load(loginURL);
        loginScene = new Scene(loginRoot, 659, 402);

        URL homeURL = new File("src/main/pages/home.fxml").toURI().toURL();
        Parent homeRoot = FXMLLoader.load(homeURL);
        homeScene = new Scene(homeRoot, 659, 402);

        URL discussionURL = new File("src/main/pages/discussion.fxml").toURI().toURL();
        Parent discussionRoot = FXMLLoader.load(discussionURL);
        discussionScene = new Scene(discussionRoot, 659, 402);

        mainStage.getIcons().add(new Image("duck-icon.png")); // adding duck icon to main stage
        setCurrentScene("");

        hasAlreadySwitchedToHomeScene = false;
        hasAlreadySwitchedToDiscussionScene = false;
    }

    /**
     * Switches to the Login scene. If already in the Login scene, nothing is done.
     */
    public static void switchToLoginScene() {
        if (!getCurrentScene().equals("login")) {
            setCurrentScene("login");

            mainStage.setTitle("Login");
            mainStage.setScene(loginScene);
            mainStage.show();
        }

        else {
            log.warn("Already in login scene !");
        }
    }

    private static boolean hasAlreadySwitchedToHomeScene;

    /**
     * Switches to the Home scene. If already in the Home scene, nothing is done.
     */
    public static void switchToHomeScene() {
        if (!getCurrentScene().equals("home")) {
            setCurrentScene("home");

            mainStage.setTitle("Home");
            mainStage.setScene(homeScene);
            mainStage.show();

            // getting the **NOT-NULL** thumbnail HBox (+ the ScrollPane) from the Home scene
            if (!hasAlreadySwitchedToHomeScene) {
                HomeController.initializeGroupThumbnailHBox((HBox) homeScene.lookup("#groupThumbnailHBox"));
                HomeController.initializeGroupThumbnailScrollPane((ScrollPane) homeScene.lookup("#groupThumbnailScrollPane"));
                hasAlreadySwitchedToHomeScene = true;
            }
        }

        else {
            log.warn("Already in home scene !");
        }
    }

    private static boolean hasAlreadySwitchedToDiscussionScene;

    /**
     * Switches to the Discussion scene. If already in the Discussion scene, nothing is done.
     */
    public static void switchToDiscussionScene() {
        if (!getCurrentScene().equals("discussion")) {
            setCurrentScene("discussion");

            mainStage.setTitle("Discussion");
            mainStage.setScene(discussionScene);
            mainStage.show();

            // getting the **NOT-NULL** discussion VBox (+ the ScrollPane) from the Discussion scene
            if (!hasAlreadySwitchedToDiscussionScene) {
                DiscussionController.initializeDiscussionVBox((VBox) discussionScene.lookup("#discussionVBox"));
                DiscussionController.initializeDiscussionScrollPane((ScrollPane) discussionScene.lookup("#discussionScrollPane"));
                hasAlreadySwitchedToDiscussionScene = true;
            }
        }

        else {
            log.warn("Already in discussion scene !");
        }
    }
}
