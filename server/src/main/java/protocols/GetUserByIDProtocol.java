package protocols;

import com.j256.ormlite.dao.Dao;
import database.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import server.DatabaseContext;
import server.IContext;
import server.ResponseBuilder;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class GetUserByIDProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(GetUserByIDProtocol.class);

    public static String requestName = "getUserByID";

    public void execute(IContext ctx, BufferedReader inStream, PrintStream outStream, JSONObject request) throws SQLException {
        JSONObject data = request.getJSONObject("data");
        String id = data.getString("id");

        Dao<User, String> userDao = ((DatabaseContext) ctx).getDatabaseManager().getUserDao();

        // check if user exist
        List<User> matchingUsers = userDao.queryBuilder().
                where().in("id", id).query();

        // if there is already a user
        if (matchingUsers.size() == 0) {
            ResponseBuilder.sendDeniedError(outStream, request, "There is no user with the id : " + id);
            return;
        }
        User matchedUser = matchingUsers.get(0);

        JSONObject userJSON = new JSONObject();
        userJSON.put("id", matchedUser.getId());
        userJSON.put("name", matchedUser.getName());
        userJSON.put("password", matchedUser.getPassword());
        userJSON.put("role", matchedUser.getRole().toString());
        userJSON.put("blacklist", matchedUser.getBlacklist());

        // then tell the user everything went ok and sending the data
        outStream.println(ResponseBuilder.buildData(
                request, ResponseBuilder.StatusCode.OK, userJSON));
    }
}
