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

    // For messages, it isn't really useful to write an "initialize" FXML method

    private String sender;
    private String date;
    private String content;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageController(String sender, String date, String content) {
        this.sender = sender;
        this.date = date;
        this.content = content;
    }
}
