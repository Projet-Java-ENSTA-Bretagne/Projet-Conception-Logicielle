import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class MainClient extends Application {

    // Logging
    private static final Logger log = LogManager.getLogger(MainClient.class);

    @Override
    public void start(Stage primaryStage) throws IOException {
        pageManagement.MainController.setMainStage(primaryStage);
        pageManagement.MainController.initializeScenes();
        pageManagement.MainController.switchToLoginScene();
    }

    public static void main(String[] args) {
        // Setting logging level
        Configurator.setRootLevel(Level.DEBUG);

        launch(args);

        /*
        networking.TCPClient tcpClient = new networking.TCPClient("localhost", 6666);
        if (tcpClient.connectToServer()) {
            String request = networking.RequestBuilder.buildWithoutData("PING").toString();
            tcpClient.sendRequest(request);
            tcpClient.disconnectFromServer();
        }
        */
    }
}
