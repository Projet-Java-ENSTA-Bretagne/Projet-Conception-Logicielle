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

    @FXML
    void initialize() {
        System.out.println("Initializing group settings controller");

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
        System.out.println(operationType);
    }

    @FXML
    void actionCreateGroupRadioButton() {
        setOperationType("createGroup");
        System.out.println(operationType);
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
        System.out.println(groupStatus);
    }

    @FXML
    void actionPrivateRadioButton() {
        setGroupStatus("private");
        System.out.println(groupStatus);
    }

    /* -------------------------------------------- */

    @FXML
    private JFXTextField groupDescriptionTextField;

    private String groupDescription;

    @FXML
    void actionDoneButton() {
        System.out.println("\nVous venez d'appuyer sur le bouton \"DONE\"");

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
                        System.out.printf("\nLe nom de groupe \"%s\" existe deja !\n", groupName);
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
                // the group description has to be less than 40 characters
                groupDescription = wholeDescription.substring(0, Math.min(wholeDescription.length(), 40));
            }
            else {
                groupDescription = "[Aucune description]";
            }

            if (parametersAreValid) {
                System.out.printf("\nType de l'operation : \"%s\"", operationType);
                System.out.printf("\nAdresse IP du serveur : \"%s\"", serverIpAddress);
                System.out.printf("\nPort du serveur : %d", serverPort);
                System.out.printf("\nNom du groupe : \"%s\"", groupName);
                System.out.printf("\nID du groupe : %d", groupId);
                System.out.printf("\nStatut du groupe : \"%s\"", groupStatus);
                System.out.printf("\nDescription du groupe : \"%s\"\n", groupDescription);

                HomeController.getCurrentGroupSettingsStage().close();
                HomeController.setCurrentGroupSettingsStage(null);

                /* ---------------------------------------------------------- */

                // adding new group visualizer/thumbnail

                HomeController.setNbCreatedGroups(HomeController.getNbCreatedGroups() + 1);
                HomeController.setNbGroupsYouAreStillPartOf(HomeController.getNbGroupsYouAreStillPartOf() + 1);

                URL groupVisualizerURL = new File("src/main/pages/groupVisualizer.fxml").toURI().toURL();
                FXMLLoader groupVisualizerLoader = new FXMLLoader(groupVisualizerURL);
                GroupVisualizerController groupVisualizerController = new GroupVisualizerController(groupName, groupStatus, groupDescription);
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
            }

            else {
                System.out.println("Parametrage invalide !");
            }
        }
        catch (Exception e) {
            parametersAreValid = false;
            System.out.println("Parametrage invalide !\n" + e);
        }
    }

    //

}
