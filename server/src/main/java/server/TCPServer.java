package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocols.IProtocol;
import sun.misc.Signal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class TCPServer extends Thread{

    private int maxClients;
    private int port;
    private IContext context;
    private boolean running;
    private HashMap<String, IProtocol> protocols;
    private ArrayList<ServerThread> serverThreads = new ArrayList<>();

    // Logging
    private final Logger log = LogManager.getLogger(TCPServer.class);

    public TCPServer(int port) {
        this.port = port;
        maxClients = ConfigurationManagement.getInstance().getServerConfiguration().getMaxClients();
    }

    public TCPServer(IContext context, HashMap<String, IProtocol> protocols, int port) {
        this(port);
        this.context = context;
        this.protocols = protocols;

        log.info("Creating new server with " + this.protocols.size() + " protocols");

        // Ctrl+C handeling
        Signal.handle(new Signal("INT"), signal -> {
            log.info("I manage to catch a Ctrl+C event. Shutting down the server beautifully...");
            log.info("Closing " + serverThreads.size() + " connections.");

            // copying opened connections
            ArrayList<ServerThread> connectionsToClose = new ArrayList<>(serverThreads);

            // sending shutdown signal to all threads
            for (ServerThread th : connectionsToClose) {
                th.signalStop();
            }
            log.info("Waiting for all opened connection to close ...");
            // waiting for all sockets to close
            while (serverThreads.size() > 0) {}
            log.info("Done! Goodbye ;)");
            System.exit(0);
        });
    }

    public HashMap<String, IProtocol> getProtocols() {
        return this.protocols;
    }

    public IContext getContext() {
        return this.context;
    }

    public void removeClient(ServerThread st) {
        serverThreads.remove(st);
    }

    public boolean isRunning() { return this.running; }
    public void setRunning(boolean value) {
        this.running = value;
    }

    public String toString() {
        return "[TCPServer] Open on Port: " + port + ", Context: " + context + ", Nb of clients max: " + maxClients;
    }

    public void run() {
        // Creating main.java.server socket
        ServerSocket serverSocket = null;
        try {
            log.info("Starting server on port: " + port);
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            log.error("Could not listen on port: " + port, e);
            System.exit(1);
        }

        this.running = true;
        while (this.running) {
            // Listening to a maximum of maxConnexions at the same time
            while (serverThreads.size() <= maxClients) {
                try {
                    log.info("Waiting for a new client to connect.");
                    ServerThread st = new ServerThread(serverSocket.accept(), this);
                    serverThreads.add(st);
                    log.info("Number of clients : " + serverThreads.size());
                    st.start();
                } catch (IOException e) {
                    log.error("Accept failed: " + serverSocket.getLocalPort(), e);
                    System.exit(1);
                }
            }
            log.warn("There are already " + serverThreads.size() + " clients connected. Reached maximum.");
            while (serverThreads.size() > maxClients);
        }

        // Closing properly the main.java.server
        try {
            serverSocket.close();
        } catch (IOException e) {
            log.error("Could not close the main.java.server...");
        }
    }
}
