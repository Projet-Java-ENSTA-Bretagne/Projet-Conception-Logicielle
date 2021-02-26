package database.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.json.JSONObject;

@DatabaseTable(tableName = "users")
public class User {

    public enum Role {
        ROLE_USER,
        ROLE_ADMIN
    }

    @DatabaseField(id = true)
    private String id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String password;

    @DatabaseField(canBeNull = false)
    private Role role;

    @DatabaseField
    private String blacklist;

    public User() {}

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
