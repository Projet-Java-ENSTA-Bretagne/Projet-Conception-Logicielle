package protocols;

import database.UserNotLoggedException;
import junit.framework.TestCase;
import org.json.JSONObject;

import java.sql.SQLException;

public class CreateUserProtocolTest extends TestCase {
    public void testExistingUser() throws SQLException, UserNotLoggedException {
        // Forging the request
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        request.put("command", CreateUserProtocol.requestName);
        args.put("username", "test");
        args.put("password", "test");
        request.put("args", args);

        JSONObject response = ProtocolTestingHelper.executeProtocol(new CreateUserProtocol(), request);

        // Checking the response data
        System.out.println(response.toString());
        assertEquals(response.getString("status"),"DENIED");
        assertEquals(response.getJSONObject("data").getString("message"), "There is already a user with the name : test");
    }
}
