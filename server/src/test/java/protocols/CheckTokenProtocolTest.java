package protocols;

import com.j256.ormlite.dao.Dao;
import context.DatabaseContext;
import database.SecurityManager;
import database.UserNotLoggedException;
import database.entities.User;
import junit.framework.TestCase;
import org.json.JSONObject;

import java.sql.SQLException;

public class CheckTokenProtocolTest extends TestCase {

    public void testInvalidToken() throws SQLException, UserNotLoggedException {
        // Forging the request
        JSONObject request = new JSONObject();

        request.put("token", "invalid_token");
        request.put("command", CheckTokenProtocol.requestName);

        JSONObject response = ProtocolTestingHelper.executeProtocol(new CheckTokenProtocol(), request);

        // Checking the response data
        System.out.println(response.toString());
        assertEquals(response.getString("status"),"TOKEN_ERROR");
        assertEquals(response.getJSONObject("data").getString("message"), "Token not valid");
    }

    public void testValidToken() throws SQLException, UserNotLoggedException {
        ProtocolTestingHelper helper = new ProtocolTestingHelper();
        Dao<User, String> userDao = ((DatabaseContext) helper.context).getDatabaseManager().getUserDao();
        User test = userDao.queryForId("8d34a545-3a45-447d-a67c-d6653b5ab773");
        String token = SecurityManager.getInstance().createJWT(test);

        // Forging the request
        JSONObject request = new JSONObject();

        request.put("token", token);
        request.put("command", CheckTokenProtocol.requestName);

        JSONObject response = ProtocolTestingHelper.executeProtocol(new CheckTokenProtocol(), request);

        // Checking the response data
        System.out.println(response.toString());
        assertEquals(response.getString("status"),"OK");
        assertEquals(response.getJSONObject("data").getString("message"), "The token is valid");
    }
}
