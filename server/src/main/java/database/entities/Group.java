package database.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.json.JSONObject;

import java.util.Date;

@DatabaseTable(tableName = "groups")
public class Group {

    /**
     * Group ID
     */
    @DatabaseField(id = true)
    private String id;

    /**
     * Group name
     */
    @DatabaseField(canBeNull = true)
    private String name;

    /**
     * If group is a private group (with only two users) or not
     */
    @DatabaseField(canBeNull = false)
    private boolean isPM;

    /**
     * The creation date of the group
     */
    @DatabaseField(canBeNull = false)
    private Date creationDate;

    /**
     * The list of members in group in format String ("user_id1, user_id2,...")
     */
    @DatabaseField(canBeNull = false)
    private String members;

    public Group() {}

    /**
     * Constructor of group entitie
     * @param id : the group ID
     * @param name : the group name
     * @param isPM : A boolean indicate if the group is a private group with only two users
     * @param creationDate : The creation date of this group
     * @param members : The list of members
     */
    public Group(String id, String name, boolean isPM, Date creationDate, String members) {
        this.id = id;
        this.name = name;
        this.isPM = isPM;
        this.creationDate = creationDate;
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isThisAPM() {
        return isPM;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getMembers() {
        return members;
    }
    public void setMembers(String value) {
        this.members = value;
    }

    /**
     * Function which allow you get the entitie in JSON format
     * @return a JSONObject which is the entitie in JSON format
     */
    public JSONObject toJSON() {
        JSONObject res = new JSONObject();
        res.put("id",               this.getId());
        res.put("name",             this.getName());
        res.put("is_pm",            this.isThisAPM());
        res.put("creation_date",    this.getCreationDate().toString());
        res.put("members",          this.getMembers());
        return res;
    }
}
