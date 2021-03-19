package protocols;

import database.UserNotLoggedException;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;

public class CreateGroupProtocolTest extends TestCase {
    public void testExistingGroup() throws SQLException, UserNotLoggedException {
        // Forging the request
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();
        JSONArray users = new JSONArray();

        users.put("45fabc9a-83f2-4069-83ad-529740094efc");
        users.put("d48cdb48-15e0-49e2-8304-e4e0589f6319");

        request.put("command", CreateGroupProtocol.requestName);
        args.put("group_name", "cookieGroup");
        args.put("user_id", users);
        request.put("args", args);

        JSONObject response = ProtocolTestingHelper.executeProtocol(new CreateGroupProtocol(), request);

        // Checking the response data
        System.out.println(response.toString());
        assertEquals(response.getString("status"),"DENIED");
        assertEquals(response.getJSONObject("data").getString("message"), "There is already a group with the name : cookieGroup");
    }
}
