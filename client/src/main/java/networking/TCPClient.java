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

/**
 * Class describing the behaviour of the TCP client that will be associated with each user.
 */
public class TCPClient {

    private int port;
    private String host;
    private Socket clientSocket;
    private PrintStream outStream;
    private BufferedReader inStream;

    public boolean isConnectedToServer;

    // Logging
    private final Logger log = LogManager.getLogger(TCPClient.class);

    /**
     * Constructor of the TCPCLient class.
     *
     * @param host IP address of the host/server
     * @param port Port number of the host/server
     */
    public TCPClient(String host, int port) {
        this.port = port;
        this.host = host;
        isConnectedToServer = false;
    }

    /**
     * Connects the TCP client to the server.
     *
     * @return Boolean indicating if the connection to the server was succesful
     */
    public boolean connectToServer() {
        boolean ok = false;
        System.out.println("");

        try {
            log.info("Trying to connect to : " + host + ":" + port);
            clientSocket = new Socket(host, port);
            outStream = new PrintStream(clientSocket.getOutputStream());
            inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            log.info("The connection to the server was successful");
            ok = true;
            isConnectedToServer = true;
        } catch (UnknownHostException e) {
            log.error("Unknown host", e);
        } catch (ConnectException e) {
            log.error("Error during connexion : ", e);
        } catch (IOException e) {
            log.error("Error during data exchange : ", e);
        }

        return ok;
    }

    /**
     * Disconnects the client from the server.
     */
    public void disconnectFromServer() {
        try {
            System.out.println("");
            log.info("Client en train de se déconnecter : " + clientSocket);
            outStream.close();
            inStream.close();
            clientSocket.close();
            isConnectedToServer = false;
        } catch (Exception e) {
            log.error("Error while trying to disconnect from the server");
        }
    }

    /**
     * Sends a given request to the server.
     *
     * @param request The request to send
     * @return        The server's response to the client's request
     */
    public String sendRequest(String request) {
        String serverResponse = null;

        // Trying to send the request
        try {
            log.debug("Client request : " + request);
            outStream.println(request);
            outStream.flush();
            // Waiting for the main.java.server response
            serverResponse = inStream.readLine();
            log.debug("Server response : " + serverResponse);
        } catch (UnknownHostException e) {
            log.error("Unknown host : " + e);
        } catch (IOException e) {
            log.error("IOException : " + e);
        }

        return serverResponse;
    }
}
