package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Date;

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
    private Date date;
    private String content;

    public String getSender() {
        return sender;
    }

    public String getDate() {
        return DiscussionController.formatDate(date);
    }

    public String getContent() {
        return content;
    }

    public MessageController(String sender, Date date, String content) {
        this.sender = sender;
        this.date = date;
        this.content = content;
    }
}
