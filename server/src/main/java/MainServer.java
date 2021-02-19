import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import protocols.ExampleProtocol;
import server.IContext;
import server.TCPServer;

public class MainServer {

    public static void main(String[] args) {
        // Setting logging level
        Configurator.setRootLevel(Level.DEBUG);

        TCPServer server = new TCPServer(new IContext() {}, new ExampleProtocol(), 6666);
        server.start();
    }

}
