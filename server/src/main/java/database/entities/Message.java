package database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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
}
