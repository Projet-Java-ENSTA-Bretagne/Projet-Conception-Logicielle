package protocols;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import server.IContext;

import java.io.BufferedReader;
import java.io.PrintStream;

public class RemoveUserFromGroupProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(RemoveUserFromGroupProtocol.class);

    public static String requestName = "removeUserFromGroup";

    public void execute(IContext ctx, BufferedReader inStream, PrintStream outStream, JSONObject request) {

    }
}
