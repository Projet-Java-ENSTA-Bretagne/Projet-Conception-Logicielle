package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.io.File;
import javafx.scene.image.Image;

/**
 * Class handling the switches between the main scenes (Login, Home, Discussion).
 */
public class MainController {
    // Logging
    private static final Logger log = LogManager.getLogger(MainController.class);

    private static Stage mainStage;
    private static Scene loginScene;
    private static Scene homeScene;
    private static String currentScene;
    private static Scene discussionScene;

    public static Stage getMainStage() {
        return mainStage;
    }

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

        mainStage.getIcons().add(new Image("DUCK.png")); // adding duck icon to main stage
        setCurrentScene("");
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

    /**
     * Switches to the Home scene. If already in the Home scene, nothing is done.
     */
    public static void switchToHomeScene() {
        if (!getCurrentScene().equals("home")) {
            setCurrentScene("home");

            mainStage.setTitle("Home");
            mainStage.setScene(homeScene);
            mainStage.show();
        }

        else {
            log.warn("Already in home scene !");
        }
    }

    /**
     * Switches to the Discussion scene. If already in the Discussion scene, nothing is done.
     */
    public static void switchToDiscussionScene() {
        if (!getCurrentScene().equals("discussion")) {
            setCurrentScene("discussion");

            mainStage.setTitle("Discussion");
            mainStage.setScene(discussionScene);
            mainStage.show();
        }

        else {
            log.warn("Already in discussion scene !");
        }
    }
}
