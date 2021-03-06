import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.io.File;
import javafx.scene.image.Image;

public class MainController {

    private static Stage mainStage;
    private static Scene loginScene;
    private static Scene homeScene;
    private static String currentScene;

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

    public static String getCurrentScene() {
        return currentScene;
    }

    public static void setCurrentScene(String scene) {
        currentScene = scene;
    }

    public static void initializeScenes() throws IOException {
        URL loginURL = new File("src/main/pages/login.fxml").toURI().toURL();
        Parent loginRoot = FXMLLoader.load(loginURL);
        loginScene = new Scene(loginRoot, 659, 402);

        URL homeURL = new File("src/main/pages/Home.fxml").toURI().toURL();
        Parent homeRoot = FXMLLoader.load(homeURL);
        homeScene = new Scene(homeRoot, 659, 402);

        mainStage.getIcons().add(new Image("DUCK.png")); // adding duck icon to main stage
        setCurrentScene("");
    }

    public static void switchToLoginScene() {
        if (!getCurrentScene().equals("login")) {
            setCurrentScene("login");

            mainStage.setTitle("Login");
            mainStage.setScene(loginScene);
            mainStage.show();
        }

        else {
            System.out.println("Already in login scene");
        }
    }

    public static void switchToHomeScene() {
        if (!getCurrentScene().equals("home")) {
            setCurrentScene("home");

            mainStage.setTitle("Home");
            mainStage.setScene(homeScene);
            mainStage.show();
        }

        else {
            System.out.println("Already in home scene");
        }
    }

    //

}
