package protocols;

import database.UserNotLoggedException;
import junit.framework.TestCase;
import org.json.JSONObject;
import java.sql.SQLException;

public class PingProtocolTest extends TestCase {

    public void testPing() throws SQLException, UserNotLoggedException {
        // Forging the request
        JSONObject request = new JSONObject();
        request.put("command", "PING");

        // Getting the response from the protocol
        JSONObject response = ProtocolTestingHelper.executeProtocol(new PingProtocol(), request);

        // Checking the response data
        assertEquals(response.getString("status"), "OK");
        assertEquals(response.getJSONObject("data").getString("message"), "PONG");

        // Debug printing of response
        // System.out.println(response);
    }
}
