import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import protocols.ExampleProtocol;
import protocols.IProtocol;
import server.ConfigurationManagement;
import server.IContext;
import server.ServerConfiguration;
import server.TCPServer;

import java.util.HashMap;

public class MainServer {

    public static void main(String[] args) {
        // Setting logging level
        Configurator.setRootLevel(Level.DEBUG);

        // Getting configuration
        ConfigurationManagement.getInstance().fromPath("config.json");
        ServerConfiguration serverConfig = ConfigurationManagement.getInstance().getServerConfiguration();

        // Setup protocols
        HashMap<String, IProtocol> protocols = new HashMap<>();
        protocols.put(ExampleProtocol.requestName, new ExampleProtocol());

        TCPServer server = new TCPServer(new IContext() {}, protocols, serverConfig.getPort());
        server.start();
    }

}
