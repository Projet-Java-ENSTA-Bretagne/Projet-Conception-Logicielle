package protocols;

import com.j256.ormlite.dao.Dao;
import database.entities.Group;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CreateGroupProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(CreateGroupProtocol.class);

    public static String requestName = "createGroup";

    /**
     * Function allow to execute the command createGroup send by a client.
     *
     * Example of request send by the client :
     *
     *      {
     *          command: createGroup,
     *          args : {
     *          group_name : cookieGroup,
     *          user_id : [
     *                  45fabc9a-83f2-4069-83ad-529740094efc,
     *                  d48cdb48-15e0-49e2-8304-e4e0589f6319
     *          ]
     *          }
     *      }
     *
     * @param ctx : The context
     * @param inStream : The input stream of the client
     * @param outStream : The output stream of the server
     * @param request : The request of the client
     * @throws SQLException
     */
    public void execute(IContext ctx, BufferedReader inStream, PrintStream outStream, JSONObject request) throws SQLException {
        JSONObject data = request.getJSONObject("args");
        String groupname = data.getString("group_name");
        JSONArray userid = data.getJSONArray("user_id");

        Dao<Group, String> groupDao = ((DatabaseContext) ctx).getDatabaseManager().getGroupDao();

        // check if group exist
        List<Group> matchingGroups = groupDao.queryBuilder().where().in("name", groupname).query();

        // if there is already a group
        if(matchingGroups.size() > 0) {
            ResponseBuilder.sendDeniedError(outStream,request,"There is already a group with the name : " + groupname);
            return;
        }

        // then we create the new group
        Group newGroup = new Group(UUID.randomUUID().toString(),groupname, userid.length() <= 2, new Date(), userid.join(","));
        groupDao.create(newGroup);

        // then tell the user everything went ok
        outStream.println(ResponseBuilder.buildMessage(request, ResponseBuilder.StatusCode.OK, "Group successfully created"));

    }
}
