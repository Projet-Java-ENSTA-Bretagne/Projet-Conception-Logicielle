package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocols.IProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class TCPServer extends Thread{

    private static int nbClients = 0;

    private int maxClients;
    private int port;
    private Socket clientSocket;
    private IContext context;
    private boolean running;
    private HashMap<String, IProtocol> protocols;

    // Logging
    private final Logger log = LogManager.getLogger(TCPServer.class);

    public TCPServer(int port) {
        this.port = port;
        maxClients = 10;
    }

    public TCPServer(IContext context, HashMap<String, IProtocol> protocols, int port) {
        this(port);
        this.context = context;
        this.protocols = protocols;
    }

    public HashMap<String, IProtocol> getProtocols() {
        return this.protocols;
    }

    public IContext getContext() {
        return this.context;
    }

    public boolean isRunning() { return this.running; }
    public void setRunning(boolean value) {
        this.running = value;
    }

    public String toString() {
        return "[TCPServer] Open on Port: " + port + ", Context: " + context + ", Nb of clients: " + nbClients;
    }

    public void run() {
        // Creating main.java.server socket
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            log.error("Could not listen on port: " + port, e);
            System.exit(1);
        }

        this.running = true;
        while (this.running) {
            // Listening to a maximum of maxConnexions at the same time
            while (nbClients <= maxClients) {
                try {
                    log.info("Waiting for a new client to connect.");
                    clientSocket = serverSocket.accept();
                    nbClients++;
                    log.info("Number of clients : " + nbClients);
                } catch (IOException e) {
                    log.error("Accept failed: " + serverSocket.getLocalPort(), e);
                    System.exit(1);
                }
                ServerThread st = new ServerThread(clientSocket, this);
                st.start();
            }
            log.warn("There are already " + nbClients + " clients connected. Reached maximum.");
            while (nbClients > maxClients) {}
        }

        // Closing properly the main.java.server
        try {
            serverSocket.close();
            nbClients--;
        } catch (IOException e) {
            log.error("Could not close the main.java.server...");
        }
    }
}
