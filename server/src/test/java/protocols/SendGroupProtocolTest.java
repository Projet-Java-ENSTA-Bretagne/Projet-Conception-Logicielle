package protocols;

import com.j256.ormlite.dao.Dao;
import context.DatabaseContext;
import context.IContext;
import database.UserNotLoggedException;
import database.entities.Group;
import database.entities.User;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

public class SendGroupProtocolTest extends TestCase {

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

    public void testSendingMessage() throws SQLException, UserNotLoggedException {
        // creating a group with default users
        ProtocolTestingHelper helper = new ProtocolTestingHelper();
        Dao<User, String> userDao = ((DatabaseContext) helper.context).getDatabaseManager().getUserDao();
        Dao<Group, String> groupDao = ((DatabaseContext) helper.context).getDatabaseManager().getGroupDao();

        User user1 = userDao.queryForId("45fabc9a-83f2-4069-83ad-529740094efc");
        Group group = new Group(UUID.randomUUID().toString(), "testing_group", false, Date.from(Instant.now()), user1.getId());
        groupDao.create(group);

        // Forging the request
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        args.put("group_id", group.getId());
        args.put("message", "Un message de test.");
        request.put("args", args);
        request.put("command", SendGroupProtocol.requestName);

        loginUser(helper);
        JSONObject response = ProtocolTestingHelper.executeProtocol(new SendGroupProtocol(), request);

        // Checking the response data
        System.out.println(response.toString());
        assertEquals(response.getString("status"),"OK");
        assertEquals(response.getJSONObject("data").getString("message").split(":")[0], "Message successfully sent with id");

        // cleaning up
        groupDao.delete(group);
    }

    public void testSendingToNoGroup() throws SQLException, UserNotLoggedException {
        // creating a group with default users
        ProtocolTestingHelper helper = new ProtocolTestingHelper();
        Dao<User, String> userDao = ((DatabaseContext) helper.context).getDatabaseManager().getUserDao();
        Dao<Group, String> groupDao = ((DatabaseContext) helper.context).getDatabaseManager().getGroupDao();

        User user1 = userDao.queryForId("45fabc9a-83f2-4069-83ad-529740094efc");

        // Forging the request
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        args.put("group_id", "notfoundid");
        args.put("message", "Un message de test.");
        request.put("args", args);
        request.put("command", SendGroupProtocol.requestName);

        loginUser(helper);
        JSONObject response = ProtocolTestingHelper.executeProtocol(new SendGroupProtocol(), request);

        // Checking the response data
        System.out.println(response.toString());
        assertEquals(response.getString("status"),"NOT_FOUND");
        assertEquals(response.getJSONObject("data").getString("message"), "There is no group with the id: notfoundid");
    }
}
