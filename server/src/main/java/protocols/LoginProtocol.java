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

/**
 *
 */
public class LoginProtocol implements IProtocol {

    /* The protocol logger instance */
    private final Logger log = LogManager.getLogger(LoginProtocol.class);

    public static String requestName = "login";

    /**
     *
     * @param ctx Le contexte du serveur
     * @param inStream Le stream d'entrée client
     * @param outStream Le stream de sortie vers le client
     * @param request La requête émise par le client
     * @throws SQLException Retourne une erreur sql si il y a un soucis avec la base de données
     */
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
            ResponseBuilder.forRequest(request, outStream).deniedError("Wrong username/password combinaison");
            return;
        }

        User matchedUser = matchingUsers.get(0);
        SecurityManager.getInstance().setLoggedUser(matchedUser);
        // then tell the user everything went ok and sending the data
        ResponseBuilder.forRequest(request, outStream).ok("Successfully logged in.");
    }
}
