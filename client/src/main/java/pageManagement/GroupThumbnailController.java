package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    // displayed features
    private String groupName;
    private GroupStatusesEnum groupStatus;
    private String groupDescription;

    private OperationTypesEnum operationType;

    public String getGroupName() {
        return groupName;
    }

    public GroupStatusesEnum getGroupStatus() {
        return groupStatus;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public OperationTypesEnum getOperationType() {
        return operationType;
    }

    @FXML
    private Label groupNameLabel;

    @FXML
    private Label groupStatusLabel;

    @FXML
    private Label groupDescriptionLabel;

    // it's cleaner if we do NOT use constructors when updating FXML templates
    public void build(String groupName, GroupStatusesEnum groupStatus, String groupDescription,
                      OperationTypesEnum operationType) {

        this.groupName = groupName;
        this.groupStatus = groupStatus;
        this.groupDescription = groupDescription;

        this.operationType = operationType;

        System.out.println("");
        log.info("Initializing group thumbnail controller, groupName = \"" + this.groupName + "\"");

        updateDesign();
    }

    // text contained in the discussion name label of the associated group/PM chat
    private String discussionNameText;

    private void updateDesign() {
        groupNameLabel.setText(groupName);

        if (operationType == OperationTypesEnum.CREATE_PM) {
            groupStatusLabel.setText("Messages privés (MP)");
            discussionNameText = "MP avec : " + groupName;
        }
        else {
            if (groupStatus == GroupStatusesEnum.PUBLIC) {
                groupStatusLabel.setText("Groupe public");
            }
            else if (groupStatus == GroupStatusesEnum.PRIVATE) {
                groupStatusLabel.setText("Groupe privé");
            }

            discussionNameText = "Groupe : " + groupName;
        }

        groupDescriptionLabel.setText(groupDescription);
    }

    /**
     * Action linked to the "OPEN" JFXButton. Opens the discussion scene associated with the
     * chosen group. Loads the message from that same group.
     * TODO : Link this method to network
     *
     * @throws IOException If error when FXMLLoader.load() is called (in DiscussionController.loadMessages();)
     */
    @FXML
    private void actionOpenGroupButton() throws IOException {
        log.info("Bouton \"OPEN\" appuyé, groupName = \"" + groupName + "\"");

        DiscussionController.setCurrentlyOpenedGroup(groupName);

        MainController.switchToDiscussionScene();
        DiscussionController.updateDiscussionNameLabel(discussionNameText);
        DiscussionController.loadMessages(0, false);
    }

    /**
     * Action linked to the "LEAVE" JFXButton. Leaves the chosen group chat. Will open a new
     * ConfirmLeaveGroup window/stage to check if you really want to leave the group (since
     * it's an irreversible action).
     *
     * @throws IOException If error when FXMLLoader.load() is called
     */
    @FXML
    private void actionLeaveGroupButton() throws IOException {
        log.info("Bouton \"LEAVE\" appuyé, groupName = \"" + groupName + "\"");

        URL confirmLeaveGroupURL = new File("src/main/pages/confirmLeaveGroup.fxml").toURI().toURL();
        FXMLLoader confirmLeaveGroupLoader = new FXMLLoader(confirmLeaveGroupURL);
        Parent confirmLeaveGroupRoot = confirmLeaveGroupLoader.load();

        ConfirmLeaveGroupController confirmLeaveGroupController = confirmLeaveGroupLoader.getController();
        confirmLeaveGroupController.build(groupName);

        Scene scene = new Scene(confirmLeaveGroupRoot, 300, 150);

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
