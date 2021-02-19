package protocols;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.IContext;

import java.io.*;

public class ExampleProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(ExampleProtocol.class);

    public void execute(IContext ctx, InputStream inStream, OutputStream outStream) {
        String inputRequest;
        BufferedReader is = new BufferedReader(new InputStreamReader(inStream));
        PrintStream os = new PrintStream(outStream);

        try {
            String valeurExpediee = "";

            inputRequest = is.readLine();
            if (inputRequest != null) {
                log.info("Received order : " + inputRequest);
                String[] args = inputRequest.split(" ");

                if (args[0].contentEquals("PING")) {
                    valeurExpediee = "PONG";
                    log.info("Answered to server: " + valeurExpediee);
                }
                os.println(valeurExpediee);
            }
        } catch (Exception e) {
            log.error("Catched an exception : " + e);
        }
    }
}
