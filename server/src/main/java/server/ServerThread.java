package server;

import database.UserNotLoggedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.SQLException;

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
            String requestString;
            BufferedReader inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintStream outStream = new PrintStream(clientSocket.getOutputStream());

            // reading the incoming request
            requestString = inStream.readLine();
            if (requestString != null) {
                JSONObject request = new JSONObject(requestString);
                log.debug("Received command: " + request.getString("command"));

                try {
                    tcpServer.getProtocols().get(request.getString("command")).
                            execute(tcpServer.getContext(),
                                    inStream,
                                    outStream,
                                    request);
                } catch (SQLException e) {
                    log.error(e);
                    ResponseBuilder.forRequest(request, outStream).serverError(e.toString());
                } catch (UserNotLoggedException e) {
                    log.warn(e);
                    ResponseBuilder.forRequest(request, outStream).deniedError(e.toString());
                }
            }

            log.info("Closing server thread");
        } catch (IOException e) {
            log.error("Exception", e);
        }
    }
}
