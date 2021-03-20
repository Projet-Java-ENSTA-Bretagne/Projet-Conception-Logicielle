package pageManagement;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class handling the JavaFX objects from the Home scene (defined in home.fxml).
 */
public class DiscussionController {
    // Logging
    private static final Logger log = LogManager.getLogger(DiscussionController.class);

    // Object containing all the current group thumbnails
    private static ArrayList<MessageObject> messageList;

    public static ArrayList<MessageObject> getmessageList() {
        return messageList;
    }

    /**
     * Adds messages previews to the discussion page.
     *
     * @param messageObject The message preview object to add
     */
    public static void addmessage(MessageObject messageObject) {
        discussionVBox.getChildren().add(messageObject.getRoot());
        messageList.add(messageObject);
    }

    /**
     * Deletes a group thumbnail from the Home page.
     *
     * @param numberOfTheMessageToDelete The name of the message to delete
     */
    public static void deleteMessageByNumber(int numberOfTheMessageToDelete) {
        for (MessageObject messageObject : messageList) {
            MessageController messageController = messageObject.getController();
            int messageNb = messageController.getMessageNb();

            if (messageNb == numberOfTheMessageToDelete) {
                Parent messageRoot = messageObject.getRoot();
                discussionVBox.getChildren().remove(messageRoot);

                messageList.remove(messageObject);

                messageController = null;
                messageRoot = null;
                messageObject = null;

                decrementNbMessagesVisible();
                System.out.println("");
                log.debug("Vous venez de supprimer le message n° \"" + messageNb + "\"");
                log.debug("Nombre total de groupes restants : " + nbMessagesVisible + "\n");

                return;
            }
        }

        // just in case (but should theoretically never happen)
        System.out.println("");
        log.error("Le message \"" + numberOfTheMessageToDelete + "\" n'existe pas !\n");
    }

    private static VBox discussionVBox;

    public static void initializediscussionVBox(VBox newdiscussionVBox) {
        discussionVBox = newdiscussionVBox;
    }

    // number of created **or joined** groups
    private static int nbmessages;

    public static int getNbMessages() {
        return nbmessages;
    }

    // nbCreatedGroups can only increase
    public static void incrementNbMessages() {
        nbmessages += 1;
    }

    private static int nbMessagesVisible;

    public static void incrementNbMessagesVisible() {
        nbMessagesVisible += 1;
    }

    public static void decrementNbMessagesVisible() {
        nbMessagesVisible -= 1;
    }

    /**
     * Method that is executed right before "discussion.fxml" is loaded.
     */
    @FXML
    void initialize() {
        log.info("Initializing discussion controller\n");

        discussionVBox = null;
        messageList = new ArrayList<>();
        nbmessages = 0;
        nbMessagesVisible = 0;
        currentGroupSettingsStage = null;
        currentConfirmLeaveGroupStage = null;
    }

    //

    /**
     * Action linked to the "Déconnexion" JFXButton.
     * Disconnects from the server, leaves the Home page, then switches to the Login scene.
     *
     * TODO : Link this method to network
     */
    @FXML
    void actionDisconnectButton() {
        System.out.println("");
        log.info("Deconnexion");
        MainController.switchToLoginScene();
    }

    private static Stage currentGroupSettingsStage;

    public static void setCurrentGroupSettingsStage(Stage groupSettingsStage) {
        currentGroupSettingsStage = groupSettingsStage;
    }

    public static void closeCurrentGroupSettingsStage() {
        currentGroupSettingsStage.close();
        setCurrentGroupSettingsStage(null);
    }

    private static Stage currentConfirmLeaveGroupStage;

    public static void setCurrentConfirmLeaveGroupStage(Stage confirmLeaveGroupStage) {
        currentConfirmLeaveGroupStage = confirmLeaveGroupStage;
    }

    public static void closeCurrentConfirmLeaveGroupStage() {
        currentConfirmLeaveGroupStage.close();
        setCurrentConfirmLeaveGroupStage(null);
    }



    /**
     * Action linked to the "DONE" JFXButton.
     * Checks if the group settings are valid, then, according to the chosen
     * operation type, creates a new group or connects the current tcpClient
     * to the desired group chat.
     *
     * @throws IOException If error when FXMLLoader.load() is called
     * TODO : Link this method to network
     */
    @FXML
    void addMessageToDiscussion(MessageController message) throws IOException {
        log.info("Vous venez d'appuyer sur le bouton \"DONE\"");

        boolean parametersAreValid = true;

        // TODO : parameter check ?

        if (parametersAreValid) {
            System.out.println("");
            log.debug("message N° : \""  +message.getMessageNb() + "\"");
            log.debug("sender : \"" + message.getSender() + "\"");
            log.debug("date d'envoi : \"" + message.getdate() + "\"");


            /* ---------------------------------------------------------- */

            // adding new message displayed

            DiscussionController.incrementNbMessages();
            DiscussionController.incrementNbMessagesVisible();

            URL messageURL = new File("src/main/pages/message.fxml").toURI().toURL();
            FXMLLoader messageLoader = new FXMLLoader(messageURL);
            MessageController messageController = new MessageController(message.getMessageNb(), message.getSender(), message.getdate(), message.getContent());
            messageLoader.setController(messageController);
            Parent discussionRoot = messageLoader.load();


            // this seems to be the only way to get the **NOT-NULL** discussion HBox from the Home scene
            if (DiscussionController.getNbMessages() == 1) {
                DiscussionController.initializediscussionVBox((VBox) MainController.getHomeScene().lookup("#discussionVBox"));
            }

            Label senderLabel = (Label) discussionRoot.lookup("#senderlabel");
            senderLabel.setText(message.getContent());

            Label dateLabel = (Label) discussionRoot.lookup("#datelabel");
            if (message.getContent().equals("today")) {  //TODO : add real date support to date (change type)
                dateLabel.setText("today");
            }
            else {
                dateLabel.setText(message.getdate());
            }

            Label contentLabel = (Label) discussionRoot.lookup("#contentlabel");
            contentLabel.setText(message.getContent());

            MessageObject newMessageObject = new MessageObject(messageController, discussionRoot);
            DiscussionController.addmessage(newMessageObject);

        }

        else {
            System.out.println("");
            log.error("Parametrage invalide !");
        }
    }
    //

}
