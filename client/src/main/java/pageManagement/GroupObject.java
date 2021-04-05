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
 * We will consider that a message is equivalent to its associated controller, since
 * all the relevant data of a message is held inside its controller.
 */
public class GroupObject {
    // Logging
    private final Logger log = LogManager.getLogger(GroupObject.class);

    private String groupName;
    private JSONArray userIdList;
    private JSONArray usernameList;
    private ArrayList<MessageController> messageList;

    public GroupObject(String groupName, JSONArray userIdList) {
        this.groupName = groupName;
        this.userIdList = userIdList;
        messageList = new ArrayList<>();

        // the "getUserByID" protocol seems to be broken
        //initializeUsernameList();
    }

    private void initializeUsernameList() throws JSONException {
        usernameList = new JSONArray();

        for (int i = 0; i < userIdList.length(); i++) {
            String userID = userIdList.getString(i);

            JSONObject userIdData = new JSONObject();
            userIdData.put("user_id", userID);

            String getUserByIdRequest = RequestBuilder.buildWithData("getUserByID", userIdData).toString();
            String responseFromServer = MainController.getTcpClient().sendRequest(getUserByIdRequest);

            JSONObject wholeReceivedData = new JSONObject(responseFromServer);
            String requestStatus = wholeReceivedData.getString("status");

            if (!requestStatus.equals("OK")) {
                log.error("L'utilisateur d'ID \"" + userID + "\" n'existe pas !");
                System.exit(1);
            }

            // getting associated username
            JSONObject data = wholeReceivedData.getJSONObject("data");
            JSONObject user = data.getJSONObject("user");
            String username = user.getString("name");

            usernameList.put(username);
        }

        log.debug(String.format("Username list (groupName : \"%s\") : %s", groupName, usernameList.toString()));
    }

    public String getGroupName() {
        return groupName;
    }

    public JSONArray getUserIdList() {
        return userIdList;
    }

    public JSONArray getUsernameList() {
        return usernameList;
    }

    public ArrayList<MessageController> getMessageList() {
        return messageList;
    }

    public void addMessage(MessageController messageController) {
        messageList.add(messageController);
    }


    public void delete() {
        groupName = null;
        messageList = null;
    }
}
