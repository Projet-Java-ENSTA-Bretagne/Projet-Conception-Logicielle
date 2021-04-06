package protocols;

import com.j256.ormlite.dao.Dao;
import context.DatabaseContext;
import database.DatabaseManager;
import database.UserNotLoggedException;
import database.entities.User;
import junit.framework.TestCase;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class BlockUserProtocolTest extends TestCase {

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

    public void testBlockingUser() throws SQLException, UserNotLoggedException {
        ProtocolTestingHelper helper = new ProtocolTestingHelper();
        Dao<User, String> userDao = ((DatabaseContext) helper.context).getDatabaseManager().getUserDao();

        User toBlock = userDao.queryForId("a5a1ed15-9aee-4127-b648-c0000dbc53ed");
        User testUser = userDao.queryForId("8d34a545-3a45-447d-a67c-d6653b5ab773");

        String initialBlackList = testUser.getBlacklist();

        // Forging the request
        // Forging the request
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        args.put("id", toBlock.getId());
        request.put("args", args);
        request.put("command", BlockUserProtocol.requestName);

        loginUser(helper);
        JSONObject response = ProtocolTestingHelper.executeProtocol(new BlockUserProtocol(), request);

        // Checking the response data
        System.out.println(response.toString());
        assertEquals(response.getString("status"),"OK");
        assertEquals(response.getJSONObject("data").getString("message"), "The user a5a1ed15-9aee-4127-b648-c0000dbc53ed has been successfully blocked.");

        testUser = userDao.queryForId("8d34a545-3a45-447d-a67c-d6653b5ab773");
        assertEquals(testUser.getBlacklist(), initialBlackList + ";" + toBlock.getId());

        String[] users = testUser.getBlacklist().split(";");
        users = Arrays.copyOfRange(users, 0, users.length - 1);
        testUser.setBlacklist(String.join(";", users));
        userDao.update(testUser);
    }
}
