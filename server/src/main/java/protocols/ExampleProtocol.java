package protocols;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.IContext;

import java.io.*;

public class ExampleProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(ExampleProtocol.class);

    public static String requestName = "PING";

    public void execute(IContext ctx, BufferedReader inStream, PrintStream outStream) {
        String valeurExpediee = "PONG";
        log.info("Answered to server: " + valeurExpediee);
        outStream.println(valeurExpediee);
    }
}
