package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.event.EventHandler;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Observable;

import networking.RequestBuilder;

/**
 * Class handling the JavaFX objects from the Discussion scene (defined in discussion.fxml).
 */
public class DiscussionController extends Observable {
    // Logging
    private static final Logger log = LogManager.getLogger(DiscussionController.class);

    /**
     * Method that is automatically executed right after "discussion.fxml" is loaded.
     */
    @FXML
    private void initialize() {
        log.info("Initializing discussion controller");
        groupObjectList = new ArrayList<>();
        this.addObserver(new MessageSender());
    }

    private static VBox discussionVBox;
    private static ScrollPane discussionScrollPane;

    public static void initializeStaticComponents() {
        Parent discussionRoot = MainController.getDiscussionScene().getRoot();

        // unfortunately, JavaFX does NOT load objects into static "@FXML" variables, so we
        // will have to do it ourselves
        discussionVBox = (VBox) discussionRoot.lookup("#discussionVBox");
        discussionScrollPane = (ScrollPane) discussionRoot.lookup("#discussionScrollPane");
        discussionNameLabel = (Label) discussionRoot.lookup("#discussionNameLabel");
        messageTextField = (JFXTextField) discussionRoot.lookup("#messageTextField");

        // preventing horizontal scrolling in the discussion ScrollPane
        discussionScrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if (scrollEvent.getDeltaX() != 0) {
                    scrollEvent.consume();
                }
            }
        });

        // we do this so that we get the new value of the height of the discussion VBox
        // **as soon as it's increased**, and can therefore automatically scroll to the
        // *very bottom* of the discussion ScrollPane **as soon as a new message is sent/received**,
        // which CANNOT be done (easily) otherwise
        discussionVBox.heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldVvalue, Object newVvalue) {
                discussionScrollPane.setVvalue((Double) newVvalue);
            }
        });
    }

    private static Label discussionNameLabel;

    public static void updateDiscussionNameLabel(String newDiscussionName) {
        discussionNameLabel.setText(newDiscussionName);
    }

    private static String currentSender;

    public static String getCurrentSender() {
        return currentSender;
    }

    public static void setCurrentSender(String newSenderName) {
        currentSender = newSenderName;
    }

    private static String currentSenderID;

    public static String getCurrentSenderID() {
        return currentSenderID;
    }

    public static void setCurrentSenderID(String newSenderID) {
        currentSenderID = newSenderID;
    }

    // Object containing all the current groups (with their respective messages)
    private static ArrayList<GroupObject> groupObjectList;

    public static ArrayList<GroupObject> getGroupObjectList() {
        return groupObjectList;
    }

    public static void addGroupObject(GroupObject groupObject) {
        groupObjectList.add(groupObject);
    }

    /**
     * Deletes a group object from the group object list.
     * TODO : Link this method to network
     *
     * @param nameOfTheGroupToDelete The name of the group to delete
     */
    public static void deleteGroupObjectByGroupName(String nameOfTheGroupToDelete) {
        for (GroupObject groupObject : groupObjectList) {
            String otherGroupName = groupObject.getGroupName();

            if (otherGroupName.equals(nameOfTheGroupToDelete)) {
                groupObjectList.remove(groupObject);

                groupObject.delete();
                groupObject = null;

                return;
            }
        }
    }

    private static String currentlyOpenedGroup;

    public static String getCurrentlyOpenedGroup() {
        return currentlyOpenedGroup;
    }

    public static void setCurrentlyOpenedGroup(String newCurrentlyOpenedGroup) {
        currentlyOpenedGroup = newCurrentlyOpenedGroup;
        currentlyOpenedGroupID = HomeController.getGroupID(currentlyOpenedGroup);
    }

    private static String currentlyOpenedGroupID;

    public static String getCurrentlyOpenedGroupID() {
        return currentlyOpenedGroupID;
    }

    public static void addMessageToAssociatedMessageList(MessageController messageController) {
        for (GroupObject groupObject : groupObjectList) {
            String groupName = groupObject.getGroupName();

            if (groupName.equals(currentlyOpenedGroup)) {
                groupObject.addMessage(messageController);
                return;
            }
        }
    }

    /**
     * Displays a given message in the discussion VBox from the data contained in its associated controller.
     *
     * @param messageController The message controller that will be converted into a visual object
     * @throws IOException If error when FXMLLoader.load() is called
     */
    public static void displayMessageFromController(MessageController messageController) throws IOException {
        String sender = messageController.getSender();
        URL messageURL;

        if (sender.equals(currentSender)) {
            messageURL = new File("src/main/pages/sentMessage.fxml").toURI().toURL();
        }
        else {
            messageURL = new File("src/main/pages/receivedMessage.fxml").toURI().toURL();
        }

        FXMLLoader messageLoader = new FXMLLoader(messageURL);
        messageLoader.setController(messageController);
        Parent messageRoot = messageLoader.load();

        Label senderLabel = (Label) messageRoot.lookup("#senderLabel");
        senderLabel.setText(messageController.getSender());

        Label dateLabel = (Label) messageRoot.lookup("#dateLabel");
        dateLabel.setText(messageController.getDate());

        Label contentLabel = (Label) messageRoot.lookup("#contentLabel");
        contentLabel.setText(messageController.getContent());

        discussionVBox.getChildren().add(messageRoot);
    }

    /**
     * Loads all the messages from the group that has just been opened.
     *
     * @throws IOException If error when FXMLLoader.load() is called (in displayMessageFromController())
     */
    public static void loadMessages(int indexOfFirstMsg, boolean isAnUpdate) throws IOException, JSONException {
        for (GroupObject groupObject : groupObjectList) {
            String groupName = groupObject.getGroupName();

            if (groupName.equals(currentlyOpenedGroup)) {
                boolean loadedAllMessages = false;
                int nbOfMessagesLoadedAtOnce = 5; // can easily be changed here

                int index = indexOfFirstMsg;

                while (!loadedAllMessages) {
                    JSONObject groupData = new JSONObject();
                    groupData.put("group_id", getCurrentlyOpenedGroupID());
                    groupData.put("index", index);
                    groupData.put("limit", nbOfMessagesLoadedAtOnce);

                    String[] requestStatusAndMsgInfo = getGroupMsgProtocol(groupData);
                    String requestStatus = requestStatusAndMsgInfo[0];
                    JSONArray msgInfo = new JSONArray(requestStatusAndMsgInfo[1]);

                    if (!requestStatus.equals("OK")) {
                        log.error("corrupted communication between client and server (getGroupMsgStatus : \"" + requestStatus + "\")");
                        System.exit(1);
                    }

                    if (msgInfo.length() < nbOfMessagesLoadedAtOnce) {
                        loadedAllMessages = true;

                        if (msgInfo.length() == 0) {
                            break;
                        }
                    }

                    for (int i = 0; i < msgInfo.length(); i++) {
                        JSONObject msgData = msgInfo.getJSONObject(i);
                        MessageController messageController = convertMsgDataToController(msgData);

                        String msgID = messageController.getMsgID();
                        boolean isADuplicate = messageIsADuplicate(msgID, groupObject);

                        if ((!isAnUpdate) || (!isADuplicate)) {
                            addMessageToAssociatedMessageList(messageController);
                            displayMessageFromController(messageController);
                        }
                    }

                    index += nbOfMessagesLoadedAtOnce;
                }

                return;
            }
        }
    }

    private static String[] getGroupMsgProtocol(JSONObject groupData) {
        String getGroupMsgRequest = RequestBuilder.buildWithData("getGroupMsg", groupData).toString();
        String responseFromServer = MainController.getTcpClient().sendRequest(getGroupMsgRequest);

        JSONObject wholeReceivedData = new JSONObject(responseFromServer);
        String requestStatus = wholeReceivedData.getString("status");

        JSONArray msgInfo;
        if (requestStatus.equals("OK")) {
            JSONObject data = wholeReceivedData.getJSONObject("data");
            msgInfo = data.getJSONArray("msg");
        }
        else {
            msgInfo = new JSONArray();
        }

        String[] requestStatusAndMsgInfo = {requestStatus, msgInfo.toString()};
        return requestStatusAndMsgInfo;
    }

    private static MessageController convertMsgDataToController(JSONObject msgData) {
        String senderID = msgData.getString("sender_id");
        String sender = getUsernameFromID(senderID);
        String date = formatDateFromString(msgData.getString("date"));
        String content = msgData.getString("content");
        String msgID = msgData.getString("id");

        MessageController messageController = new MessageController(sender, senderID, date, content, msgID);
        return messageController;
    }

    private static boolean messageIsADuplicate(String msgIdToTest, GroupObject groupObject) {
        for (MessageObject messageObject : groupObject.getMessageList()) {
            String msgID = messageObject.getMsgID();
            if (msgID.equals(msgIdToTest)) {
                return true;
            }
        }
        return false;
    }

    public static String getUsernameFromID(String userID) {
        JSONObject userIdData = new JSONObject();
        userIdData.put("user_id", userID);

        String getUserByIdRequest = RequestBuilder.buildWithData("getUserByID", userIdData).toString();
        String responseFromServer = MainController.getTcpClient().sendRequest(getUserByIdRequest);

        JSONObject wholeReceivedData = new JSONObject(responseFromServer);
        String requestStatus = wholeReceivedData.getString("status");

        if (!requestStatus.equals("OK")) {
            log.error("user with ID \"" + userID + "\" doesn't exist !");
            System.exit(1);
        }

        // getting associated username
        JSONObject data = wholeReceivedData.getJSONObject("data");
        JSONObject user = data.getJSONObject("user");
        String username = user.getString("name");

        return username;
    }

    @FXML
    private void actionUpdateMessagesButton() throws IOException {
        updateMessages();
    }

    public static void updateMessages() throws IOException {
        for (GroupObject groupObject : groupObjectList) {
            String groupName = groupObject.getGroupName();

            if (groupName.equals(currentlyOpenedGroup)) {
                int initialIndex = groupObject.getTotalNbOfMessages();
                loadMessages(initialIndex, true);
                return;
            }
        }
    }

    public static void unloadMessages() {
        discussionVBox.getChildren().clear();
    }

    /**
     * Returns the current date.
     */
    public static String getCurrentDate() {
        Date currentDate = new Date();
        return formatDate(currentDate);
    }

    /**
     * Formats a date object in a relevant way for the group messages.
     *
     * @return The formatted date (example : "Le 21/03 à 17h53")
     */
    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
        String rawFormattedDate = formatter.format(date);

        String[] splitFormattedDate = rawFormattedDate.split(" ");
        String[] splitTime = splitFormattedDate[1].split(":");

        String formattedDate = "Le " + splitFormattedDate[0] + " à " + splitTime[0] + "h" + splitTime[1];
        return formattedDate;
    }

    public static String formatDateFromString(String dateString) {
        // TODO

        return dateString;
    }

    private static JFXTextField messageTextField;

    public static JFXTextField getMessageTextField() {
        return messageTextField;
    }

    @FXML
    private void checkIfEnterIsPressedThenSendMessage(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            this.setChanged();
            this.notifyObservers();
        }
    }

    /**
     * Action linked to the "Quitter" JFXButton.
     * Leaves the current Discussions page, then switches to the Home scene.
     */
    @FXML
    private void actionExitButton() {
        System.out.println("");
        log.info("Vous venez d'appuyer sur le bouton \"Quitter\"");

        unloadMessages();
        MainController.switchToHomeScene();
    }
}
