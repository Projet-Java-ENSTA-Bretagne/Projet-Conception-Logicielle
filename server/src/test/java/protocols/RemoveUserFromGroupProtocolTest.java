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

public class RemoveUserFromGroupProtocolTest extends TestCase {

    public void testRemoveUser() throws SQLException, UserNotLoggedException {
        // creating a group with default users
        ProtocolTestingHelper helper = new ProtocolTestingHelper();
        Dao<User, String> userDao = ((DatabaseContext) helper.context).getDatabaseManager().getUserDao();
        Dao<Group, String> groupDao = ((DatabaseContext) helper.context).getDatabaseManager().getGroupDao();

        User user1 = userDao.queryForId("45fabc9a-83f2-4069-83ad-529740094efc");
        User user2 = userDao.queryForId("d48cdb48-15e0-49e2-8304-e4e0589f6319");
        Group group = new Group(UUID.randomUUID().toString(), "testing_group", false, Date.from(Instant.now()), user1.getId() + ";" + user2.getId());
        groupDao.create(group);

        // Forging the request
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        args.put("group_id", group.getId());
        args.put("user_id", user1.getId());
        request.put("args", args);
        request.put("command", RemoveUserFromGroupProtocol.requestName);

        JSONObject response = ProtocolTestingHelper.executeProtocol(new RemoveUserFromGroupProtocol(), request);

        // Checking the response data
        System.out.println(response.toString());
        assertEquals(response.getString("status"),"OK");
        assertEquals(response.getJSONObject("data").getString("message"), "The user 45fabc9a-83f2-4069-83ad-529740094efc has been successfully removed from the group " + group.getId() + ".");

        group = groupDao.queryForId(group.getId());
        assertEquals(group.getMembers().split(";").length, 1);
        groupDao.delete(group);
    }

    public void testUserNotBelonging() throws SQLException, UserNotLoggedException {
        ProtocolTestingHelper helper = new ProtocolTestingHelper();
        Dao<User, String> userDao = ((DatabaseContext) helper.context).getDatabaseManager().getUserDao();
        Dao<Group, String> groupDao = ((DatabaseContext) helper.context).getDatabaseManager().getGroupDao();

        User user1 = userDao.queryForId("45fabc9a-83f2-4069-83ad-529740094efc");
        User user2 = userDao.queryForId("d48cdb48-15e0-49e2-8304-e4e0589f6319");
        Group group = new Group(UUID.randomUUID().toString(), "testing_group", false, Date.from(Instant.now()), user1.getId());
        groupDao.create(group);

        // Forging the request
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        args.put("group_id", group.getId());
        args.put("user_id", user2.getId());
        request.put("args", args);
        request.put("command", RemoveUserFromGroupProtocol.requestName);

        JSONObject response = ProtocolTestingHelper.executeProtocol(new RemoveUserFromGroupProtocol(), request);

        // Checking the response data
        System.out.println(response.toString());
        assertEquals(response.getString("status"),"DENIED");
        assertEquals(response.getJSONObject("data").getString("message"), "The user d48cdb48-15e0-49e2-8304-e4e0589f6319 doesn't belong to the group " + group.getId());

        groupDao.delete(group);
    }

    public void testUserNotFound() throws SQLException, UserNotLoggedException {
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
        args.put("user_id", "notfoundid");
        request.put("args", args);
        request.put("command", RemoveUserFromGroupProtocol.requestName);

        JSONObject response = ProtocolTestingHelper.executeProtocol(new RemoveUserFromGroupProtocol(), request);

        // Checking the response data
        System.out.println(response.toString());
        assertEquals(response.getString("status"),"NOT_FOUND");
        assertEquals(response.getJSONObject("data").getString("message"), "There is no user with the id: notfoundid");

        groupDao.delete(group);
    }

    public void testGroupNotFound() throws SQLException, UserNotLoggedException {
        ProtocolTestingHelper helper = new ProtocolTestingHelper();
        Dao<User, String> userDao = ((DatabaseContext) helper.context).getDatabaseManager().getUserDao();
        Dao<Group, String> groupDao = ((DatabaseContext) helper.context).getDatabaseManager().getGroupDao();

        User user1 = userDao.queryForId("45fabc9a-83f2-4069-83ad-529740094efc");
        Group group = new Group(UUID.randomUUID().toString(), "testing_group", false, Date.from(Instant.now()), user1.getId());
        groupDao.create(group);

        // Forging the request
        JSONObject request = new JSONObject();
        JSONObject args = new JSONObject();

        args.put("group_id", "notfoundid");
        args.put("user_id", "d48cdb48-15e0-49e2-8304-e4e0589f6319");
        request.put("args", args);
        request.put("command", RemoveUserFromGroupProtocol.requestName);

        JSONObject response = ProtocolTestingHelper.executeProtocol(new RemoveUserFromGroupProtocol(), request);

        // Checking the response data
        System.out.println(response.toString());
        assertEquals(response.getString("status"),"NOT_FOUND");
        assertEquals(response.getJSONObject("data").getString("message"), "There is no group with the id: notfoundid");

        groupDao.delete(group);
    }
}
