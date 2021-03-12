package protocols;

import com.j256.ormlite.dao.Dao;
import database.SecurityManager;
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

public class LoginProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(LoginProtocol.class);

    public static String requestName = "login";

    public void execute(IContext ctx, BufferedReader inStream, PrintStream outStream, JSONObject request) throws SQLException {
        JSONObject data = request.getJSONObject("args");
        String username = data.getString("username");
        String password = data.getString("password");

        Dao<User, String> userDao = ((DatabaseContext) ctx).getDatabaseManager().getUserDao();

        // check if user exist
        List<User> matchingUsers = userDao.queryBuilder().where().
                eq("name", username).and().eq("password", password).
                query();

        // if there is already a user
        if (matchingUsers.size() == 0) {
            ResponseBuilder.sendDeniedError(outStream, request, "Wrong username/password combinaison");
            return;
        }

        User matchedUser = matchingUsers.get(0);
        SecurityManager.getInstance().setLoggedUser(matchedUser);
        // then tell the user everything went ok and sending the data
        outStream.println(ResponseBuilder.buildMessage(
                request, ResponseBuilder.StatusCode.OK, "Successfully logged in."));
    }
}
