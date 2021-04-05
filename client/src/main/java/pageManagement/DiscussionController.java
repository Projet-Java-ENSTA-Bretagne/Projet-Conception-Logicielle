package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Class handling the JavaFX objects from the Discussion scene (defined in discussion.fxml).
 */
public class DiscussionController {
    // Logging
    private static final Logger log = LogManager.getLogger(DiscussionController.class);

    /**
     * Method that is automatically executed right after "discussion.fxml" is loaded.
     */
    @FXML
    private void initialize() {
        log.info("Initializing discussion controller");
        groupObjectList = new ArrayList<>();
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

    /**
     * Adds "dummy messages" to the current (empty) group chat.
     */
    public static void loadCurrentGroupObjectWithDummyData() {
        String currentDate = getCurrentDate();

        // generating a random number between 0 and 1
        double random_number = Math.random();

        if (random_number > 0.5) {
            // 1st set of dummy data

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

        else {
            // 2nd set of dummy data

            MessageController message1 = new MessageController("Mec 1", currentDate, "qqn a vu mes clés ?");
            MessageController message2 = new MessageController(currentSender, currentDate, "euh ... non pk ?");
            MessageController message3 = new MessageController(currentSender, currentDate, "ah si c possible");
            MessageController message4 = new MessageController("Mec 1", currentDate, "nice ! où ça ?");

            addMessageToAssociatedMessageList(message1);
            addMessageToAssociatedMessageList(message2);
            addMessageToAssociatedMessageList(message3);
            addMessageToAssociatedMessageList(message4);
        }
    }

    private static String currentlyOpenedGroup;

    public static void setCurrentlyOpenedGroup(String newCurrentlyOpenedGroup) {
        currentlyOpenedGroup = newCurrentlyOpenedGroup;
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
     * Loads all the messages stored in the group object associated to the discussion page
     * that was just opened from the Home scene.
     * TODO : Link this method to network
     *
     * @throws IOException If error when FXMLLoader.load() is called (in displayMessageFromController())
     */
    public static void loadMessages() throws IOException {
        for (GroupObject groupObject : groupObjectList) {
            String groupName = groupObject.getGroupName();

            if (groupName.equals(currentlyOpenedGroup)) {
                for (MessageController messageController : groupObject.getMessageList()) {
                    displayMessageFromController(messageController);
                }

                return;
            }
        }
    }

    public static void unloadMessages() {
        discussionVBox.getChildren().clear();
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

    @FXML
    private JFXTextField messageTextField;

    @FXML
    private void checkIfEnterIsPressedThenSendMessage(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }

    /**
     * Sends the message to all the users in the current group chat (or the PM chat).
     * TODO : Link this method to network
     *
     * @throws IOException If error when FXMLLoader.load() is called (in displayMessageFromController())
     */
    private void sendMessage() throws IOException {
        String currentWholeMessage = messageTextField.getText();

        if ((currentWholeMessage == null) || (currentWholeMessage.length() == 0)) {
            // we don't want to send empty messages
            return;
        }

        else {
            System.out.println("");
            log.info("Vous venez d'envoyer un nouveau message !");

            // adding the new message to the Discussion scene

            // the sent message has to be less than 20 characters (for now)
            String currentMessage = currentWholeMessage.substring(0, Math.min(currentWholeMessage.length(), 20));

            // resetting the message TextField
            messageTextField.setText("");

            MessageController messageController = new MessageController(getCurrentSender(),
                                                      getCurrentDate(), currentMessage);

            log.debug("Émetteur : \"" + messageController.getSender() + "\"");
            log.debug("Date d'envoi : \"" + messageController.getDate() + "\"");
            log.debug("Contenu : \"" + messageController.getContent() + "\"");

            addMessageToAssociatedMessageList(messageController);
            displayMessageFromController(messageController);
        }
    }

    /**
     * Action linked to the "Quitter" JFXButton.
     * Leaves the current Discussions page, then switches to the Home scene.
     * TODO : Link this method to network
     */
    @FXML
    private void actionExitButton() {
        System.out.println("");
        log.info("Vous venez d'appuyer sur le bouton \"Quitter\"");

        unloadMessages();
        MainController.switchToHomeScene();
    }
}
