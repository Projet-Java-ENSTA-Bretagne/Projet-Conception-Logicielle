package pageManagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
     * Action linked to the "join or create group" icon/JFXButton.
     * Opens a new GroupSettings stage, where it's possible to configurate
     * the settings of the group you want to join/create.
     *
     * @throws IOException If error when FXMLLoader.load() is called
     * TODO : Link this method to network
     */
    @FXML
    void joinOrCreateGroup() throws IOException {
        log.info("Vous venez d'appuyer sur le bouton \"Join or create group\"");

        URL groupSettingsURL = new File("src/main/pages/groupSettings.fxml").toURI().toURL();
        Parent groupSettingsRoot = FXMLLoader.load(groupSettingsURL);
        Scene scene = new Scene(groupSettingsRoot, 400, 375);

        Stage currentGroupSettingsStage = new Stage();
        currentGroupSettingsStage.getIcons().add(new Image("settings-icon.png"));
        currentGroupSettingsStage.setTitle("Group Settings");
        currentGroupSettingsStage.setScene(scene);
        currentGroupSettingsStage.initModality(Modality.APPLICATION_MODAL);
        currentGroupSettingsStage.setOnCloseRequest(e -> setCurrentGroupSettingsStage(null));

        setCurrentGroupSettingsStage(currentGroupSettingsStage);

        currentGroupSettingsStage.show();
    }

    //

}
