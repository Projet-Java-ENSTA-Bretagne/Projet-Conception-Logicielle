package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import java.util.Arrays;
import java.io.IOException;
import java.io.File;
import java.net.URL;

import networking.RequestBuilder;

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

        operationType = OperationTypesEnum.CREATE_GROUP;
        groupStatus = GroupStatusesEnum.PUBLIC;

        /* ---------------------------------------------------------- */

        // defining the selected colour of the JFXRadioButtons (it cannot be done via SceneBuilder,
        // and cannot be done *easily* via a CSS stylesheet)

        // 1st toggle group : operation type
        Color operationTypeSelectedColor = Color.web("#1aa3ff"); // sky blue
        createGroupRadioButton.setSelectedColor(operationTypeSelectedColor);
        createPmRadioButton.setSelectedColor(operationTypeSelectedColor);

        // 2nd toggle group : group status
        Color groupStatusSelectedColor = Color.web("#cc0099"); // light purple
        publicGroupStatusRadioButton.setSelectedColor(groupStatusSelectedColor);
        privateGroupStatusRadioButton.setSelectedColor(groupStatusSelectedColor);
    }

    /* ------------- 1st toggle group : Operation Type ------------- */

    private OperationTypesEnum operationType;

    public void setOperationType(OperationTypesEnum operationType) {
        this.operationType = operationType;
    }

    @FXML
    private JFXRadioButton createGroupRadioButton;

    @FXML
    private void actionCreateGroupRadioButton() {
        setOperationType(OperationTypesEnum.CREATE_GROUP);
        log.debug("Operation type : " + operationType.toString());
    }

    @FXML
    private JFXRadioButton createPmRadioButton;

    @FXML
    private void actionCreatePmRadioButton() {
        setOperationType(OperationTypesEnum.CREATE_PM);
        log.debug("Operation type : " + operationType.toString());

        if (publicGroupStatusRadioButton.isSelected()) {
            publicGroupStatusRadioButton.setSelected(false);

            privateGroupStatusRadioButton.setSelected(true);
            setGroupStatus(GroupStatusesEnum.PRIVATE);
            log.debug("Group status : " + groupStatus.toString());
        }
    }

    /* -------------------------------------------- */

    @FXML
    private JFXTextField groupNameTextField;

    private String groupName;

    @FXML
    private JFXTextField groupMembersTextField;

    private String groupMembers;

    /* ------------- 2nd toggle group : Group Status ------------- */

    private GroupStatusesEnum groupStatus;

    public void setGroupStatus(GroupStatusesEnum groupStatus) {
        this.groupStatus = groupStatus;
    }

    @FXML
    private JFXRadioButton publicGroupStatusRadioButton;

    @FXML
    private void actionPublicGroupStatusRadioButton() {
        if (operationType == OperationTypesEnum.CREATE_PM) {
            log.warn("A PM discussion cannot be public !");

            publicGroupStatusRadioButton.setSelected(false);

            privateGroupStatusRadioButton.setSelected(true);
            setGroupStatus(GroupStatusesEnum.PRIVATE);
        }
        else {
            setGroupStatus(GroupStatusesEnum.PUBLIC);
        }

        log.debug("Group status : " + groupStatus.toString());
    }

    @FXML
    private JFXRadioButton privateGroupStatusRadioButton;

    @FXML
    private void actionPrivateGroupStatusRadioButton() {
        setGroupStatus(GroupStatusesEnum.PRIVATE);
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
        System.out.println("");
        log.info("Vous venez d'appuyer sur le bouton \"DONE\"");
        System.out.println("");

        boolean parametersAreValid = true;
        boolean anErrorWasCaught = false;

        String[] members = new String[0];
        JSONArray userIDs = new JSONArray();

        try {
            String wholeGroupName = groupNameTextField.getText();
            if ((wholeGroupName == null) || (wholeGroupName.length() == 0)) {
                log.error("Le nom de groupe fourni est vide !");
                parametersAreValid = false;
            }
            else {
                // the group name has to be less than 12 characters
                groupName = wholeGroupName.substring(0, Math.min(wholeGroupName.length(), 12));
            }

            /* ---------------------------------------------------------------- */

            // checking (with server) if group members really exist

            groupMembers = groupMembersTextField.getText();
            members = groupMembers.split(", ");

            if (members.length <= 1) {
                log.error("Pas assez de noms d'utilisateurs renseignés ! Il en faut au moins 2");
                parametersAreValid = false;
            }
            else if (!currentSenderIsInMemberList(members)) {
                log.error("Vous ne vous êtes pas inclus dans la liste des membres !");
                parametersAreValid = false;
            }
            else if ((operationType == OperationTypesEnum.CREATE_GROUP) && (members.length == 2)) {
                log.error("Un groupe doit contenir plus de 3 utilisateurs !");
                parametersAreValid = false;
            }
            else if ((operationType == OperationTypesEnum.CREATE_PM) && (members.length > 2)) {
                log.error("Une page de discussion privée (MP) ne doit contenir qu'un seul autre utilisateur !");
                parametersAreValid = false;
            }
            else {
                for (String member : members) {
                    try {
                        JSONObject usernameData = new JSONObject();
                        usernameData.put("user_name", member);

                        String getUserByNameRequest = RequestBuilder.buildWithData("getUserByName", usernameData).toString();
                        String responseFromServer = MainController.getTcpClient().sendRequest(getUserByNameRequest);

                        JSONObject wholeReceivedData = new JSONObject(responseFromServer);
                        String requestStatus = wholeReceivedData.getString("status");

                        if (!requestStatus.equals("OK")) {
                            log.error("L'utilisateur \"" + member + "\" n'existe pas !");
                            parametersAreValid = false;
                            break;
                        }

                        // getting associated user ID
                        JSONObject data = wholeReceivedData.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");
                        String userID = user.getString("id");
                        log.debug(String.format("ID de l'utilisateur \"%s\" : \"%s\"", member, userID));

                        userIDs.put(userID);
                    }
                    catch (JSONException jsonException) {
                        log.error("Erreur JSON détectée : " + jsonException);
                        System.exit(1);
                    }
                }
            }

            /* ---------------------------------------------------------------- */

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
            log.error("Paramétrage invalide ! Erreur : " + e);
        }

        if (parametersAreValid) {
            HomeController.closeCurrentGroupSettingsStage();

            System.out.println("");
            log.debug("Type de l'opération : " + operationType.toString());
            log.debug("Nom du groupe : \"" + groupName + "\"");
            log.debug("Pseudos des membres du groupe : " + Arrays.toString(members));
            log.debug("User IDs associés (aux pseudos) : " + userIDs.toString());
            log.debug("Statut du groupe : " + groupStatus.toString());
            log.debug("Description du groupe : \"" + groupDescription + "\"");
            System.out.println("");

            /* ---------------------------------------------------------- */

            // "createGroup" request

            try {
                JSONObject createGroupData = new JSONObject();
                createGroupData.put("group_name", groupName);
                createGroupData.put("user_id", userIDs);

                String createGroupRequest = RequestBuilder.buildWithData("createGroup", createGroupData).toString();
                String responseFromServer = MainController.getTcpClient().sendRequest(createGroupRequest);

                JSONObject wholeReceivedData = new JSONObject(responseFromServer);
                String requestStatus = wholeReceivedData.getString("status");

                if (requestStatus.equals("OK")) {
                    HomeController.addNewestItemToHashMap(groupName);
                }
                else {
                    if (requestStatus.equals("DENIED")) {
                        log.error("Le nom de groupe \"" + groupName + "\" existe déjà !");
                        return;
                    }
                    else {
                        log.error("La communication avec le serveur est corrompue (createGroupStatus : \"" + requestStatus + "\")");
                        System.exit(1);
                    }
                }
            }
            catch (JSONException jsonException) {
                log.error("Erreur JSON détectée : " + jsonException);
                System.exit(1);
            }

            /* ---------------------------------------------------------- */

            // adding new group thumbnail + the new group object

            HomeController.incrementNbGroupsYouAreStillPartOf();

            URL groupVisualizerURL = new File("src/main/pages/groupThumbnail.fxml").toURI().toURL();
            FXMLLoader groupThumbnailLoader = new FXMLLoader(groupVisualizerURL);
            Parent groupThumbnailRoot = groupThumbnailLoader.load();

            GroupThumbnailController groupThumbnailController = groupThumbnailLoader.getController();
            groupThumbnailController.build(groupName, groupStatus, groupDescription, operationType);

            GroupThumbnailObject newGroupThumbnailObject = new GroupThumbnailObject(groupThumbnailController, groupThumbnailRoot);
            HomeController.addGroup(newGroupThumbnailObject);

            GroupObject newGroupObject = new GroupObject(groupName, userIDs);
            DiscussionController.addGroupObject(newGroupObject);

            System.out.println("");
            if (operationType == OperationTypesEnum.CREATE_GROUP) {
                log.debug("Vous venez de créer le groupe \"" + groupName + "\" !");
            }
            else if (operationType == OperationTypesEnum.CREATE_PM) {
                log.debug("Vous venez de créer une page de discussion privée avec l'utilisateur \"" + groupName + "\" !");
            }
            System.out.println("");
        }

        else {
            if (!anErrorWasCaught) {
                log.error("Paramétrage invalide !");
            }
        }
    }

    private boolean currentSenderIsInMemberList(String[] memberList) {
        for (String member : memberList) {
            if (member.equals(DiscussionController.getCurrentSender())) {
                return true;
            }
        }

        return false;
    }
}
