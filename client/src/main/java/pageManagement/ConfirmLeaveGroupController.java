package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;

public class ConfirmLeaveGroupController {
    /**
     * Class handling the JavaFX objects from the ConfirmLeaveGroup secondary stage (defined
     * in confirmLeaveGroup.fxml).
     */

    // Logging
    private final Logger log = LogManager.getLogger(ConfirmLeaveGroupController.class);

    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public ConfirmLeaveGroupController(String groupName) {
        this.groupName = groupName;
    }

    @FXML
    void initialize() {
        /**
         * Method that is executed right before "confirmLeaveGroup.fxml" is loaded.
         * @param void
         * @return void
         */

        log.info("Initializing confirmLeaveGroup controller, groupName = \"" + getGroupName() + "\"");
    }

    // /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
    public void actionYesButton() {
        /**
         * Action linked to the "Oui" JFXButton.
         * Effectively leaves the chosen group.
         * @param void
         * @return void
         * TODO : Link this method to network
         */

        log.info("Bouton \"Oui\" appuye (confirmLeaveGroup), groupName = \"" + getGroupName() + "\"");
        HomeController.closeCurrentConfirmLeaveGroupStage();
        HomeController.deleteGroupByName(getGroupName());
    }

    // /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
    public void actionNoButton() {
        /**
         * Action linked to the "Non" JFXButton.
         * Clicking this button means that you actually chose not to leave the group. Thus
         * no further action will be taken.
         * @param void
         * @return void
         */

        log.info("Bouton \"Non\" appuye (confirmLeaveGroup), groupName = \"" + getGroupName() + "\"");
        HomeController.closeCurrentConfirmLeaveGroupStage();
    }

    //

}
