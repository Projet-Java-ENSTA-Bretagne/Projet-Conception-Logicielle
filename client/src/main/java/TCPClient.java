import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {

    private int port;
    private String host;
    private Socket serverSocket;
    private PrintStream outStream;
    private BufferedReader inStream;

    // Logging
    private final Logger log = LogManager.getLogger(TCPClient.class);

    public TCPClient(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public boolean connectToServer() {
        boolean ok = false;
        try {
            log.info("Trying to connect to: " + host + ":" + port);
            serverSocket = new Socket(host, port);
            outStream = new PrintStream(serverSocket.getOutputStream());
            inStream = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            log.info("Connexion successful");
            ok = true;
        } catch (UnknownHostException e) {
            log.error("Unknown host", e);
        } catch (ConnectException e) {
            log.error("Error during connexion", e);
        } catch (IOException e) {
            log.error("Error during data exchange", e);
        }

        return ok;
    }

    public void disconnectFromServer() {
        try {
            log.info("Client : " + serverSocket);
            outStream.close();
            inStream.close();
            serverSocket.close();
        } catch (Exception e) {
            log.error("Error during disconnect...");
        }
    }

    public String sendRequest(String request) {
        String serverResponse = null;
        // Trying to send the request
        try {
            log.debug("Client request: " + request);
            outStream.println(request);
            outStream.flush();
            // Waiting for the main.java.server response
            serverResponse = inStream.readLine();
            log.debug("Server response: " + serverResponse);
        } catch (UnknownHostException e) {
            log.error("Unknown host : " + e);
        } catch (IOException e) {
            log.error("IOException : " + e);
        }

        return serverResponse;
    }
}
