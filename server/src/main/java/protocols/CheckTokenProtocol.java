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

public class CheckTokenProtocol implements IProtocol {

    private final Logger log = LogManager.getLogger(CreateUserProtocol.class);

    public static String requestName = "checkToken";

    /**
     * Function allow to execute the command createUser send by a client.
     *
     * Example of request send by the client :
     *
     *
     * @param ctx : The context
     * @param inStream : The input stream of the client
     * @param outStream : The output stream of the server
     * @param request : The request of the client
     * @throws SQLException
     */
    public void execute(IContext ctx, BufferedReader inStream, PrintStream outStream, JSONObject request) throws SQLException {
        String token = request.getString("token");

        if (token.equals("")) {
            ResponseBuilder.forRequest(request, outStream).tokenError("Empty token.");
            return;
        }

        ResponseBuilder.forRequest(request, outStream).ok("Token valid");
    }
}
