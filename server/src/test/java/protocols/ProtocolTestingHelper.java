package protocols;

import database.DatabaseManager;
import database.UserNotLoggedException;
import org.json.JSONObject;
import server.DatabaseContext;
import server.IContext;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.sql.SQLException;

public class ProtocolTestingHelper {

    public PrintStream outStream;
    public ByteArrayOutputStream os;
    public BufferedReader inStream;
    public JSONObject response;
    public IContext context;

    public ProtocolTestingHelper() {
        // 1. Creating context
        DatabaseManager dm = new DatabaseManager("database.db");
        context = new DatabaseContext(dm);
        dm.createTables();
        dm.linkDaos();

        // 2. Creating input and output streams
        inStream = new BufferedReader(new StringReader(""));
        os = new ByteArrayOutputStream();
        outStream = new PrintStream(os);
    }

    public static JSONObject executeProtocol(IProtocol protocol, JSONObject request) throws SQLException, UserNotLoggedException {
        ProtocolTestingHelper instance = new ProtocolTestingHelper();
        protocol.execute(instance.context, instance.inStream, instance.outStream, request);
        return new JSONObject(instance.os.toString());
    }
}
