package pageManagement;

import javafx.scene.Parent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is simply a "backpack class" containing all the relevant information of
 * a given message.
 */
public class MessageObject {
    // Logging
    private final Logger log = LogManager.getLogger(MessageObject.class);

    private MessageController messageController;
    private Parent messageRoot;

    public MessageObject(MessageController controller, Parent root) {
        messageController = controller;
        messageRoot = root;
    }

    public MessageController getController() {
        return messageController;
    }

    public Parent getRoot() {
        return messageRoot;
    }

    //

}
