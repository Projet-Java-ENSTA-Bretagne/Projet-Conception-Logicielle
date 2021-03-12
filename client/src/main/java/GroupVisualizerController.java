import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    // Logging
    private final Logger log = LogManager.getLogger(GroupVisualizerController.class);

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

    public GroupVisualizerController(String groupName, String groupStatus, String groupDescription,
                                     String operationType, String serverIpAddress,
                                     int serverPort, int groupId) {

        this.groupName = groupName;
        this.groupStatus = groupStatus;
        this.groupDescription = groupDescription;

        this.operationType = operationType;
        this.serverIpAddress = serverIpAddress;
        this.serverPort = serverPort;
        this.groupId = groupId;
    }

    @FXML
    void initialize() {
        log.info("Initializing group visualizer controller, groupName = \"" + getGroupName() + "\"");
    }

    // /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
    public void actionOpenGroupButton() {
        log.info("Bouton \"OPEN\" appuye, groupName = \"" + getGroupName() + "\"");

        // --> cf. Guillaume
    }

    // /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
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
