package protocols;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import server.IContext;
import server.ResponseBuilder;

import java.io.*;

public class PingProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(PingProtocol.class);

    public static String requestName = "PING";

    public void execute(IContext ctx, BufferedReader inStream, PrintStream outStream, JSONObject request) {
        String returnValue = ResponseBuilder.buildMessage(
                request,
                ResponseBuilder.StatusCode.OK,
                "PONG").toString();

        log.info("Answered to server: " + returnValue);
        outStream.println(returnValue);
    }
}
