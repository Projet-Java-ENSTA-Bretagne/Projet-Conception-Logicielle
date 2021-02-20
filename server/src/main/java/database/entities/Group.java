package database.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "groups")
public class Group {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField(canBeNull = true)
    private String name;

    @DatabaseField(canBeNull = false)
    private boolean isPM;

    @DatabaseField(canBeNull = false)
    private Date creationDate;

    @DatabaseField(canBeNull = false)
    private String members;

    public Group() {}

    public Group(String id, String name, boolean isPM, Date creationDate, String members) {
        this.id = id;
        this.name = name;
        this.isPM = isPM;
        this.creationDate = creationDate;
        this.members = members;
    }
}
