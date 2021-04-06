package protocols;

import database.UserNotLoggedException;
import junit.framework.TestCase;
import org.json.JSONObject;
import java.sql.SQLException;

public class GetUserGroupsProtocolTest extends TestCase {

    public void testUserNotExisting() throws SQLException, UserNotLoggedException {
        // Forging the request
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        request.put("command", GetUserGroupsProtocol.requestName);
        args.put("user_id", "0123456789abdcef");
        request.put("args", args);

        // Getting the response from the protocol
        JSONObject response = ProtocolTestingHelper.executeProtocol(new GetUserGroupsProtocol(), request);

        // Checking the response data
        assertEquals(response.getString("status"), "NOT_FOUND");
        assertEquals(response.getJSONObject("data").getString("message"), "There is no user with the id: 0123456789abdcef");

        // Debug printing of response
        // System.out.println(response);
    }

    public void testExistingUser() throws SQLException, UserNotLoggedException {
        // Forging the request
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        request.put("command", GetUserByIDProtocol.requestName);
        args.put("user_id", "a5a1ed15-9aee-4127-b648-c0000dbc53ed");
        request.put("args", args);

        // Getting the response from the protocol
        JSONObject response = ProtocolTestingHelper.executeProtocol(new GetUserGroupsProtocol(), request);

        // Checking the response data
        assertEquals(response.getString("status"), "OK");
        assertEquals(response.getJSONObject("data").getJSONArray("groups").length(), 1);

        // Debug printing of response
        System.out.println(response);
    }
}
