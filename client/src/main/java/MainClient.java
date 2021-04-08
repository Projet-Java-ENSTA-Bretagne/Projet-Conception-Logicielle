import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

import pageManagement.MainController;

/**
 * The main class of the client part of the project.
 * To configure this part of the project, please refer to the YouTube video
 * linked in "Client-config-YouTube-link.txt" (in the "resources" bundle).
 */
public class MainClient extends Application {
    // Logging
    private static final Logger log = LogManager.getLogger(MainClient.class);

    /**
     * Method that initializes the main scenes (Login, Home, Discussion), and sets the scene
     * to the Login scene.
     * This method is executed when the "launch" method is called.
     *
     * @param mainStage The main window
     */
    @Override
    public void start(Stage mainStage) throws IOException {
        MainController.initializeMainStage(mainStage);
        MainController.initializeMainScenes();
        MainController.initializeFSM();

        String serverIpAddress = "localhost";
        int serverPort = 6666;
        MainController.initializeTcpClient(serverIpAddress, serverPort);

        MainController.switchToLoginScene();
    }

    /**
     * Main method of the client part of the project.
     *
     * @param args Default argument
     */
    public static void startApp(String[] args) {
        // Setting logging level
        Configurator.setRootLevel(Level.DEBUG);

        // Executes the "start" method
        launch(args);
    }
}
