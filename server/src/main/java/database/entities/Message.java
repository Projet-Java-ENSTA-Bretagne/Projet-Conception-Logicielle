package database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.json.JSONObject;

import java.util.Date;

@DatabaseTable(tableName = "messages")
public class Message {

    /**
     * Message ID
     */
    @DatabaseField(id = true)
    private String id;

    /**
     * The sender ID of this message
     */
    @DatabaseField(canBeNull = false)
    private String senderID;

    /**
     * The group ID where the message was been send
     */
    @DatabaseField(canBeNull = false)
    private String groupID;

    /**
     * The date of send of this message
     */
    @DatabaseField(canBeNull = false)
    private Date date;

    /**
     * The content of this message
     */
    @DatabaseField(canBeNull = false)
    private String content;

    /**
     * Check if the message was been read or not
     */
    @DatabaseField(canBeNull = false)
    private boolean isRead;

    public Message() {}

    /**
     * Constructor of message entitie
     * @param id : The message ID
     * @param senderID : The sender ID of this message
     * @param groupID : The group ID where the message was been send
     * @param date : The send date of this message
     * @param content : The content of this message
     * @param isRead Check if the message was been read or not
     */
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

    /**
     * Function which allow you get the entitie in JSON format
     * @return a JSONObject which is the entitie in JSON format
     */
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
