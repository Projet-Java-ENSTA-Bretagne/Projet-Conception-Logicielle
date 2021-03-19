package protocols;

import database.DatabaseManager;
import junit.framework.TestCase;
import org.json.JSONObject;
import server.DatabaseContext;
import server.IContext;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

public class PingProtocolTest extends TestCase {

    public void testPing() {
        // 1. Creating new instance of protocol
        PingProtocol instance = new PingProtocol();

        // 2. Creating context
        DatabaseManager dm = new DatabaseManager("database.db");
        IContext ctx = new DatabaseContext(dm);

        // 3. Creating input and output streams
        BufferedReader inStream = new BufferedReader(new StringReader(""));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream outStream = new PrintStream(os);

        // 4. Creating the request to the protocol
        JSONObject request = new JSONObject();
        request.put("command", "PING");

        // 5. Executing the protocol and getting back the response
        instance.execute(ctx, inStream, outStream, request);
        JSONObject response = new JSONObject(os.toString());

        // 6. Checking the response data
        assertEquals(response.getString("status"), "OK");
        assertEquals(response.getJSONObject("data").getString("message"), "PONG");

        // Debug printing of response
        // System.out.println(response);
    }
}
