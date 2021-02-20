package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class DatabaseManager {

    public static Logger log = LogManager.getLogger(DatabaseManager.class);

    public static void createDatabase(String filename) {
        String url = "jdbc:sqlite:" + filename;

        try {
            // Trying to connect to the database
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
                        + " date text NOT NULL,\n"
                        + " content text NOT NULL,\n"
                        + " isRead text NOT NULL\n"
                        + ");";

        String groupSql =  "CREATE TABLE IF NOT EXISTS groups (\n"
                        + " id text PRIMARY KEY,\n"
                        + " name text,\n"
                        + " isPM integer NOT NULL,\n"
                        + " creationDate text NOT NULL,\n"
                        + " members text NOT NULL\n"
                        + ");";

        try {
            // Connecting to the database
            Connection conn = DriverManager.getConnection(url);
            Statement statement = conn.createStatement();

            // Executing the sql requests
            statement.execute(userSql);
            statement.execute(messageSql);
            statement.execute(groupSql);

        } catch (SQLException e) {
            log.error(e.getMessage());
            System.exit(1);
        }
    }

    public static void insertDummyData(String filename) {
        String url = "jdbc:sqlite:" + filename;

        try {
            // Trying to connect to the database
            Connection conn = DriverManager.getConnection(url);
            Statement statement = conn.createStatement();
            log.info("Inserting dummy data into DB");

            // adding dummy data
            log.debug("Adding user dummy data");
            statement.execute("INSERT INTO users (id, name, password, role, blacklist) VALUES\n" +
                                 " (0, 'alexandre', '1234', 'ROLE_ADMIN', ''), \n" +
                                 " (1, 'guillaume', '1234', 'ROLE_USER', '')");

            log.debug("Adding groups dummy data");
            statement.execute("INSERT INTO groups (id, name, isPM, creationDate, members) VALUES\n" +
                                 " (0, 'Les hackers', 0, '20-02-2021', '0;1')");

            log.debug("Adding messages dummy data");
            statement.execute("INSERT INTO messages (id, sender, groupID, date, content, isRead) VALUES\n" +
                                 " (0, 0, 0, '20-02-2021T08:00', 'Hello tout le monde :)', 'none')");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
