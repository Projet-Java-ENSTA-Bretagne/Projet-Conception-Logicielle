package protocols;

import com.j256.ormlite.dao.Dao;
import database.entities.Group;
import database.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import context.DatabaseContext;
import context.IContext;
import server.ResponseBuilder;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveUserFromGroupProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(RemoveUserFromGroupProtocol.class);

    public static String requestName = "removeUserFromGroup";

    /**
     * Function allow to execute the command removeUserFromGroup send by a client.
     *
     * Example of request send by the client :
     *
     *      {
     *          command: "removeUserFromGroup",
     *          args: {
     *              group_id: "c9d36c97-4e3d-4b0c-9a78-50f76e7f5589",
     *              user_id: "3ce9300b-e66c-4fe0-b2bf-756ebf07d4e4"
     *          }
     *      }
     *
     * @param ctx: The context
     * @param outStream : The output stream of the server
     * @param request : The request of the client
     * @throws SQLException
     */
    public void execute(IContext ctx, PrintStream outStream, JSONObject request) throws SQLException {
        JSONObject data = request.getJSONObject("args");
        String groupId = data.getString("group_id");
        String userId = data.getString("user_id");

        Dao<User, String> userDao = ((DatabaseContext) ctx).getDatabaseManager().getUserDao();
        Dao<Group, String> groupDao = ((DatabaseContext) ctx).getDatabaseManager().getGroupDao();

        // check if the user and the group exist
        List<User> matchingUsers = userDao.queryBuilder().
                where().eq("id", userId).query();
        List<Group> matchingGroups = groupDao.queryBuilder().
                where().eq("id", groupId).query();

        // Creating the response builder
        ResponseBuilder resBuilder = ResponseBuilder.forRequest(request, outStream);

        // if the user doesn't exist
        if (matchingUsers.size() == 0) {
            resBuilder.notFoundError("There is no user with the id: " + userId);
            return;
        }
        if (matchingGroups.size() == 0) {
            resBuilder.notFoundError("There is no group with the id: " + groupId);
            return;
        }

        // getting the group
        Group groupToChange = matchingGroups.get(0);
        String[] membersArr = groupToChange.getMembers().split(";");
        List<String> membersList = new ArrayList<String>(Arrays.asList(membersArr));
        boolean removed = membersList.remove(userId);

        // if the user wasn't in the group
        if (!removed) {
            resBuilder.deniedError("The user " + userId + " doesn't belong to the group " + groupId);
            return;
        }

        // else we update the group
        groupToChange.setMembers(String.join(";", membersList));

        // say to the user that everything went fine
        resBuilder.ok("The user " + userId + " has been successfully removed from the group " + groupId + ".");
    }
}
