//

import javafx.fxml.FXML;

public class ConfirmLeaveGroupController {

    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public ConfirmLeaveGroupController(String groupName) {
        this.groupName = groupName;
    }

    @FXML
    void initialize() {
        System.out.printf("\nInitializing confirmLeaveGroup controller, groupName = \"%s\"", getGroupName());
    }

    // /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
    public void actionYesButton() {
        System.out.printf("\nBouton \"Oui\" appuye (confirmLeaveGroup), groupName = \"%s\"", getGroupName());

        HomeController.getCurrentConfirmLeaveGroupStage().close();
        HomeController.setCurrentConfirmLeaveGroupStage(null);
        HomeController.deleteGroupByName(getGroupName());
    }

    // /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
    public void actionNoButton() {
        System.out.printf("\nBouton \"Non\" appuye (confirmLeaveGroup), groupName = \"%s\"", getGroupName());

        HomeController.getCurrentConfirmLeaveGroupStage().close();
        HomeController.setCurrentConfirmLeaveGroupStage(null);
    }

    //

}
