package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

import networking.RequestBuilder;

/**
 * This is a "backpack class" containing all the relevant information of a given
 * group (ie the group name + all the messages).
 */
public class GroupObject {
    // Logging
    private final Logger log = LogManager.getLogger(GroupObject.class);

    private String groupName;
    private String groupID;
    private JSONArray userIdList;
    private JSONArray usernameList;
    private ArrayList<MessageObject> messageList;
    private int messageIndex;

    public GroupObject(String groupName, JSONArray userIdList) {
        this.groupName = groupName;
        groupID = HomeController.getGroupID(this.groupName);

        this.userIdList = userIdList;
        initializeUsernameList();

        messageList = new ArrayList<>();
        messageIndex = -1;

        log.debug(String.format("Username list (groupName : \"%s\") : %s", groupName, usernameList.toString()));
        log.debug(String.format("User ID list (groupName : \"%s\") : %s", groupName, userIdList.toString()));
    }

    private void initializeUsernameList() throws JSONException {
        usernameList = new JSONArray();

        for (int i = 0; i < userIdList.length(); i++) {
            try {
                String userID = userIdList.getString(i);
                String username = DiscussionController.getUsernameFromID(userID);
                usernameList.put(username);
            }
            catch (JSONException jsonException) {
                log.error("Erreur JSON détectée : " + jsonException);
                System.exit(1);
            }
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupID() {
        return groupID;
    }

    public JSONArray getUserIdList() {
        return userIdList;
    }

    public JSONArray getUsernameList() {
        return usernameList;
    }

    public ArrayList<MessageObject> getMessageList() {
        return messageList;
    }

    public void addMessage(MessageController messageController) {
        messageIndex += 1;
        MessageObject newMessage = new MessageObject(groupName, groupID, messageIndex, messageController);
        messageList.add(newMessage);
    }

    public int getTotalNbOfMessages() {
        return messageIndex + 1;
    }

    public void delete() {
        groupName = null;
        groupID = null;
        userIdList = null;
        usernameList = null;

        for (MessageObject messageObject : messageList) {
            messageObject.delete();
            messageList.remove(messageObject);
            messageObject = null;
        }

        messageIndex = 0;
    }
}
