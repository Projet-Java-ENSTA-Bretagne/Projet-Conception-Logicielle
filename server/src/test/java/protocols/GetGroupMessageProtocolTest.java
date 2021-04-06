package protocols;

import database.UserNotLoggedException;
import org.json.JSONObject;

import java.sql.SQLException;

public class GetGroupMessageProtocolTest {

    private void loginUser(ProtocolTestingHelper helper) throws SQLException, UserNotLoggedException {
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        request.put("command", LoginProtocol.requestName);
        args.put("username", "test");
        args.put("password", "test");
        request.put("args", args);

        // Getting the response from the protocol
        JSONObject response = ProtocolTestingHelper.executeProtocol(new LoginProtocol(), request);
        System.out.println(response.toString());
    }


}
