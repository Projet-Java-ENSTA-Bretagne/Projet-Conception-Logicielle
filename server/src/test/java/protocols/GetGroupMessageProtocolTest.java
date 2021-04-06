package protocols;

import com.j256.ormlite.dao.Dao;
import context.DatabaseContext;
import database.UserNotLoggedException;
import database.entities.Group;
import database.entities.Message;
import database.entities.User;
import junit.framework.TestCase;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

public class GetGroupMessageProtocolTest extends TestCase {

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

    public void testGetGroupMessages() throws SQLException, UserNotLoggedException {
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
        ProtocolTestingHelper.executeProtocol(new SendGroupProtocol(), request);

        // Forging the request
        request = new JSONObject();
        args = new JSONObject();

        args.put("group_id", group.getId());
        args.put("index", 0);
        args.put("limit", 5);
        request.put("args", args);
        request.put("command", GetGroupMsgProtocol.requestName);

        JSONObject response = ProtocolTestingHelper.executeProtocol(new GetGroupMsgProtocol(), request);

        System.out.println(response.toString());
        assertEquals(response.getString("status"),"OK");

        JSONObject message = response.getJSONObject("data").getJSONArray("msg").getJSONObject(0);
        assertEquals(message.getString("content"), "Un message de test.");
        assertEquals(message.getString("sender_id"), "8d34a545-3a45-447d-a67c-d6653b5ab773");

        // cleaning up
        groupDao.delete(group);
    }

    public void testNoGroup() throws SQLException, UserNotLoggedException {
        ProtocolTestingHelper helper = new ProtocolTestingHelper();
        loginUser(helper);

        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();
        args.put("group_id", "notfoundid");
        args.put("index", 0);
        args.put("limit", 5);
        request.put("args", args);
        request.put("command", GetGroupMsgProtocol.requestName);

        JSONObject response = ProtocolTestingHelper.executeProtocol(new GetGroupMsgProtocol(), request);

        System.out.println(response.toString());
        assertEquals(response.getString("status"),"NOT_FOUND");
        assertEquals(response.getJSONObject("data").getString("message"), "There is no group with the ID: notfoundid");
    }
}
