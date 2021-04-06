package database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import database.entities.Group;
import database.entities.Message;
import database.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private final Logger log = LogManager.getLogger(DatabaseManager.class);

    private String dbURL;
    private ConnectionSource conn;
    private Dao<User, String> userDao;
    private Dao<Group, String> groupDao;
    private Dao<Message, String> messageDao;

    /**
     * Constructor of DatabaseManager
     * @param filename : the path of sqlite file
     */
    public DatabaseManager(String filename) {
        this.dbURL = "jdbc:sqlite:" + filename;

        try {
            this.conn = new JdbcConnectionSource(this.dbURL);
            this.createDatabase();
        } catch (SQLException e) {
            log.fatal("constructor - " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Function allow you to create a Database
     * @throws SQLException
     */
    private void createDatabase() throws SQLException {
        // Trying to connect to the database
        Connection conn = DriverManager.getConnection(this.dbURL);
        if (conn != null) {
            DatabaseMetaData meta = conn.getMetaData();
            log.debug("The driver name is " + meta.getDriverName());
            log.debug("A new database has been created at: " + this.dbURL);
        }
    }

    /**
     * Function allow you to create a table
     */
    public void createTables() {
        try {
            log.debug("Creating tables from ORMLite");
            TableUtils.createTable(conn, User.class);
            TableUtils.createTable(conn, Group.class);
            TableUtils.createTable(conn, Message.class);
        } catch (SQLException e) {
            log.error("createTables - " + e.getMessage());
        }
    }

    /**
     * Link the DAOs from DB to entities
     */
    public void linkDaos() {
        try {
            log.debug("Linking Daos from DB to entities");
            userDao = DaoManager.createDao(conn, User.class);
            groupDao = DaoManager.createDao(conn, Group.class);
            messageDao = DaoManager.createDao(conn, Message.class);
        } catch (SQLException e) {
            log.error("linkDaos - " + e.getMessage());
        }
    }

    public Dao<User, String> getUserDao() {
        return userDao;
    }

    public Dao<Group, String> getGroupDao() {
        return groupDao;
    }

    public Dao<Message, String> getMessageDao() {
        return messageDao;
    }
}
