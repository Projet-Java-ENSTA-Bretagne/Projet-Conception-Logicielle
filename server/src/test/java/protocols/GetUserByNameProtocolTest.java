package protocols;

import database.UserNotLoggedException;
import junit.framework.TestCase;
import org.json.JSONObject;
import java.sql.SQLException;

public class GetUserByNameProtocolTest extends TestCase {

    public void testUserNotExisting() throws SQLException, UserNotLoggedException {
        // Forging the request
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        request.put("command", GetUserByIDProtocol.requestName);
        args.put("user_name", "alexandrie");
        request.put("args", args);

        // Getting the response from the protocol
        JSONObject response = ProtocolTestingHelper.executeProtocol(new GetUserByNameProtocol(), request);

        // Checking the response data
        assertEquals(response.getString("status"), "NOT_FOUND");
        assertEquals(response.getJSONObject("data").getString("message"), "There is no user with the name: alexandrie");

        // Debug printing of response
        // System.out.println(response);
    }

    public void testExistingUser() throws SQLException, UserNotLoggedException {
        // Forging the request
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        request.put("command", GetUserByIDProtocol.requestName);
        args.put("user_name", "alexandre");
        request.put("args", args);

        // Getting the response from the protocol
        JSONObject response = ProtocolTestingHelper.executeProtocol(new GetUserByNameProtocol(), request);

        // Checking the response data
        assertEquals(response.getString("status"), "OK");
        assertEquals(response.getJSONObject("data").getJSONObject("user").getString("id"), "a5a1ed15-9aee-4127-b648-c0000dbc53ed");

        // Debug printing of response
        // System.out.println(response);
    }
}
