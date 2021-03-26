package server;

import database.UserNotLoggedException;
import fsm.ActionsEnum;
import fsm.IFiniteStateMachine;
import fsm.ServerFSM;
import fsm.StatesEnum;
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
    private IFiniteStateMachine fsm;

    private final Logger log = LogManager.getLogger(ServerThread.class);

    public ServerThread(Socket clientSocket, TCPServer tcpServer) {
        this.clientSocket = clientSocket;
        this.tcpServer = tcpServer;
        this.fsm = new ServerFSM();
    }

    public void run() {
        fsm.transit(ActionsEnum.ACCEPT.getAction());

        // while the server is connected to the client
        while (fsm.getCurrentState() != StatesEnum.CLOSING.getState()) {
            // we try to get the request
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
                        if (fsm.getCurrentState() == StatesEnum.RECEIVING.getState()) {
                            // we accept the request
                            fsm.transit(ActionsEnum.RECEIVE.getAction());
                            tcpServer.getProtocols().get(request.getString("command")).
                                    execute(tcpServer.getContext(),
                                            outStream,
                                            request);
                            fsm.transit(ActionsEnum.SEND.getAction());
                        } else {
                            // there is already a request pending so we refuse it
                            log.warn("Cannot accept request, the server needs to answer to the client beforehand.");
                        }
                    } catch (SQLException e) {
                        log.error(e);
                        ResponseBuilder.forRequest(request, outStream).serverError(e.toString());
                        fsm.transit(ActionsEnum.SEND.getAction());
                    } catch (UserNotLoggedException e) {
                        log.warn(e);
                        ResponseBuilder.forRequest(request, outStream).deniedError(e.toString());
                        fsm.transit(ActionsEnum.SEND.getAction());
                    }
                }
            } catch (IOException e) {
                // if we catch an exception like the client disconnected from the server
                log.error("Exception", e);
                log.info("Closing server thread");
                fsm.transit(ActionsEnum.CLOSE.getAction());
            }
        }

        // closing properly the thread
        tcpServer.removeClient();
        try {
            clientSocket.close();
        } catch (IOException e) {
            log.error("Could not properly close the socket with the client");
        }

    }
}
