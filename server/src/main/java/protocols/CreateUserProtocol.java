package protocols;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import database.DatabaseManager;
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

public class CreateUserProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(CreateUserProtocol.class);

    public static String requestName = "createUser";

    public void execute(IContext ctx, BufferedReader inStream, PrintStream outStream, JSONObject request) throws SQLException {
        JSONObject data = request.getJSONObject("data");
        String username = data.getString("username");
        String password = data.getString("password");
        User.Role role = User.Role.valueOf(data.getString("role"));

        Dao<User, String> userDao = ((DatabaseContext) ctx).getDatabaseManager().getUserDao();

        // check if user exist
        List<User> matchingUsers = userDao.queryBuilder().
                    where().in("name", username).query();

        // if there is already a user
        if (matchingUsers.size() > 0) {
            ResponseBuilder.sendDeniedError(outStream, request, "There is already a user with the name : " + username);
            return;
        }

        // then we create the new user
        User newUser = new User(UUID.randomUUID().toString(), username, password, role, "");
        userDao.create(newUser);

        // then tell the user everything went ok
        outStream.println(ResponseBuilder.buildMessage(request, ResponseBuilder.StatusCode.OK, "User successfully created"));
    }
}
