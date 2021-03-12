import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class GroupSettingsController {

    // Logging
    private final Logger log = LogManager.getLogger(GroupSettingsController.class);

    @FXML
    void initialize() {
        log.info("Initializing group settings controller");

        operationType = "joinGroup";
        groupStatus = "public";
    }

    /* ------------- 1st toggle group ------------- */

    private String operationType;

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @FXML
    void actionJoinGroupRadioButton() {
        setOperationType("joinGroup");
        log.debug(operationType);
    }

    @FXML
    void actionCreateGroupRadioButton() {
        setOperationType("createGroup");
        log.debug(operationType);
    }

    /* -------------------------------------------- */

    @FXML
    private JFXTextField ipAddressTextField;

    private String serverIpAddress;

    @FXML
    private JFXTextField portTextField;

    private int serverPort;

    @FXML
    private JFXTextField groupNameTextField;

    private String groupName;

    @FXML
    private JFXTextField groupIdTextField;

    private int groupId;

    /* ------------- 2nd toggle group ------------- */

    private String groupStatus;

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    @FXML
    void actionPublicRadioButton() {
        setGroupStatus("public");
        log.debug("Group status : " + groupStatus);
    }

    @FXML
    void actionPrivateRadioButton() {
        setGroupStatus("private");
        log.debug("Group status : " + groupStatus);
    }

    /* -------------------------------------------- */

    @FXML
    private JFXTextField groupDescriptionTextField;

    private String groupDescription;

    @FXML
    void actionDoneButton() {
        log.info("Vous venez d'appuyer sur le bouton \"DONE\"");

        boolean parametersAreValid = true;

        try {
            serverIpAddress = ipAddressTextField.getText();
            if ((serverIpAddress == null) || (serverIpAddress.length() == 0)) {
                parametersAreValid = false;
            }

            serverPort = Integer.parseInt(portTextField.getText());
            if (serverPort <= 0) {
                parametersAreValid = false;
            }

            String wholeGroupName = groupNameTextField.getText();
            if ((wholeGroupName == null) || (wholeGroupName.length() == 0)) {
                parametersAreValid = false;
            }
            else {
                // the group name has to be less than 15 characters
                groupName = wholeGroupName.substring(0, Math.min(wholeGroupName.length(), 15));

                // here we check if the group name already exists
                ArrayList<GroupVisualizerObject> groupObjectList = HomeController.getGroupObjectList();
                for (GroupVisualizerObject groupVisualizerObject : groupObjectList) {
                    String otherGroupName = groupVisualizerObject.getController().getGroupName();
                    if (groupName.equals(otherGroupName)) {
                        parametersAreValid = false;
                        log.error("Le nom de groupe \"" + groupName + "\" existe deja !\n");
                        break;
                    }
                }
            }

            groupId = Integer.parseInt(groupIdTextField.getText());
            if (groupId <= 0) {
                parametersAreValid = false;
            }

            String wholeDescription = groupDescriptionTextField.getText();
            if ((wholeDescription != null) && (wholeDescription.length() > 0)) {
                // the group description has to be less than 30 characters
                groupDescription = wholeDescription.substring(0, Math.min(wholeDescription.length(), 30));
            }
            else {
                groupDescription = "[Aucune description]";
            }

            if (parametersAreValid) {
                System.out.println("");
                log.debug("Type de l'operation : \"" + operationType + "\"");
                log.debug("Adresse IP du serveur : \"" + serverIpAddress + "\"");
                log.debug("Port du serveur : " + serverPort);
                log.debug("Nom du groupe : \"" + groupName + "\"");
                log.debug("ID du groupe : " + groupId);
                log.debug("Statut du groupe : \"" + groupStatus + "\"");
                log.debug("Description du groupe : \"" + groupDescription + "\"\n");

                HomeController.getCurrentGroupSettingsStage().close();
                HomeController.setCurrentGroupSettingsStage(null);

                /* ---------------------------------------------------------- */

                // adding new group visualizer/thumbnail

                HomeController.setNbCreatedGroups(HomeController.getNbCreatedGroups() + 1);
                HomeController.setNbGroupsYouAreStillPartOf(HomeController.getNbGroupsYouAreStillPartOf() + 1);

                URL groupVisualizerURL = new File("src/main/pages/groupVisualizer.fxml").toURI().toURL();
                FXMLLoader groupVisualizerLoader = new FXMLLoader(groupVisualizerURL);
                GroupVisualizerController groupVisualizerController = new GroupVisualizerController(groupName, groupStatus, groupDescription,
                                                                          operationType, serverIpAddress, serverPort, groupId);
                groupVisualizerLoader.setController(groupVisualizerController);
                Parent groupVisualizerRoot = groupVisualizerLoader.load();

                JFXButton openGroupButton = (JFXButton) groupVisualizerRoot.lookup("#openGroupButton");
                openGroupButton.setOnAction(e -> groupVisualizerController.actionOpenGroupButton());

                JFXButton leaveGroupButton = (JFXButton) groupVisualizerRoot.lookup("#leaveGroupButton");
                leaveGroupButton.setOnAction(e -> {
                    try {
                        groupVisualizerController.actionLeaveGroupButton();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });

                // this seems to be the only way to get the **NOT-NULL** discussion HBox from the Home scene
                if (HomeController.getNbCreatedGroups() == 1) {
                    HomeController.setDiscussionHBox((HBox) MainController.getHomeScene().lookup("#discussionHBox"));
                }

                Label groupNameLabel = (Label) groupVisualizerRoot.lookup("#groupNameLabel");
                groupNameLabel.setText(groupName);

                Label groupStatusLabel = (Label) groupVisualizerRoot.lookup("#groupStatusLabel");
                if (groupStatus.equals("public")) {
                    groupStatusLabel.setText("Groupe public");
                }
                else if (groupStatus.equals("private")) {
                    groupStatusLabel.setText("Groupe privé");
                }

                Label groupDescriptionLabel = (Label) groupVisualizerRoot.lookup("#groupDescriptionLabel");
                groupDescriptionLabel.setText(groupDescription);

                HomeController.getDiscussionHBox().getChildren().add(groupVisualizerRoot);

                GroupVisualizerObject groupVisualizerObject = new GroupVisualizerObject(groupVisualizerController, groupVisualizerRoot);
                HomeController.getGroupObjectList().add(groupVisualizerObject);

                System.out.println("");
                if (operationType.equals("joinGroup")) {
                    log.debug("Vous venez de rejoindre le groupe \"" + groupName + "\" !\n");
                }
                else if (operationType.equals("createGroup")) {
                    log.debug("Vous venez de creer le groupe \"" + groupName + "\" !\n");
                }
            }

            else {
                System.out.println("");
                log.error("Parametrage invalide !\n");
            }
        }
        catch (Exception e) {
            parametersAreValid = false; // just in case
            System.out.println("");
            log.error("Parametrage invalide ! Erreur : " + e + "\n");
        }
    }

    //

}
