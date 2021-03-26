package protocols;

import database.UserNotLoggedException;
import org.json.JSONObject;
import context.IContext;

import java.io.PrintStream;
import java.sql.SQLException;

public interface IProtocol {
    void execute(IContext ctx, PrintStream outStream, JSONObject request) throws SQLException, UserNotLoggedException;
}
