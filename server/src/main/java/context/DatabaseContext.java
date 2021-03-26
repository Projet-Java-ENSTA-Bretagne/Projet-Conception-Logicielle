package context;

import database.DatabaseManager;

public class DatabaseContext implements IContext {

    private DatabaseManager databaseManager;

    public DatabaseContext(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }
}
