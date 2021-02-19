import protocols.ExampleProtocol;
import server.IContext;
import server.TCPServer;

public class MainServer {

    public static void main(String[] args) {
        TCPServer server = new TCPServer(new IContext() {}, new ExampleProtocol(), 6666);
        server.start();
    }

}
