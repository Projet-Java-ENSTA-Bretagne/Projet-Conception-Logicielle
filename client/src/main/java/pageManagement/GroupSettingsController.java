package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

/**
 * Class handling the JavaFX objects from the GroupSettings secondary stage (defined
 * in groupSettings.fxml).
 */
public class GroupSettingsController {
    // Logging
    private final Logger log = LogManager.getLogger(GroupSettingsController.class);

    /**
     * Method that is executed right before "groupSettings.fxml" is loaded.
     */
    @FXML
    private void initialize() {
        log.info("Initializing group settings controller");

        operationType = "joinGroup";
        groupStatus = "public";
    }

    /* ------------- 1st toggle group : Operation Type ------------- */

    private String operationType;

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @FXML
    private void actionJoinGroupRadioButton() {
        setOperationType("joinGroup");
        log.debug("Operation type : " + operationType);
    }

    @FXML
    private void actionCreateGroupRadioButton() {
        setOperationType("createGroup");
        log.debug("Operation type : " + operationType);
    }

    @FXML
    private void actionCreatePmRadioButton() {
        setOperationType("createPm");
        log.debug("Operation type : " + operationType);

        JFXRadioButton publicGroupStatusRadioButton = (JFXRadioButton) HomeController.getCurrentGroupSettingsRoot().lookup("#publicGroupStatusRadioButton");
        if (publicGroupStatusRadioButton.isSelected()) {
            publicGroupStatusRadioButton.setSelected(false);

            JFXRadioButton privateGroupStatusRadioButton = (JFXRadioButton) HomeController.getCurrentGroupSettingsRoot().lookup("#privateGroupStatusRadioButton");
            privateGroupStatusRadioButton.setSelected(true);
            setGroupStatus("private");
            log.debug("Group status : " + groupStatus);
        }
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

    /* ------------- 2nd toggle group : Group Status ------------- */

    private String groupStatus;

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    @FXML
    private void actionPublicGroupStatusRadioButton() {
        if (operationType.equals("createPm")) {
            log.warn("A PM discussion cannot be public !");

            JFXRadioButton publicGroupStatusRadioButton = (JFXRadioButton) HomeController.getCurrentGroupSettingsRoot().lookup("#publicGroupStatusRadioButton");
            publicGroupStatusRadioButton.setSelected(false);

            JFXRadioButton privateGroupStatusRadioButton = (JFXRadioButton) HomeController.getCurrentGroupSettingsRoot().lookup("#privateGroupStatusRadioButton");
            privateGroupStatusRadioButton.setSelected(true);
            setGroupStatus("private");
            log.debug("Group status : " + groupStatus);
        }
        else {
            setGroupStatus("public");
            log.debug("Group status : " + groupStatus);
        }
    }

    @FXML
    private void actionPrivateGroupStatusRadioButton() {
        setGroupStatus("private");
        log.debug("Group status : " + groupStatus);
    }

    /* -------------------------------------------- */

    @FXML
    private JFXTextField groupDescriptionTextField;

    private String groupDescription;

    /* -------------------------------------------- */

    /**
     * Action linked to the "DONE" JFXButton.
     * Checks if the group settings are valid, then, according to the chosen
     * operation type, creates a new group or connects the current tcpClient
     * to the desired group chat (or to the desired PM chat).
     * TODO : Link this method to network
     *
     * @throws IOException If error when FXMLLoader.load() is called
     */
    @FXML
    private void actionDoneButton() throws IOException {
        log.info("Vous venez d'appuyer sur le bouton \"DONE\"");

        boolean parametersAreValid = true;
        boolean anErrorWasCaught = false;

        try {
            // just in case
            if (operationType.equals("createPm")) {
                setGroupStatus("private");
            }

            serverIpAddress = ipAddressTextField.getText();
            if ((serverIpAddress == null) || (serverIpAddress.length() == 0)) {
                log.error("L'adresse IP fournie est vide !");
                parametersAreValid = false;
            }
            else {
                if (!ipAddressIsValid(serverIpAddress)) {
                    log.error("Adresse IPv4 invalide !");
                    parametersAreValid = false;
                }
            }

            serverPort = Integer.parseInt(portTextField.getText());
            if (serverPort <= 0) {
                log.error("Le port fourni est négatif ! Il doit être strictement positif");
                parametersAreValid = false;
            }

            ArrayList<GroupThumbnailObject> groupThumbnailObjectList = HomeController.getGroupThumbnailObjectList();

            String wholeGroupName = groupNameTextField.getText();
            if ((wholeGroupName == null) || (wholeGroupName.length() == 0)) {
                log.error("Le nom de groupe fourni est vide !");
                parametersAreValid = false;
            }
            else {
                // the group name has to be less than 12 characters
                groupName = wholeGroupName.substring(0, Math.min(wholeGroupName.length(), 12));

                // here we check if the group name (or group ID) already exists
                for (GroupThumbnailObject groupThumbnailObject : groupThumbnailObjectList) {
                    String otherGroupName = groupThumbnailObject.getController().getGroupName();
                    if (groupName.equals(otherGroupName)) {
                        parametersAreValid = false;
                        System.out.println("");
                        log.error("Le nom de groupe \"" + groupName + "\" existe deja !");
                        break;
                    }
                }
            }

            groupId = Integer.parseInt(groupIdTextField.getText());
            if (groupId <= 0) {
                log.error("L'ID de groupe fourni est négatif ! Il doit être strictement positif");
                parametersAreValid = false;
            }
            else {
                // here we check if the group ID already exists
                for (GroupThumbnailObject groupThumbnailObject : groupThumbnailObjectList) {
                    int otherGroupId = groupThumbnailObject.getController().getGroupId();
                    if (groupId == otherGroupId) {
                        parametersAreValid = false;
                        System.out.println("");
                        log.error("L'ID de groupe \"" + groupId + "\" existe deja !");
                        break;
                    }
                }
            }

            String wholeDescription = groupDescriptionTextField.getText();
            if ((wholeDescription != null) && (wholeDescription.length() > 0)) {
                // the group description has to be less than 30 characters
                groupDescription = wholeDescription.substring(0, Math.min(wholeDescription.length(), 30));
            }
            else {
                groupDescription = "[Aucune description]";
            }
        }
        catch (Exception e) {
            parametersAreValid = false;
            anErrorWasCaught = true;
            System.out.println("");
            log.error("Parametrage invalide ! Erreur : " + e);
        }

        if (parametersAreValid) {
            System.out.println("");
            log.debug("Type de l'operation : \"" + operationType + "\"");
            log.debug("Adresse IP du serveur : " + serverIpAddress);
            log.debug("Port du serveur : " + serverPort);
            log.debug("Nom du groupe : \"" + groupName + "\"");
            log.debug("ID du groupe : " + groupId);
            log.debug("Statut du groupe : \"" + groupStatus + "\"");
            log.debug("Description du groupe : \"" + groupDescription + "\"");

            HomeController.closeCurrentGroupSettingsStage();

            /* ---------------------------------------------------------- */

            // adding new group visualizer/thumbnail

            HomeController.incrementNbGroupsYouAreStillPartOf();

            URL groupVisualizerURL = new File("src/main/pages/groupThumbnail.fxml").toURI().toURL();
            FXMLLoader groupThumbnailLoader = new FXMLLoader(groupVisualizerURL);
            GroupThumbnailController groupThumbnailController = new GroupThumbnailController(groupName, groupStatus, groupDescription,
                                                                    operationType, serverIpAddress, serverPort, groupId);
            groupThumbnailLoader.setController(groupThumbnailController);
            Parent groupThumbnailRoot = groupThumbnailLoader.load();

            JFXButton openGroupButton = (JFXButton) groupThumbnailRoot.lookup("#openGroupButton");
            openGroupButton.setOnAction(e -> {
                try {
                    groupThumbnailController.actionOpenGroupButton();
                } catch (IOException ioException) {
                    log.error("Erreur lors de l'appel au bouton \"OPEN\" (groupName = \"" + groupName + "\") : " + ioException);
                }
            });

            JFXButton leaveGroupButton = (JFXButton) groupThumbnailRoot.lookup("#leaveGroupButton");
            leaveGroupButton.setOnAction(e -> {
                try {
                    groupThumbnailController.actionLeaveGroupButton();
                } catch (IOException ioException) {
                    log.error("Erreur lors de l'appel au bouton \"LEAVE\" (groupName = \"" + groupName + "\") : " + ioException);
                }
            });

            Label groupNameLabel = (Label) groupThumbnailRoot.lookup("#groupNameLabel");
            groupNameLabel.setText(groupName);

            Label groupStatusLabel = (Label) groupThumbnailRoot.lookup("#groupStatusLabel");
            if (operationType.equals("createPm")) {
                groupStatusLabel.setText("Messages privés (MP)");
            }
            else {
                if (groupStatus.equals("public")) {
                    groupStatusLabel.setText("Groupe public");
                }
                else if (groupStatus.equals("private")) {
                    groupStatusLabel.setText("Groupe privé");
                }
            }

            Label groupDescriptionLabel = (Label) groupThumbnailRoot.lookup("#groupDescriptionLabel");
            groupDescriptionLabel.setText(groupDescription);

            GroupThumbnailObject newGroupThumbnailObject = new GroupThumbnailObject(groupThumbnailController, groupThumbnailRoot);
            HomeController.addGroup(newGroupThumbnailObject);

            GroupObject newGroupObject = new GroupObject(groupName);
            DiscussionController.addGroupObject(newGroupObject);

            System.out.println("");
            if (operationType.equals("joinGroup")) {
                log.debug("Vous venez de rejoindre le groupe \"" + groupName + "\" !\n");
            }
            else if (operationType.equals("createGroup")) {
                log.debug("Vous venez de creer le groupe \"" + groupName + "\" !\n");
            }
            else if (operationType.equals("createPm")) {
                log.debug("Vous venez de creer une page de discussion privée avec l'utilisateur \"" + groupName + "\" !\n");
            }
        }

        else {
            if (!anErrorWasCaught) {
                System.out.println("");
                log.error("Parametrage invalide !");
            }
        }
    }

    /* -------------------------------------------- */

    /**
     * Checks if a given IPv4 address is valid.
     *
     * @param ipAddress The IPv4 address to check
     * @return Boolean indicating whether the IPv4 address is valid
     */
    private boolean ipAddressIsValid(String ipAddress) {
        String[] splitIpAddress = ipAddress.split("\\.");

        if (splitIpAddress.length != 4) {
            return false;
        }

        else {
            try {
                for (String ipComponent : splitIpAddress) {
                    int associatedInteger = Integer.parseInt(ipComponent);

                    if ((associatedInteger < 0) || (associatedInteger > 255)) {
                        return false;
                    }

                    // for instance, ipComponent = "013" wouldn't be valid (whereas "13" would be)
                    if ((associatedInteger != 0) && (ipComponent.startsWith("0"))) {
                        return false;
                    }
                }

                if (ipAddress.endsWith(".")) {
                    return false;
                }

                return true;
            }
            catch (NumberFormatException nfe) {
                return false;
            }
        }
    }
}
