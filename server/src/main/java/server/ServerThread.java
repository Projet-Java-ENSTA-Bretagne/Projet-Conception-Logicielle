package server;

import database.UserNotLoggedException;
import fsm.ActionsEnum;
import fsm.IFiniteStateMachine;
import fsm.ServerFSM;
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

        try {
            String requestString;
            BufferedReader inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintStream outStream = new PrintStream(clientSocket.getOutputStream());
            fsm.transit(ActionsEnum.RECEIVE.getAction());

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
                    fsm.transit(ActionsEnum.SEND.getAction());
                } catch (UserNotLoggedException e) {
                    log.warn(e);
                    ResponseBuilder.forRequest(request, outStream).deniedError(e.toString());
                    fsm.transit(ActionsEnum.SEND.getAction());
                }
            }

            log.info("Closing server thread");
            fsm.transit(ActionsEnum.CLOSE.getAction());
        } catch (IOException e) {
            log.error("Exception", e);
            fsm.transit(ActionsEnum.CLOSE.getAction());
        }
    }
}
