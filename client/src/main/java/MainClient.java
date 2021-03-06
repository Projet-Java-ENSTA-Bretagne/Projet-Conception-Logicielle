import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class MainClient extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        MainController.setMainStage(primaryStage);
        MainController.initializeScenes();
        MainController.switchToLoginScene();
    }

    public static void main(String[] args) {
        // Setting logging level
        Configurator.setRootLevel(Level.DEBUG);

        launch(args);

        /*
        TCPClient tcpClient = new TCPClient("localhost", 6666);
        if (tcpClient.connectToServer()) {
            String request = RequestBuilder.buildWithoutData("PING").toString();
            tcpClient.sendRequest(request);
            tcpClient.disconnectFromServer();
        }
        */
    }
}
