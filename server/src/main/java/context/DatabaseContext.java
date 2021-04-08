package context;

import database.DatabaseManager;

import java.net.Socket;

public class DatabaseContext implements IContext {

    public Socket client;

    private DatabaseManager databaseManager;

    public DatabaseContext(DatabaseManager databaseManager, Socket client) {
        this.client = client;
        this.databaseManager = databaseManager;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }
}
