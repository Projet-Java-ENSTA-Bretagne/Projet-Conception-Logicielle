package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.net.URL;

/**
 * Class handling the JavaFX objects from the GroupSettings secondary stage (defined
 * in groupSettings.fxml).
 */
public class GroupSettingsController {
    // Logging
    private final Logger log = LogManager.getLogger(GroupSettingsController.class);

    /**
     * Method that is automatically executed right after "groupSettings.fxml" is loaded.
     */
    @FXML
    private void initialize() {
        log.info("Initializing group settings controller");

        operationType = OperationType.JOIN_GROUP;
        groupStatus = GroupStatus.PUBLIC;
    }

    /* ------------- 1st toggle group : Operation Type ------------- */

    public enum OperationType {
        JOIN_GROUP,
        CREATE_GROUP,
        CREATE_PM
    }

    private OperationType operationType;

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    @FXML
    private JFXRadioButton joinGroupRadioButton;

    @FXML
    private void actionJoinGroupRadioButton() {
        setOperationType(OperationType.JOIN_GROUP);
        log.debug("Operation type : " + operationType.toString());
    }

    @FXML
    private JFXRadioButton createGroupRadioButton;

    @FXML
    private void actionCreateGroupRadioButton() {
        setOperationType(OperationType.CREATE_GROUP);
        log.debug("Operation type : " + operationType.toString());
    }

    @FXML
    private JFXRadioButton createPmRadioButton;

    @FXML
    private void actionCreatePmRadioButton() {
        setOperationType(OperationType.CREATE_PM);
        log.debug("Operation type : " + operationType.toString());

        if (publicGroupStatusRadioButton.isSelected()) {
            publicGroupStatusRadioButton.setSelected(false);

            privateGroupStatusRadioButton.setSelected(true);
            setGroupStatus(GroupStatus.PRIVATE);
            log.debug("Group status : " + groupStatus.toString());
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

    public enum GroupStatus {
        PUBLIC,
        PRIVATE
    }

    private GroupStatus groupStatus;

    public void setGroupStatus(GroupStatus groupStatus) {
        this.groupStatus = groupStatus;
    }

    @FXML
    private JFXRadioButton publicGroupStatusRadioButton;

    @FXML
    private void actionPublicGroupStatusRadioButton() {
        if (operationType == OperationType.CREATE_PM) {
            log.warn("A PM discussion cannot be public !");

            publicGroupStatusRadioButton.setSelected(false);

            privateGroupStatusRadioButton.setSelected(true);
            setGroupStatus(GroupStatus.PRIVATE);
        }
        else {
            setGroupStatus(GroupStatus.PUBLIC);
        }

        log.debug("Group status : " + groupStatus.toString());
    }

    @FXML
    private JFXRadioButton privateGroupStatusRadioButton;

    @FXML
    private void actionPrivateGroupStatusRadioButton() {
        setGroupStatus(GroupStatus.PRIVATE);
        log.debug("Group status : " + groupStatus.toString());
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

                // here we check if the group name entry already exists
                for (GroupThumbnailObject groupThumbnailObject : groupThumbnailObjectList) {
                    String otherGroupName = groupThumbnailObject.getController().getGroupName();
                    if (groupName.equals(otherGroupName)) {
                        parametersAreValid = false;
                        System.out.println("");
                        log.error("Le nom de groupe \"" + groupName + "\" existe déjà !");
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
                // here we check if the group ID entry already exists
                for (GroupThumbnailObject groupThumbnailObject : groupThumbnailObjectList) {
                    int otherGroupId = groupThumbnailObject.getController().getGroupId();
                    if (groupId == otherGroupId) {
                        parametersAreValid = false;
                        System.out.println("");
                        log.error("L'ID de groupe \"" + groupId + "\" existe déjà !");
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
            log.error("Paramétrage invalide ! Erreur : " + e);
        }

        if (parametersAreValid) {
            System.out.println("");
            log.debug("Type de l'opération : " + operationType.toString());
            log.debug("Adresse IP du serveur : " + serverIpAddress);
            log.debug("Port du serveur : " + serverPort);
            log.debug("Nom du groupe : \"" + groupName + "\"");
            log.debug("ID du groupe : " + groupId);
            log.debug("Statut du groupe : " + groupStatus.toString());
            log.debug("Description du groupe : \"" + groupDescription + "\"");

            HomeController.closeCurrentGroupSettingsStage();

            /* ---------------------------------------------------------- */

            // adding new group visualizer/thumbnail

            HomeController.incrementNbGroupsYouAreStillPartOf();

            URL groupVisualizerURL = new File("src/main/pages/groupThumbnail.fxml").toURI().toURL();
            FXMLLoader groupThumbnailLoader = new FXMLLoader(groupVisualizerURL);
            Parent groupThumbnailRoot = groupThumbnailLoader.load();

            GroupThumbnailController groupThumbnailController = groupThumbnailLoader.getController();
            groupThumbnailController.build(groupName, groupStatus, groupDescription, operationType,
                                           serverIpAddress, serverPort, groupId);

            GroupThumbnailObject newGroupThumbnailObject = new GroupThumbnailObject(groupThumbnailController, groupThumbnailRoot);
            HomeController.addGroup(newGroupThumbnailObject);

            GroupObject newGroupObject = new GroupObject(groupName);
            DiscussionController.addGroupObject(newGroupObject);

            System.out.println("");
            if (operationType == OperationType.JOIN_GROUP) {
                log.debug("Vous venez de rejoindre le groupe \"" + groupName + "\" !\n");
            }
            else if (operationType == OperationType.CREATE_GROUP) {
                log.debug("Vous venez de créer le groupe \"" + groupName + "\" !\n");
            }
            else if (operationType == OperationType.CREATE_PM) {
                log.debug("Vous venez de créer une page de discussion privée avec l'utilisateur \"" + groupName + "\" !\n");
            }
        }

        else {
            if (!anErrorWasCaught) {
                System.out.println("");
                log.error("Paramétrage invalide !");
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
