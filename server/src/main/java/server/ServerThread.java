package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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
            String request;
            BufferedReader inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintStream outStream = new PrintStream(clientSocket.getOutputStream());

            // reading the incoming request
            request = inStream.readLine();
            if (request != null) {
                String[] args = request.split(" ");
                log.debug("Received command: " + args[0]);

                tcpServer.getProtocols().get(args[0]).execute(tcpServer.getContext(), inStream, outStream);
            }

            log.info("Closing server thread");
        } catch (IOException e) {
            log.error("Exception", e);
        }
    }
}
