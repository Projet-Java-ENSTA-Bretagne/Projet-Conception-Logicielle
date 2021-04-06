package protocols;

import com.j256.ormlite.dao.Dao;
import database.SecurityManager;
import database.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONObject;
import context.DatabaseContext;
import context.IContext;
import server.ResponseBuilder;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 */
public class LoginProtocol implements IProtocol {

    /* The protocol logger instance */
    private final Logger log = LogManager.getLogger(LoginProtocol.class);

    public static String requestName = "login";

    /**
     * Function allow to execute the command login send by a client.
     *
     * Example of request send by the client :
     *
     *      {
     *          command: "login",
     *          "args" : {
     *              username : "cookiehacker",
     *              password: "myGreatPassword"
     *          }
     *      }
     *
     * @param ctx : The context
     * @param outStream :  The output stream of the server
     * @param request : The request of the client
     * @throws SQLException
     */
    public void execute(IContext ctx, PrintStream outStream, JSONObject request) throws SQLException {
        JSONObject data = request.getJSONObject("args");
        String username = data.getString("username");
        String password = data.getString("password");

        Dao<User, String> userDao = ((DatabaseContext) ctx).getDatabaseManager().getUserDao();

        // check if user exist
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            String sha512pass = new String(Hex.encode(hash));

            List<User> matchingUsers = userDao.queryBuilder().where().
                    eq("name", username).and().eq("password", sha512pass).
                    query();

            // if there is already a user
            if (matchingUsers.size() == 0) {
                ResponseBuilder.forRequest(request, outStream).deniedError("Wrong username/password combinaison");
                return;
            }

            User matchedUser = matchingUsers.get(0);
            SecurityManager.getInstance().setLoggedUser(matchedUser);
            // then tell the user everything went ok and sending the data
            JSONObject res = new JSONObject();
            res.put("message", "Successfully logged in.");
            res.put("user_id", matchedUser.getId());
            ResponseBuilder.forRequest(request, outStream).okWithData(res);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            ResponseBuilder.forRequest(request, outStream).serverError("Error when encoding password for verification");
        }

    }
}
