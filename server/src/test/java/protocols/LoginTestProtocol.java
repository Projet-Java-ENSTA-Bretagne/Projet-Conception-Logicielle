package protocols;

import database.UserNotLoggedException;
import junit.framework.TestCase;
import org.json.JSONObject;

import java.sql.SQLException;

public class LoginTestProtocol extends TestCase {
    public void testLoginSuccess() throws SQLException, UserNotLoggedException {
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        request.put("command", LoginProtocol.requestName);
        args.put("username", "cookiehacker");
        args.put("password", "cookie");
        request.put("args", args);

        // Getting the response from the protocol
        JSONObject response = ProtocolTestingHelper.executeProtocol(new LoginProtocol(), request);

        // Checking the response data
        assertEquals(response.getString("status"), "OK");
        assertEquals(response.getJSONObject("data").getString("message"), "Successfully logged in.");
    }

    public void testLoginError() throws SQLException, UserNotLoggedException {
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        request.put("command", LoginProtocol.requestName);
        args.put("username", "cookiehackeer");
        args.put("password", "cookie");
        request.put("args", args);

        // Getting the response from the protocol
        JSONObject response = ProtocolTestingHelper.executeProtocol(new LoginProtocol(), request);

        // Checking the response data
        assertEquals(response.getString("status"), "DENIED");
        assertEquals(response.getJSONObject("data").getString("message"), "Wrong username/password combinaison");
    }
}
