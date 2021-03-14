package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;

public class ConfirmLeaveGroupController {

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
        log.info("Initializing confirmLeaveGroup controller, groupName = \"" + getGroupName() + "\"");
    }

    // /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
    public void actionYesButton() {
        log.info("Bouton \"Oui\" appuye (confirmLeaveGroup), groupName = \"" + getGroupName() + "\"");
        HomeController.closeCurrentConfirmLeaveGroupStage();
        HomeController.deleteGroupByName(getGroupName());
    }

    // /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
    public void actionNoButton() {
        log.info("Bouton \"Non\" appuye (confirmLeaveGroup), groupName = \"" + getGroupName() + "\"");
        HomeController.closeCurrentConfirmLeaveGroupStage();
    }

    //

}
