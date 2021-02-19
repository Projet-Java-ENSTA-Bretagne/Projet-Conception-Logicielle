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

    public TCPClient(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public boolean connectToServer() {
        boolean ok = false;
        try {
            System.out.println("Trying to connect to : " + host + ":" + port);
            serverSocket = new Socket(host, port);
            outStream = new PrintStream(serverSocket.getOutputStream());
            inStream = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            ok = true;
        } catch (UnknownHostException e) {
            System.err.println("Unknown host : " + e);
        } catch (ConnectException e) {
            System.err.println("Error during connexion : " + e);
        } catch (IOException e) {
            System.err.println("Error during data exchange :  "+ e);
        }
        System.out.println("Connexion successful");

        return ok;
    }

    public void disconnectFromServer() {
        try {
            System.out.println("[TCPClient] Client : " + serverSocket);
            outStream.close();
            inStream.close();
            serverSocket.close();
        } catch (Exception e) {
            System.err.println("Error during disconnect...");
        }
    }

    public String sendRequest(String request) {
        String serverResponse = null;
        // Trying to send the request
        try {
            System.out.println("Client request: " + request);
            outStream.println(request);
            outStream.flush();
            // Waiting for the server response
            serverResponse = inStream.readLine();
            System.out.println("Server response: " + serverResponse);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host : " + e);
        } catch (IOException e) {
            System.err.println("IOException : " + e);
        }

        return serverResponse;
    }
}
