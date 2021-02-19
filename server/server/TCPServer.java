package server;

import protocols.IProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread{

    private static int nbConnexions = 0;

    private int maxConnexions;
    private int port;
    private Socket clientSocket;
    private IContext context;
    private IProtocol protocol;

    public TCPServer(int port) {
        this.port = port;
        maxConnexions = 10;
    }

    public TCPServer(IContext context, IProtocol protocol, int port) {
        this(port);
        this.context = context;
        this.protocol = protocol;
    }

    public IProtocol getProtocol() {
        return this.protocol;
    }

    public IContext getContext() {
        return this.context;
    }

    public String toString() {
        return "[TCPServer] Open on Port: " + port + ", Context: " + context;
    }

    public void run() {
        // Creating server socket
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            System.err.println("Cound not listen on port: " + port + ", - " + e);
            System.exit(1);
        }

        // Listening to a maximum of maxConnexions at the same time
        while (nbConnexions <= maxConnexions) {
            try {
                System.out.println("Waiting for a new client to connect.");
                clientSocket = serverSocket.accept();
                nbConnexions++;
                System.out.println("Number of clients : " + nbConnexions);
            } catch (IOException e) {
                System.err.println("Accept failed: " + serverSocket.getLocalPort() + ", " + e);
                System.exit(1);
            }
            ServerThread st = new ServerThread(clientSocket, this);
            st.start();
        }
        System.out.println("There are already " + nbConnexions + " clients connected. Reached maximum.");

        // Closing properly the server
        try {
            serverSocket.close();
            nbConnexions--;
        } catch (IOException e) {
            System.err.println("Could not close the server...");
        }
    }
}
