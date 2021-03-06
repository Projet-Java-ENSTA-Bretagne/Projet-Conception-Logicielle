import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import java.io.File;
import java.net.URL;

public class GroupSettingsController {

    @FXML
    void initialize() {
        System.out.println("init group settings controller");

        operationType = "joinGroup";
        ipAddress = "";
        port = 0;
        groupName = "";
        groupId = 0;
        groupStatus = "public";
        groupDescription = "";
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

    private String ipAddress;

    @FXML
    private JFXTextField portTextField;

    private int port;

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

    private static int nbCreatedGroups = 0;

    private static HBox discussionHBox = null;

    public static HBox getDiscussionHBox() {
        return discussionHBox;
    }

    @FXML
    void actionDoneButton() {
        System.out.println("\nVous venez d'appuyer sur le bouton \"DONE\"");

        try {
            ipAddress = ipAddressTextField.getText();
            port = Integer.parseInt(portTextField.getText());
            groupName = groupNameTextField.getText();
            groupId = Integer.parseInt(groupIdTextField.getText());

            String wholeDescription = groupDescriptionTextField.getText();
            if ((wholeDescription != null) && (wholeDescription.length() > 0)) {
                // be **careful** when you're using the substring function ...
                groupDescription = wholeDescription.substring(0, Math.min(wholeDescription.length(), 40));
            }
            else {
                groupDescription = "";
            }

            System.out.printf("\nType de l'operation : %s", operationType);
            System.out.printf("\nAdresse IP du serveur : %s", ipAddress);
            System.out.printf("\nPort du serveur : %d", port);
            System.out.printf("\nNom du groupe : %s", groupName);
            System.out.printf("\nID du groupe : %d", groupId);
            System.out.printf("\nStatut du groupe : %s", groupStatus);
            System.out.printf("\nDescription du groupe : %s\n", groupDescription);

            HomeController.getCurrentGroupSettingsStage().close();
            HomeController.setCurrentGroupSettingsStage(null);

            /* ---------------------------------------------------------- */

            // adding new group visualizer

            nbCreatedGroups += 1;

            URL groupVisualizerURL = new File("src/main/pages/groupVisualizer.fxml").toURI().toURL();
            FXMLLoader groupVisualizerLoader = new FXMLLoader(groupVisualizerURL);
            GroupVisualizerController groupVisualizerController = new GroupVisualizerController(nbCreatedGroups);
            groupVisualizerLoader.setController(groupVisualizerController);
            AnchorPane groupVisualizerRoot = groupVisualizerLoader.load();

            // as an example for now --> the "lookup" method is OP
            JFXButton openGroupButton = (JFXButton) groupVisualizerRoot.lookup("#openGroupButton");
            openGroupButton.setOnAction(e -> System.out.printf("\nBouton \"OPEN\" appuye, groupNumber = %d", groupVisualizerController.getGroupNumber()));

            JFXButton leaveGroupButton = (JFXButton) groupVisualizerRoot.lookup("#leaveGroupButton");
            leaveGroupButton.setOnAction(e -> System.out.printf("\nBouton \"LEAVE\" appuye, groupNumber = %d", groupVisualizerController.getGroupNumber()));

            if (nbCreatedGroups == 1) {
                discussionHBox = (HBox) MainController.getHomeScene().lookup("#discussionHBox");
                discussionHBox.setSpacing(40); // 1 cm = 40 px, et scrollbarHeight = 18 px (d'où v2 = 58 = 40 + 18)
                discussionHBox.setPadding(new Insets(40, 40, 58, 40));
            }

            discussionHBox.getChildren().add(groupVisualizerRoot);
        }
        catch (Exception e) {
            System.out.println("Parametrage invalide !\n" + e);
        }
    }

    //

}
