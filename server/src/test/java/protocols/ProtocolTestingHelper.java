package protocols;

import database.DatabaseManager;
import database.UserNotLoggedException;
import org.json.JSONObject;
import context.DatabaseContext;
import context.IContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

public class ProtocolTestingHelper {

    public PrintStream outStream;
    public ByteArrayOutputStream os;
    public JSONObject response;
    public IContext context;

    public ProtocolTestingHelper() {
        // 1. Creating context
        DatabaseManager dm = new DatabaseManager("database.db");
        context = new DatabaseContext(dm, null);
        dm.createTables();
        dm.linkDaos();

        // 2. Creating input and output streams
        os = new ByteArrayOutputStream();
        outStream = new PrintStream(os);
    }

    public static JSONObject executeProtocol(IProtocol protocol, JSONObject request) throws SQLException, UserNotLoggedException {
        ProtocolTestingHelper instance = new ProtocolTestingHelper();

        protocol.execute(instance.context, instance.outStream, request);
        return new JSONObject(instance.os.toString());
    }
}
