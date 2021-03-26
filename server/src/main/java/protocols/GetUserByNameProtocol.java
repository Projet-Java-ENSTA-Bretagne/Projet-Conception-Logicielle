package protocols;

import com.j256.ormlite.dao.Dao;
import database.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import context.DatabaseContext;
import context.IContext;
import server.ResponseBuilder;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;

public class GetUserByNameProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(GetUserByNameProtocol.class);

    public static String requestName = "getUserByName";

    /**
     * Function allow to execute the command getUserByName send by a client.
     *
     * Example of request send by the client :
     *
     * {
     *     "command" : "getUserByName",
     *     "args" : {
     *         "user_name" : "cookiehacker"
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
        String name = data.getString("user_name");

        Dao<User, String> userDao = ((DatabaseContext) ctx).getDatabaseManager().getUserDao();

        // check if user exist
        List<User> matchingUsers = userDao.queryBuilder().
                where().in("name", name).query();

        // if there is already a user
        if (matchingUsers.size() == 0) {
            ResponseBuilder.forRequest(request, outStream).notFoundError("There is no user with the name: " + name);
            return;
        }

        User matchedUser = matchingUsers.get(0);
        // then tell the user everything went ok and sending the data
        JSONObject response = new JSONObject();
        response.put("user", matchedUser.toJSON());
        response.put("message", "Data for user: " + name);
        ResponseBuilder.forRequest(request, outStream).okWithData(response);
    }
}
