import database.DatabaseManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import protocols.*;
import server.*;

import java.sql.SQLException;
import java.util.HashMap;

public class MainServer {

    public static void main(String[] args) throws SQLException {
        // Setting logging level
        Configurator.setRootLevel(Level.DEBUG);

        // Getting configuration
        ConfigurationManagement.getInstance().fromPath("config.json");
        ServerConfiguration serverConfig = ConfigurationManagement.getInstance().getServerConfiguration();

        // Setting up database
        DatabaseManager db = new DatabaseManager("database.db");
        db.createTables();
        db.linkDaos();

        // User alex = new User("0", "alexandre", "1234", User.Role.ROLE_ADMIN, "0;1");
        // db.getUserDao().create(alex);
        // System.out.println(db.getUserDao().queryForId("0").getName());

        // Setup protocols
        HashMap<String, IProtocol> protocols = new HashMap<>();
        protocols.put(PingProtocol.requestName,                 new PingProtocol());
        protocols.put(LoginProtocol.requestName,                new LoginProtocol());
        protocols.put(CreateGroupProtocol.requestName,          new CreateGroupProtocol());
        protocols.put(SendGroupProtocol.requestName,            new SendGroupProtocol());
        protocols.put(GetGroupMsgProtocol.requestName,          new GetGroupMsgProtocol());
        protocols.put(GetUserByIDProtocol.requestName,          new GetUserByIDProtocol());
        protocols.put(GetUserByNameProtocol.requestName,        new GetUserByNameProtocol());
        protocols.put(CreateUserProtocol.requestName,           new CreateUserProtocol());
        protocols.put(RemoveUserFromGroupProtocol.requestName,  new RemoveUserFromGroupProtocol());
        protocols.put(BlockUserProtocol.requestName,            new BlockUserProtocol());
        protocols.put(CheckTokenProtocol.requestName,           new CheckTokenProtocol());
        protocols.put(GetUserGroupsProtocol.requestName,        new GetUserGroupsProtocol());

        TCPServer server = new TCPServer(new DatabaseContext(db), protocols, serverConfig.getPort());
        server.start();
    }

}
