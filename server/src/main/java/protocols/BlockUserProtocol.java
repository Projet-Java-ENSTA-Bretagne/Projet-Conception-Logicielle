package protocols;

import com.j256.ormlite.dao.Dao;
import database.SecurityManager;
import database.UserNotLoggedException;
import database.entities.User;
import fsm.ActionsEnum;
import fsm.IFiniteStateMachine;
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

public class BlockUserProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(BlockUserProtocol.class);

    public static String requestName = "blockUser";

    public void execute(IContext ctx, PrintStream outStream, JSONObject request) throws SQLException, UserNotLoggedException {
        JSONObject data = request.getJSONObject("args");
        String id = data.getString("id");

        Dao<User, String> userDao = ((DatabaseContext) ctx).getDatabaseManager().getUserDao();

        // check if user exist
        List<User> matchingUsers = userDao.queryBuilder().
                where().eq("id", id).query();

        // if the user doesn't exist
        if (matchingUsers.size() == 0) {
            ResponseBuilder.forRequest(request, outStream).notFoundError("There is no user with the id: " + id);
            return;
        }

        // updating current user blacklist
        User currentUser = SecurityManager.getInstance().getLoggedUser();
        currentUser.setBlacklist(currentUser + ";" + id);
        userDao.update(currentUser);

        // say to the user that everything went fine
        ResponseBuilder.forRequest(request, outStream).ok("The user " + id + " has been successfully blocked.");
    }
}
