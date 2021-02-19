package server;

import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket clientSocket;
    private TCPServer tcpServer;

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

            System.out.println("Server thread done.");
        } catch (IOException e) {
            System.err.println("[ServerThread] Exception");
        }
    }
}
