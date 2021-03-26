package protocols;

import database.SecurityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import context.IContext;
import server.ResponseBuilder;

import java.io.PrintStream;
import java.sql.SQLException;

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
     * @param outStream : The output stream of the server
     * @param request : The request of the client
     * @throws SQLException
     */
    public void execute(IContext ctx, PrintStream outStream, JSONObject request) throws SQLException {
        String token = request.getString("token");

        if (token.equals("")) {
            ResponseBuilder.forRequest(request, outStream).tokenError("Empty token.");
            return;
        }

        if (!SecurityManager.getInstance().isTokenValid(token)) {
            ResponseBuilder.forRequest(request, outStream).tokenError("Token not valid");
            return;
        }

        JSONObject data = new JSONObject();
        data.put("message", "The token is valid");
        data.put("token", token);
        ResponseBuilder.forRequest(request, outStream).okWithData(data);
    }
}
