package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class handling the JavaFX objects from the Discussion scene (defined in discussion.fxml).
 */
public class DiscussionController {
    // Logging
    private static final Logger log = LogManager.getLogger(DiscussionController.class);

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

    public static void loadGroupObjectWithDummyData() {
        String currentDate = getCurrentDate();

        if (totalNbOfSentMessages % 3 == 0) {
            MessageController message1 = new MessageController(currentSender, currentDate, "hey");
            MessageController message2 = new MessageController("Mec 1", currentDate, "hey !");
            MessageController message3 = new MessageController(currentSender, currentDate, "ça va ?");
            MessageController message4 = new MessageController("Mec 1", currentDate, "yes et toi ?");
            MessageController message5 = new MessageController("Mec 2", currentDate, "re !");
            MessageController message6 = new MessageController(currentSender, currentDate, "aaaye re");
            MessageController message7 = new MessageController("Mec 1", currentDate, "re mdr");

            addMessageToAssociatedMessageList(message1);
            addMessageToAssociatedMessageList(message2);
            addMessageToAssociatedMessageList(message3);
            addMessageToAssociatedMessageList(message4);
            addMessageToAssociatedMessageList(message5);
            addMessageToAssociatedMessageList(message6);
            addMessageToAssociatedMessageList(message7);
        }

        else if (totalNbOfSentMessages % 3 == 1) {
            MessageController message1 = new MessageController("Mec ché-per", currentDate, "qqn a vu mes clés ?");
            MessageController message2 = new MessageController(currentSender, currentDate, "euh ... non pk ?");
            MessageController message3 = new MessageController(currentSender, currentDate, "ah si c possible");
            MessageController message4 = new MessageController("Mec ché-per", currentDate, "nice ! ou ça ?");

            addMessageToAssociatedMessageList(message1);
            addMessageToAssociatedMessageList(message2);
            addMessageToAssociatedMessageList(message3);
            addMessageToAssociatedMessageList(message4);
        }

        else {
            // 33% chance that no additional "dummy messages" are loaded
            return;
        }
    }

    private static String nameOfTheCurrentGroup;

    public static String getNameOfTheCurrentGroup() {
        return nameOfTheCurrentGroup;
    }

    public static void setNameOfTheCurrentGroup(String nameOfTheNewCurrentGroup) {
        nameOfTheCurrentGroup = nameOfTheNewCurrentGroup;
    }

    public static void addMessageToAssociatedMessageList(MessageController messageController) {
        for (GroupObject groupObject : groupObjectList) {
            String groupName = groupObject.getGroupName();

            if (groupName.equals(nameOfTheCurrentGroup)) {
                groupObject.addMessage(messageController);
                return;
            }
        }
    }

    /**
     * Displays a given message in the discussion VBox from the data in its associated controller.
     *
     * @param messageController
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
     * Loads all the messages stored in the group object associated to the discussion page
     * that was just opened from the Home scene.
     *
     * @throws IOException If error when FXMLLoader.load() is called (in displayMessageFromController())
     */
    public static void loadMessages() throws IOException {
        for (GroupObject groupObject : groupObjectList) {
            String groupName = groupObject.getGroupName();

            if (groupName.equals(nameOfTheCurrentGroup)) {
                for (MessageController messageController : groupObject.getMessageList()) {
                    displayMessageFromController(messageController);
                }
            }
        }
    }

    public static void unloadMessages() {
        discussionVBox.getChildren().clear();
    }

    private static VBox discussionVBox;

    public static void initializeDiscussionVBox(VBox newDiscussionVBox) {
        discussionVBox = newDiscussionVBox;
        discussionVBoxIsNull = false;
    }

    private static boolean discussionVBoxIsNull;

    public static boolean isDiscussionVBoxNull() {
        return discussionVBoxIsNull;
    }

    private static int totalNbOfSentMessages;

    private static String currentSender;

    public static String getCurrentSender() {
        return currentSender;
    }

    public static void setCurrentSender(String newSenderName) {
        currentSender = newSenderName;
    }

    /**
     * Generates the current date, formatted in a relevant way for the group messages.
     *
     * @return The current date (example : "Le 21/03 à 17h53")
     */
    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
        Date date = new Date();
        String formattedDate = formatter.format(date);

        String[] splitFormattedDate = formattedDate.split(" ");
        String[] splitTime = splitFormattedDate[1].split(":");

        String currentDate = "Le " + splitFormattedDate[0] + " à " + splitTime[0] + "h" + splitTime[1];

        return currentDate;
    }

    /**
     * Method that is executed right before "discussion.fxml" is loaded.
     */
    @FXML
    void initialize() {
        log.info("Initializing discussion controller\n");

        totalNbOfSentMessages = 0;
        discussionVBox = null;
        discussionVBoxIsNull = true;
        groupObjectList = new ArrayList<>();
    }

    /**
     * Action linked to the "Quitter" JFXButton.
     * Leaves the current Discussions page, then switches to the Home scene.
     * TODO : Link this method to network
     */
    @FXML
    void actionExitButton() {
        System.out.println("");
        log.info("Vous venez d'appuyer sur le bouton \"Quitter\"");

        Label discussionNameLabel = (Label) MainController.getDiscussionScene().lookup("#discussionNameLabel");
        discussionNameLabel.setText("Discussion name"); // default name

        unloadMessages();

        MainController.switchToHomeScene();
    }

    @FXML
    private JFXTextField messageTextField;

    /**
     * Sends the message to all the users in the current group chat (or the PM chat).
     * TODO : Link this method to network
     *
     * @throws IOException If error when FXMLLoader.load() is called (in displayMessageFromController())
     */
    @FXML
    void sendMessage() throws IOException {
        totalNbOfSentMessages += 1;

        // this seems to be the only way to get the **NOT-NULL** discussion VBox from the
        // Discussion scene
        if ((totalNbOfSentMessages == 1) && (discussionVBox == null)) {
            initializeDiscussionVBox((VBox) MainController.getDiscussionScene().lookup("#discussionVBox"));
        }

        String currentWholeMessage = messageTextField.getText();

        if ((currentWholeMessage == null) || (currentWholeMessage.length() == 0)) {
            System.out.println("");
            log.error("Message invalide !");
        }

        else {
            // adding the new message to the Discussion scene

            // the sent message has to be less than 20 characters (for now)
            String currentMessage = currentWholeMessage.substring(0, Math.min(currentWholeMessage.length(), 20));

            // resetting the message TextField
            messageTextField.setText("");

            MessageController messageController = new MessageController(getCurrentSender(),
                                                      getCurrentDate(), currentMessage);

            System.out.println("");
            log.debug("Emetteur : \"" + messageController.getSender() + "\"");
            log.debug("Date d'envoi : \"" + messageController.getDate() + "\"");
            log.debug("Contenu : \"" + messageController.getContent() + "\"");

            addMessageToAssociatedMessageList(messageController);
            displayMessageFromController(messageController);
        }
    }
}
