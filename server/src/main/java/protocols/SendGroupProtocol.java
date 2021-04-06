package protocols;

import com.j256.ormlite.dao.Dao;
import database.SecurityManager;
import database.UserNotLoggedException;
import database.entities.Group;
import database.entities.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import context.DatabaseContext;
import context.IContext;
import server.ResponseBuilder;

import java.io.PrintStream;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SendGroupProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(SendGroupProtocol.class);

    public static String requestName = "sendPM";

    /**
     * Function allow to execute the command sendPM send by a client.
     *
     * Example of request send by the client :
     *
     *      {
     *          command : "sendPM",
     *          args : {
     *              group_id: "c9d36c97-4e3d-4b0c-9a78-50f76e7f5589",
     *              message: "I love cookies, and you ?"
     *          }
     *      }
     *
     * @param ctx
     * @param outStream
     * @param request
     * @throws SQLException
     * @throws UserNotLoggedException
     */
    public void execute(IContext ctx, PrintStream outStream, JSONObject request) throws SQLException, UserNotLoggedException {
        JSONObject data = request.getJSONObject("args");
        String groupId = data.getString("group_id");
        String message = data.getString("message");

        Dao<Group, String> groupDao = ((DatabaseContext) ctx).getDatabaseManager().getGroupDao();
        List<Group> matchingGroups = groupDao.queryBuilder().
                where().eq("id", groupId).query();

        // check that the group already exists
        if (matchingGroups.size() == 0) {
            ResponseBuilder.forRequest(request, outStream).deniedError("There is no group with the id: " + groupId);
            return;
        }

        // putting the message into the database
        Dao<Message, String> messagesDao = ((DatabaseContext) ctx).getDatabaseManager().getMessageDao();
        Message newMsg = new Message(
                UUID.randomUUID().toString(),
                SecurityManager.getInstance().getLoggedUser().getId(),
                groupId,
                Date.from(Instant.now()),
                message,
                false
        );
        messagesDao.create(newMsg);

        // telling the user that everything went fine
        ResponseBuilder.forRequest(request, outStream).ok("Message successfully sent with id: " + newMsg.getId());
    }
}
