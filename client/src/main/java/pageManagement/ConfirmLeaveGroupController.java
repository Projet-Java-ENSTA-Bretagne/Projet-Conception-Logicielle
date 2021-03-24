package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;

/**
 * Class handling the JavaFX objects from the ConfirmLeaveGroup secondary stage (defined
 * in confirmLeaveGroup.fxml).
 */
public class ConfirmLeaveGroupController {
    // Logging
    private final Logger log = LogManager.getLogger(ConfirmLeaveGroupController.class);

    /**
     * Method that is executed right before "confirmLeaveGroup.fxml" is loaded.
     */
    @FXML
    private void initialize() {
        log.info("Initializing confirmLeaveGroup controller, groupName = \"" + getGroupName() + "\"");
    }

    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public ConfirmLeaveGroupController(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Action linked to the "Non" JFXButton.
     * Clicking this button means that you actually chose not to leave the group. Thus
     * no further action will be taken.
     * /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
     */
    public void actionNoButton() {
        log.info("Bouton \"Non\" appuye (confirmLeaveGroup), groupName = \"" + getGroupName() + "\"");
        HomeController.closeCurrentConfirmLeaveGroupStage();
    }

    /**
     * Action linked to the "Oui" JFXButton.
     * Effectively leaves the chosen group.
     * /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
     * TODO : Link this method to network
     */
    public void actionYesButton() {
        log.info("Bouton \"Oui\" appuye (confirmLeaveGroup), groupName = \"" + getGroupName() + "\"");

        HomeController.aGroupIsCurrentlyBeingDeleted = true;

        HomeController.closeCurrentConfirmLeaveGroupStage();
        HomeController.deleteGroupThumbnailByGroupName(getGroupName());
        DiscussionController.deleteGroupObjectByGroupName(getGroupName());

        // if HomeController.getNbGroupsYouAreStillPartOf() > 2, the boolean
        // "HomeController.aGroupIsCurrentlyBeingDeleted" will be automatically set to "false"
        // by the ChangeListener "observing" the width of the group thumbnail HBox (cf. the method
        // "HomeController.initializeGroupThumbnailScrollPane")
        if (HomeController.getNbGroupsYouAreStillPartOf() <= 2) {
            HomeController.aGroupIsCurrentlyBeingDeleted = false;
        }
    }
}
