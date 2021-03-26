package protocols;

import com.j256.ormlite.dao.Dao;
import database.entities.Group;
import database.entities.Message;
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

public class GetGroupMsgProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(GetGroupMsgProtocol.class);

    public static String requestName = "getGroupMsg";

    /**
     * Function allow to execute the command createUser send by a client.
     *
     * Example of request send by the client :
     *
     * {
     *     "command" : "getGroupMsg",
     *     "args" : {
     *         "group_id" : "c9d36c97-4e3d-4b0c-9a78-50f76e7f5589",
     *         "index" : 0,
     *         "limit" : 5
     *     }
     * }
     *
     * @param ctx : The context
     * @param outStream : The output stream of the server
     * @param request : The request of the client
     * @throws SQLException
     */
    public void execute(IContext ctx, PrintStream outStream, JSONObject request) throws SQLException {
        JSONObject data = request.getJSONObject("args");
        String group_id  = data.getString("group_id");
        int index = data.getInt("index");
        int limit = data.getInt("limit");

        Dao<Group,String> groupDao = ((DatabaseContext) ctx).getDatabaseManager().getGroupDao();

        List<Group> matchingGroups = groupDao.queryBuilder().where().in("id", group_id).query();

        if(matchingGroups.size() == 0)
        {
            ResponseBuilder.forRequest(request, outStream).notFoundError("There is no group with the ID: " + group_id);
            return;
        }

        Dao<Message,String> messageDao = ((DatabaseContext) ctx).getDatabaseManager().getMessageDao();

        List<Message> matchingMessages = messageDao.queryBuilder().where().in("groupID", group_id).query();

        int sizeneed = ((index+limit)+1);

        List<Message> showMessage;

        if(matchingMessages.size() < sizeneed) {

            showMessage = matchingMessages.subList(index, sizeneed - (sizeneed-matchingMessages.size()));
        }
        else
        {
            showMessage = matchingMessages.subList(index, index+limit);
        }

        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();

        for(Message m : showMessage)
        {
            array.put(m.toJSON());
        }

        jsonObject.put("msg",array);

        System.out.println(jsonObject.toString(2));

        ResponseBuilder.forRequest(request, outStream).okWithData(jsonObject);
    }
}
