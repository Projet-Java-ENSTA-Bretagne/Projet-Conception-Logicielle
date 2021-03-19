package networking;

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
    /**
     * Class describing the behaviour of the TCP client that will be associated with each user.
     */

    private int port;
    private String host;
    private Socket serverSocket;
    private PrintStream outStream;
    private BufferedReader inStream;

    // Logging
    private final Logger log = LogManager.getLogger(TCPClient.class);

    public TCPClient(String host, int port) {
        /**
         * Constructor of the TCPCLient class.
         * @param host IP address of the host/server
         * @param port Port number of the host/server
         */

        this.port = port;
        this.host = host;
    }

    public boolean connectToServer() {
        /**
         * Connects the TCP client to the server.
         * @param void
         * @return Boolean indicating if the connection to the server was succesful
         */

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
        /**
         * Disconnects the client from the server.
         * @param void
         * @return void
         */

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
        /**
         * Sends a given request to the server.
         * @param request The request to send
         * @return The server's response to the client's request
         */

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
