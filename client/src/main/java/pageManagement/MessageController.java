package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class handling the JavaFX objects from the messages (defined in sentMessage.fxml and
 * receivedMessage.fxml) that will be added to the Discussion scene.
 */
public class MessageController {
    // Logging
    private final Logger log = LogManager.getLogger(MessageController.class);

    // For messages, we will NOT write an "initialize" FXML method, since this class
    // is deliberately NOT LINKED to any FXML file (since messages will be loaded AND unloaded
    // dynamically from the Discussion scene)

    private String sender;
    private String senderID;
    private String date;
    private String content;
    private String msgID;

    public String getSender() {
        return sender;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getMsgID() {
        return msgID;
    }

    public MessageController(String sender, String senderID, String date, String content, String msgID) {
        this.sender = sender;
        this.senderID = senderID;
        this.date = date;
        this.content = content;
        this.msgID = msgID;
    }

    public void delete() {
        sender = null;
        senderID = null;
        date = null;
        content = null;
        msgID = null;
    }
}
