package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket clientSocket;
    private TCPServer tcpServer;

    private final Logger log = LogManager.getLogger(ServerThread.class);

    public ServerThread(Socket clientSocket, TCPServer tcpServer) {
        this.clientSocket = clientSocket;
        this.tcpServer = tcpServer;
    }

    public void run() {
        try {
            tcpServer.getProtocol().execute(
                    tcpServer.getContext(),
                    clientSocket.getInputStream(),
                    clientSocket.getOutputStream());

            log.info("Server thread done.");
        } catch (IOException e) {
            log.error("Exception", e);
        }
    }
}
