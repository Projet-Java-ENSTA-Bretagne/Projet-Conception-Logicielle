package protocols;

import fsm.ActionsEnum;
import fsm.IFiniteStateMachine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import server.IContext;
import server.ResponseBuilder;

import java.io.*;

public class PingProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(PingProtocol.class);

    public static String requestName = "PING";

    public void execute(IContext ctx, PrintStream outStream, JSONObject request) {
        String returnValue = ResponseBuilder.forRequest(request, outStream).buildMessage(
                ResponseBuilder.StatusCode.OK,
                "PONG").toString();

        log.info("Answer from server: " + returnValue);
        outStream.println(returnValue);
    }
}
