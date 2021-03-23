package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Class handling the JavaFX objects from the Group Thumbnails (defined in groupThumbnail.fxml)
 * that will be added to the Home scene.
 */
public class GroupThumbnailController {
    // Logging
    private final Logger log = LogManager.getLogger(GroupThumbnailController.class);

    /**
     * Method that is executed right before "groupThumbnail.fxml" is loaded.
     */
    @FXML
    void initialize() {
        System.out.println("");
        log.info("Initializing group thumbnail controller, groupName = \"" + getGroupName() + "\"");
    }

    private String groupName;
    private String groupStatus;
    private String groupDescription;

    private String operationType;
    private String serverIpAddress;
    private int serverPort;
    private int groupId;

    public String getGroupName() {
        return groupName;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getServerIpAddress() {
        return serverIpAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getGroupId() {
        return groupId;
    }

    private boolean theGroupHasAlreadyBeenOpened;

    public GroupThumbnailController(String groupName, String groupStatus, String groupDescription,
                                    String operationType, String serverIpAddress,
                                    int serverPort, int groupId) {

        this.groupName = groupName;
        this.groupStatus = groupStatus;
        this.groupDescription = groupDescription;

        this.operationType = operationType;
        this.serverIpAddress = serverIpAddress;
        this.serverPort = serverPort;
        this.groupId = groupId;

        theGroupHasAlreadyBeenOpened = false;
    }

    /**
     * Action linked to the "OPEN" JFXButton.
     * Opens the discussion scene associated with the chosen group. Loads the message
     * from that same group.
     * /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
     * TODO : Link this method to network
     *
     * @throws IOException If error when FXMLLoader.load() is called (in DiscussionController.loadMessages();)
     */
    public void actionOpenGroupButton() throws IOException {
        log.info("Bouton \"OPEN\" appuye, groupName = \"" + getGroupName() + "\"");

        Label discussionNameLabel = (Label) MainController.getDiscussionScene().lookup("#discussionNameLabel");
        if (getOperationType().equals("createPm")) {
            discussionNameLabel.setText("MP avec : " + getGroupName());
        }
        else {
            discussionNameLabel.setText("Groupe : " + getGroupName());
        }

        DiscussionController.setNameOfTheCurrentGroup(getGroupName());

        if (!theGroupHasAlreadyBeenOpened) {
            DiscussionController.loadGroupObjectWithDummyData();
            theGroupHasAlreadyBeenOpened = true;
        }

        MainController.switchToDiscussionScene();
        DiscussionController.loadMessages();
    }

    /**
     * Action linked to the "LEAVE" JFXButton.
     * Leaves the chosen group chat. Will open a new ConfirmLeaveGroup window/stage to
     * check if you really want to leave the group (since it's an irreversible action).
     * /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
     *
     * @throws IOException If error when FXMLLoader.load() is called
     */
    public void actionLeaveGroupButton() throws IOException {
        log.info("Bouton \"LEAVE\" appuye, groupName = \"" + getGroupName() + "\"");

        URL confirmLeaveGroupURL = new File("src/main/pages/confirmLeaveGroup.fxml").toURI().toURL();
        FXMLLoader confirmLeaveGroupLoader = new FXMLLoader(confirmLeaveGroupURL);
        ConfirmLeaveGroupController confirmLeaveGroupController = new ConfirmLeaveGroupController(groupName);
        confirmLeaveGroupLoader.setController(confirmLeaveGroupController);
        Parent confirmLeaveGroupRoot = confirmLeaveGroupLoader.load();
        Scene scene = new Scene(confirmLeaveGroupRoot, 300, 150);

        Label groupNameLabel = (Label) confirmLeaveGroupRoot.lookup("#groupNameLabel");
        groupNameLabel.setText("\"" + groupName + "\" ?");

        JFXButton yesButton = (JFXButton) confirmLeaveGroupRoot.lookup("#yesButton");
        yesButton.setOnAction(e -> confirmLeaveGroupController.actionYesButton());

        JFXButton noButton = (JFXButton) confirmLeaveGroupRoot.lookup("#noButton");
        noButton.setOnAction(e -> confirmLeaveGroupController.actionNoButton());

        Stage currentConfirmLeaveGroupStage = new Stage();
        currentConfirmLeaveGroupStage.getIcons().add(new Image("question-mark.png"));
        currentConfirmLeaveGroupStage.setTitle("Confirmation");
        currentConfirmLeaveGroupStage.setScene(scene);
        currentConfirmLeaveGroupStage.initModality(Modality.APPLICATION_MODAL);
        currentConfirmLeaveGroupStage.setOnCloseRequest(e -> HomeController.setCurrentConfirmLeaveGroupStage(null));

        HomeController.setCurrentConfirmLeaveGroupStage(currentConfirmLeaveGroupStage);

        currentConfirmLeaveGroupStage.show();
    }
}
