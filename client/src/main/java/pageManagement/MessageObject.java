package pageManagement;

/**
 * This is a "backpack class" containing all the relevant information of a given
 * message.
 */
public class MessageObject {

    private String groupName;
    private String groupID;
    private int index;
    private MessageController controller;

    public MessageObject(String groupName, String groupID, int index, MessageController controller) {
        this.groupName = groupName;
        this.groupID = groupID;
        this.index = index;
        this.controller = controller;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupID() {
        return groupID;
    }

    public int getIndex() {
        return index;
    }

    public MessageController getController() {
        return controller;
    }
}
