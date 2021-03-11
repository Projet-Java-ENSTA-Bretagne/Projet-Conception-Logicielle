import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

// class handling the group thumbnails that will be added to the Home page
public class GroupVisualizerController {

    private String groupName;
    private String groupStatus;
    private String groupDescription;

    public String getGroupName() {
        return groupName;
    }

    public GroupVisualizerController(String groupName, String groupStatus, String groupDescription) {
        this.groupName = groupName;
        this.groupStatus = groupStatus;
        this.groupDescription = groupDescription;
    }

    @FXML
    void initialize() {
        System.out.printf("\nInitializing group visualizer controller, groupName = \"%s\"", groupName);
    }

    // /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
    public void actionOpenGroupButton() {
        System.out.printf("\nBouton \"OPEN\" appuye, groupName = \"%s\"", getGroupName());
    }

    // /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
    public void actionLeaveGroupButton() throws IOException {
        System.out.printf("\nBouton \"LEAVE\" appuye, groupName = \"%s\"", getGroupName());

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

        Stage secondaryStage = new Stage(); // new stage for ConfirmLeaveGroup page
        secondaryStage.getIcons().add(new Image("question-mark.png"));
        secondaryStage.setTitle("Confirmation");
        secondaryStage.setScene(scene);
        secondaryStage.initModality(Modality.APPLICATION_MODAL);

        HomeController.setCurrentConfirmLeaveGroupStage(secondaryStage);

        secondaryStage.show();
    }

    //

}
