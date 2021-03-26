package protocols;

import database.UserNotLoggedException;
import fsm.IFiniteStateMachine;
import org.json.JSONObject;
import server.IContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

public interface IProtocol {
    void execute(IContext ctx, PrintStream outStream, JSONObject request) throws SQLException, UserNotLoggedException;
}
