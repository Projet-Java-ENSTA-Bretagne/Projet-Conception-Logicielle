package protocols;

import com.j256.ormlite.dao.Dao;
import database.entities.Group;
import database.entities.User;
import fsm.IFiniteStateMachine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import server.DatabaseContext;
import server.IContext;
import server.ResponseBuilder;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;

public class GetUserGroupsProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(GetUserByNameProtocol.class);

    public static String requestName = "getUserGroups";

    /**
     * Function allow to execute the command getUserByName send by a client.
     * @param ctx : The context
     * @param outStream : The output stream of the server
     * @param request : The request of the client
     * @throws SQLException
     */
    public void execute(IContext ctx, PrintStream outStream, JSONObject request) throws SQLException {
        JSONObject data = request.getJSONObject("args");
        String id = data.getString("user_id");

        Dao<User, String> userDao = ((DatabaseContext) ctx).getDatabaseManager().getUserDao();

        // check if user exist
        List<User> matchingUsers = userDao.queryBuilder().
                where().eq("id", id).query();

        // if there is no user
        if (matchingUsers.size() == 0) {
            ResponseBuilder.forRequest(request, outStream).notFoundError("There is no user with the id: " + id);
            return;
        }

        // getting the list of groups
        Dao<Group, String> groupDao = ((DatabaseContext) ctx).getDatabaseManager().getGroupDao();
        List<Group> matchingGroups = groupDao.queryBuilder().
                where().like("members", "%" + id + "%").query();

        JSONObject resData = new JSONObject();
        JSONArray groupsJSON = new JSONArray();
        for (Group g : matchingGroups) {
            groupsJSON.put(g.toJSON());
        }
        resData.put("groups", groupsJSON);


        // then tell the user everything went ok and sending the data
        ResponseBuilder.forRequest(request, outStream).okWithData(resData);
    }
}
