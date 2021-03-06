import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class GroupSettingsController {

    @FXML
    void initialize() {
        System.out.println("init group settings");

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
        System.out.println("join");
    }

    @FXML
    void actionCreateGroupRadioButton() {
        setOperationType("createGroup");
        System.out.println("create");
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
        System.out.println("public");
    }

    @FXML
    void actionPrivateRadioButton() {
        setGroupStatus("private");
        System.out.println("private");
    }

    /* -------------------------------------------- */

    @FXML
    private JFXTextField groupDescriptionTextField;

    private String groupDescription;

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
                groupDescription = wholeDescription.substring(0, Math.min(wholeDescription.length(), 50));
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
        }
        catch (Exception e) {
            System.out.println("Parametrage invalide !\n" + e);
        }
    }

    //

}
