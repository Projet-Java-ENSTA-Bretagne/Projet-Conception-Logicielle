package protocols;

import com.j256.ormlite.dao.Dao;
import database.SecurityManager;
import database.UserNotLoggedException;
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

public class BlockUserProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(BlockUserProtocol.class);

    public static String requestName = "blockUser";

    /**
     * Function allow to execute the command blockUser send by a client.
     *
     * Example of request send by the client :
     *
     *      {
     *          command: "blockUser",
     *          args: {
     *              id: "3ce9300b-e66c-4fe0-b2bf-756ebf07d4e4"
     *          }
     *      }
     *
     * @param ctx: The context
     * @param outStream: The output stream of the server
     * @param request: The request of the client
     * @throws SQLException
     * @throws UserNotLoggedException
     */
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
        User currentUser = SecurityManager.getInstance().getLoggedUser(((DatabaseContext) ctx).client);
        if (currentUser.getBlacklist().split(";").length == 0) {
            currentUser.setBlacklist(id);
        } else {
            currentUser.setBlacklist(currentUser.getBlacklist() + ";" + id);
        }
        userDao.update(currentUser);

        // say to the user that everything went fine
        ResponseBuilder.forRequest(request, outStream).ok("The user " + id + " has been successfully blocked.");
    }
}
