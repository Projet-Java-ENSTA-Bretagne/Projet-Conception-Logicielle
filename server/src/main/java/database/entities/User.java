package database.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.json.JSONObject;

@DatabaseTable(tableName = "users")
public class User {

    /**
     * The differennt roles of a User :
     *  - ROLE_USER : For a lambda user
     *  - ROLE_ADMIN : For the administrator
     */
    public enum Role {
        ROLE_USER,
        ROLE_ADMIN
    }

    /**
     * The User ID
     */
    @DatabaseField(id = true)
    private String id;

    /**
     * The user name
     */
    @DatabaseField(canBeNull = false)
    private String name;

    /**
     * The password of this user
     */
    @DatabaseField(canBeNull = false)
    private String password;

    /**
     * The role of this user
     */
    @DatabaseField(canBeNull = false)
    private Role role;

    /**
     * The blacklist of this user in String format ("bad_user1, bad_user2,...")
     */
    @DatabaseField
    private String blacklist;

    public User() {}

    /**
     * Constructor of user entitie
     * @param id : the user ID
     * @param name : the user name
     * @param password : the password of this user
     * @param role : the role of this user
     * @param blacklist : the blacklist of this user in String format
     */
    public User(String id, String name, String password, Role role, String blacklist) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.blacklist = blacklist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String blacklist) {
        this.blacklist = blacklist;
    }

    /**
     * Function which allow you get the entitie in JSON format
     * @return a JSONObject which is the entitie in JSON format
     */
    public JSONObject toJSON() {
        JSONObject res = new JSONObject();
        res.put("id",           this.getId());
        res.put("name",         this.getName());
        res.put("password",     this.getPassword());
        res.put("role",         this.getRole().toString());
        res.put("blacklist",    this.getBlacklist());
        return res;
    }
}
