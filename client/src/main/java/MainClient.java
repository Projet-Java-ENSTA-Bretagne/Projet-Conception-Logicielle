import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.io.File;

public class MainClient extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            URL loginURL = new File("src/main/pages/login.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(loginURL);

            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root, 1024, 600));
            primaryStage.show();
        }
        catch (Exception e) {
            System.out.println("erreur de chargement de la page" + e );
        }

    }

    public static void main(String[] args) {
        // Setting logging level
        Configurator.setRootLevel(Level.DEBUG);
        TCPClient tcpClient = new TCPClient("localhost", 6666);
        launch(args);
        if (tcpClient.connectToServer()) {
            String request = RequestBuilder.buildWithoutData("PING").toString();
            tcpClient.sendRequest(request);
            tcpClient.disconnectFromServer();
        }

    }

}


