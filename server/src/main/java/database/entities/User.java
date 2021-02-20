package database.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
}
