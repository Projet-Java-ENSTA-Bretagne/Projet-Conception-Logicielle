package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DatabaseManager {

    public static Logger log = LogManager.getLogger(DatabaseManager.class);

    public static void createDatabase(String filename) {
        String url = "jdbc:sqlite:" + filename;

        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                log.debug("The driver name is " + meta.getDriverName());
                log.info("A new database has been created at: " + filename);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            System.exit(1);
        }
    }

    public static void createTables(String filename) {
        String url = "jdbc:sqlite:" + filename;

        String userSql =  "CREATE TABLE IF NOT EXISTS users (\n"
                        + " id text PRIMARY KEY,\n"
                        + " name text NOT NULL,\n"
                        + " password text NOT NULL,\n"
                        + " role text NOT NULL,\n"
                        + " blacklist text\n"
                        + ");";

        String messageSql =  "CREATE TABLE IF NOT EXISTS messages (\n"
                        + " id text PRIMARY KEY,\n"
                        + " sender text NOT NULL,\n"
                        + " groupID text NOT NULL,\n"
                        + " content text NOT NULL,\n"
                        + " isRead integer NOT NULL\n"
                        + ");";

        String groupSql =  "CREATE TABLE IF NOT EXISTS groups (\n"
                        + " id text PRIMARY KEY,\n"
                        + " name text,\n"
                        + " isPM integer NOT NULL,\n"
                        + " creationDate text NOT NULL,\n"
                        + " members text NOT NULL\n"
                        + ");";

        try {
            Connection conn = DriverManager.getConnection(url);
            Statement statement = conn.createStatement();


            statement.execute(userSql);
            statement.execute(messageSql);
            statement.execute(groupSql);

        } catch (SQLException e) {
            log.error(e.getMessage());
            System.exit(1);
        }
    }
}
