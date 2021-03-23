package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;

/**
 * This is a "backpack class" containing all the relevant information of a given
 * group (ie the group name + all the messages).
 * We will consider that a message is equivalent to its associated controller, since
 * all the relevant data of a message is held inside its controller.
 */
public class GroupObject {
    // Logging
    private final Logger log = LogManager.getLogger(GroupObject.class);

    private String groupName;
    private ArrayList<MessageController> messageList;

    public GroupObject(String groupName) {
        this.groupName = groupName;
        messageList = new ArrayList<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public void addMessage(MessageController messageController) {
        messageList.add(messageController);
    }

    public ArrayList<MessageController> getMessageList() {
        return messageList;
    }

    public void delete() {
        groupName = null;
        messageList = null;
    }
}
