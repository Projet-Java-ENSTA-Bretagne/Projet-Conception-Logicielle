import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

public class MainClient {

    public static void main(String[] args) {
        // Setting logging level
        Configurator.setRootLevel(Level.DEBUG);

        TCPClient tcpClient = new TCPClient("localhost", 6666);

        if (tcpClient.connectToServer()) {
            tcpClient.sendRequest("PING");
            tcpClient.disconnectFromServer();
        }
    }
}
