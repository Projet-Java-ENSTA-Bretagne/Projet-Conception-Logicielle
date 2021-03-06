package protocols;

import com.j256.ormlite.dao.Dao;
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
import java.util.UUID;

public class CreateUserProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(CreateUserProtocol.class);

    public static String requestName = "createUser";

    /**
     * Function allow to execute the command createUser send by a client.
     *
     * Example of request send by the client :
     *
     * {
     *   "command" : "createUser",
     *   "args" : {
     *     "username" : "john",
     *     "password" : "dough"
     *   }
     * }
     *
     * @param ctx : The context
     * @param outStream : The output stream of the server
     * @param request : The request of the client
     * @throws SQLException
     */
    public void execute(IContext ctx, PrintStream outStream, JSONObject request) throws SQLException {
        JSONObject data = request.getJSONObject("args");
        String username = data.getString("username");
        String password = data.getString("password");
        User.Role role = User.Role.ROLE_USER;

        Dao<User, String> userDao = ((DatabaseContext) ctx).getDatabaseManager().getUserDao();

        // check if user exist
        List<User> matchingUsers = userDao.queryBuilder().
                    where().in("name", username).query();

        // if there is already a user
        if (matchingUsers.size() > 0) {
            ResponseBuilder.forRequest(request, outStream).deniedError("There is already a user with the name : " + username);
            return;
        }

        // checking of double uuid
        String uuid = UUID.randomUUID().toString();

        boolean doubleuuid = true;
        while (doubleuuid)
        {
            matchingUsers = userDao.queryBuilder().
                    where().in("id", uuid).query();
            if(matchingUsers.size() == 0) doubleuuid = false;
            else uuid = UUID.randomUUID().toString();
        }



        // then we create the new user
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            String sha512pass = new String(Hex.encode(hash));

            User newUser = new User(uuid, username, sha512pass, role, "");
            userDao.create(newUser);

            // then tell the user everything went ok
            ResponseBuilder.forRequest(request, outStream).ok("User successfully created");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            ResponseBuilder.forRequest(request, outStream).serverError("Encoding error in SHA-512");
        }


    }
}
