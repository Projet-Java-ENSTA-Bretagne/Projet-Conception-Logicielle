package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Class handling the JavaFX objects from the ConfirmLeaveGroup secondary stage (defined
 * in confirmLeaveGroup.fxml).
 */
public class ConfirmLeaveGroupController {
    // Logging
    private final Logger log = LogManager.getLogger(ConfirmLeaveGroupController.class);

    /**
     * Method that is automatically executed right after "confirmLeaveGroup.fxml" is loaded.
     */
    @FXML
    private void initialize() {
        // useless method atm (but can potentially be useful in the future)
    }

    private String groupName;

    @FXML
    private Label groupNameLabel;

    // it's cleaner if we do NOT use constructors when updating FXML templates
    public void build(String groupName) {
        this.groupName = groupName;

        log.info("Initializing confirmLeaveGroup controller, groupName = \"" + this.groupName + "\"");

        updateDesign();
    }

    private void updateDesign() {
        groupNameLabel.setText("\"" + groupName + "\" ?");
    }

    /**
     * Action linked to the "Non" JFXButton. Clicking this button means that you actually chose
     * not to leave the group. Thus no further action will be taken.
     */
    @FXML
    private void actionNoButton() {
        log.info("Bouton \"Non\" appuyé (confirmLeaveGroup), groupName = \"" + groupName + "\"");
        HomeController.closeCurrentConfirmLeaveGroupStage();
    }

    /**
     * Action linked to the "Oui" JFXButton. Effectively leaves the selected group.
     * TODO : Link this method to network
     */
    @FXML
    private void actionYesButton() {
        log.info("Bouton \"Oui\" appuyé (confirmLeaveGroup), groupName = \"" + groupName + "\"");
        HomeController.closeCurrentConfirmLeaveGroupStage();

        HomeController.aGroupIsCurrentlyBeingDeleted = true;

        HomeController.deleteGroupThumbnailByGroupName(groupName);
        DiscussionController.deleteGroupObjectByGroupName(groupName);

        // if HomeController.getNbGroupsYouAreStillPartOf() > 2, the boolean
        // "HomeController.aGroupIsCurrentlyBeingDeleted" will be automatically set to "false"
        // by the ChangeListener "observing" the width of the group thumbnail HBox (cf. the method
        // "HomeController.initializeStaticComponents")
        if (HomeController.getNbGroupsYouAreStillPartOf() <= 2) {
            HomeController.aGroupIsCurrentlyBeingDeleted = false;
        }
    }
}
