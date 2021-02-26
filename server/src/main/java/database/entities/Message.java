package database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.json.JSONObject;

import java.util.Date;

@DatabaseTable(tableName = "messages")
public class Message {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField(canBeNull = false)
    private String senderID;

    @DatabaseField(canBeNull = false)
    private String groupID;

    @DatabaseField(canBeNull = false)
    private Date date;

    @DatabaseField(canBeNull = false)
    private String content;

    @DatabaseField(canBeNull = false)
    private boolean isRead;

    public Message() {}

    public Message(String id, String senderID, String groupID, Date date, String content, boolean isRead) {
        this.id = id;
        this.senderID = senderID;
        this.groupID = groupID;
        this.date = date;
        this.content = content;
        this.isRead = isRead;
    }

    public String getId() {
        return id;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getGroupID() {
        return groupID;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public boolean hasBeenRead() {
        return isRead;
    }

    public JSONObject toJSON() {
        JSONObject res = new JSONObject();
        res.put("id",           this.getId());
        res.put("sender_id",    this.getSenderID());
        res.put("group_id",     this.getGroupID());
        res.put("date",         this.getDate().toString());
        res.put("content",      this.getContent());
        res.put("is_read",      this.hasBeenRead());
        return res;
    }
}
