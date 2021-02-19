import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import protocols.ExampleProtocol;
import protocols.IProtocol;
import server.IContext;
import server.TCPServer;

import java.util.HashMap;

public class MainServer {

    public static void main(String[] args) {
        // Setting logging level
        Configurator.setRootLevel(Level.DEBUG);

        // Setup protocols
        HashMap<String, IProtocol> protocols = new HashMap<>();
        protocols.put(ExampleProtocol.requestName, new ExampleProtocol());

        TCPServer server = new TCPServer(new IContext() {}, protocols, 6666);
        server.start();
    }

}
