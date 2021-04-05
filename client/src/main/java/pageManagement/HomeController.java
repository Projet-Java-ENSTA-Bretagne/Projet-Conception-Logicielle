package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.net.URL;

import networking.RequestBuilder;

/**
 * Class handling the JavaFX objects from the Home scene (defined in home.fxml).
 */
public class HomeController {
    // Logging
    private static final Logger log = LogManager.getLogger(HomeController.class);

    /**
     * Method that is automatically executed right after "home.fxml" is loaded.
     */
    @FXML
    private void initialize() {
        log.info("Initializing home controller");

        groupThumbnailObjectList = new ArrayList<>();
        nbGroupsYouAreStillPartOf = 0;
        aGroupIsCurrentlyBeingDeleted = false;
        groupNamesToGroupIDs = new HashMap<>();
    }

    private static HBox groupThumbnailHBox;
    private static ScrollPane groupThumbnailScrollPane;

    public static void initializeStaticComponents() {
        Parent homeRoot = MainController.getHomeScene().getRoot();

        // unfortunately, JavaFX does NOT load objects into static "@FXML" variables, so we
        // will have to do it ourselves
        groupThumbnailHBox = (HBox) homeRoot.lookup("#groupThumbnailHBox");
        groupThumbnailScrollPane = (ScrollPane) homeRoot.lookup("#groupThumbnailScrollPane");

        // preventing vertical scrolling in the group thumbnail ScrollPane
        groupThumbnailScrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if (scrollEvent.getDeltaY() != 0) {
                    scrollEvent.consume();
                }
            }
        });

        // we do this so that we get the new value of the width of the group thumbnail HBox
        // **as soon as it's increased**, and can therefore automatically scroll to the
        // *very right* of the group thumbnail ScrollPane **as soon as a new group thumbnail
        // is generated**, which CANNOT be done (easily) otherwise
        groupThumbnailHBox.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldHvalue, Object newHvalue) {
                // we don't necessarily want to go to the very right of the group thumbnail
                // ScrollPane when we leave/delete a group
                if (!aGroupIsCurrentlyBeingDeleted) {
                    groupThumbnailScrollPane.setHvalue((Double) newHvalue);
                }
                else {
                    // for the next (potential) change detection by the ChangeListener
                    // NB : being in this "else" block automatically implies that
                    //      the variable "nbGroupsYouAreStillPartOf" was >= 4 before
                    //      a group was deleted (so after deletion it should be >= 3)
                    aGroupIsCurrentlyBeingDeleted = false;
                }
            }
        });
    }

    public static void clearGroupThumbnailHBox() {
        groupThumbnailHBox.getChildren().clear();
    }

    private static HashMap<String, String> groupNamesToGroupIDs;

    public static void addItemToHashMap(String groupName, String groupID) {
        groupNamesToGroupIDs.put(groupName, groupID);
    }

    /**
     * Adds the newest item (groupName, groupID) to the associated HashMap. By "newest" we mean
     * the group/PM chat that has just been created. We do this because the group ID isn't
     * sent back by the server when we create a group.
     */
    public static void addNewestItemToHashMap(String referenceGroupName) {
        try {
            JSONObject userIdData = new JSONObject();
            userIdData.put("user_id", DiscussionController.getCurrentSenderID());

            String[] requestStatusAndGroupsInfo = getUserGroupsRequest(userIdData);

            String requestStatus = requestStatusAndGroupsInfo[0];
            JSONArray groupsInfo = new JSONArray(requestStatusAndGroupsInfo[1]);

            if (requestStatus.equals("OK")) {
                if (groupsInfo.length() != groupNamesToGroupIDs.size() + 1) {
                    log.fatal("Error while adding the newest group");
                    System.exit(1);
                }

                for (int i = 0; i < groupsInfo.length(); i++) {
                    JSONObject groupData = groupsInfo.getJSONObject(i);

                    String groupName = groupData.getString("name");
                    String groupID = groupData.getString("id");

                    if ((!groupNamesToGroupIDs.containsKey(groupName)) && (!groupNamesToGroupIDs.containsValue(groupID)) && (groupName.equals(referenceGroupName))) {
                        addItemToHashMap(groupName, groupID);
                        log.debug("GroupNames --> GroupIDs : " + groupNamesToGroupIDs.toString());
                        return;
                    }
                }

                log.fatal("Error while adding the newest group");
                System.exit(1);
            }
            else {
                log.error("La communication avec le serveur est corrompue (getUserGroupsStatus : \"" + requestStatus + "\")");
                System.exit(1);
            }
        }
        catch (JSONException jsonException) {
            log.error("Erreur JSON détectée : " + jsonException);
            System.exit(1);
        }
    }

    public static void clearHashMap() {
        groupNamesToGroupIDs.clear();
    }

    private static int nbGroupsYouAreStillPartOf;

    public static int getNbGroupsYouAreStillPartOf() {
        return nbGroupsYouAreStillPartOf;
    }

    public static void resetNbGroupsYouAreStillPartOf() {
        nbGroupsYouAreStillPartOf = 0;
    }

    public static void incrementNbGroupsYouAreStillPartOf() {
        nbGroupsYouAreStillPartOf += 1;
    }

    public static void decrementNbGroupsYouAreStillPartOf() {
        nbGroupsYouAreStillPartOf -= 1;
    }

    public static boolean aGroupIsCurrentlyBeingDeleted;

    // List containing all the current group thumbnail objects
    private static ArrayList<GroupThumbnailObject> groupThumbnailObjectList;

    public static ArrayList<GroupThumbnailObject> getGroupThumbnailObjectList() {
        return groupThumbnailObjectList;
    }

    /**
     * Adds a group thumbnail to the Home page.
     *
     * @param groupThumbnailObject The group thumbnail object to add
     */
    public static void addGroup(GroupThumbnailObject groupThumbnailObject) {
        groupThumbnailObjectList.add(groupThumbnailObject);
        groupThumbnailHBox.getChildren().add(groupThumbnailObject.getRoot());
    }

    /**
     * Deletes a group thumbnail from the Home page.
     *
     * @param nameOfTheGroupToDelete The name of the group to delete
     */
    public static void deleteGroupThumbnailByGroupName(String nameOfTheGroupToDelete) {
        for (GroupThumbnailObject groupThumbnailObject : groupThumbnailObjectList) {
            GroupThumbnailController groupThumbnailController = groupThumbnailObject.getController();
            String groupName = groupThumbnailController.getGroupName();

            if (groupName.equals(nameOfTheGroupToDelete)) {
                Parent groupThumbnailRoot = groupThumbnailObject.getRoot();
                groupThumbnailHBox.getChildren().remove(groupThumbnailRoot);

                groupThumbnailObjectList.remove(groupThumbnailObject);

                groupThumbnailObject.delete();
                groupThumbnailObject = null;

                decrementNbGroupsYouAreStillPartOf();
                System.out.println("");
                log.debug("Vous venez de quitter le groupe \"" + nameOfTheGroupToDelete + "\"");
                log.debug("Nombre total de groupes restants : " + nbGroupsYouAreStillPartOf + "\n");

                return;
            }
        }
    }

    public static void loadExistingGroups() {
        try {
            JSONObject userIdData = new JSONObject();
            userIdData.put("user_id", DiscussionController.getCurrentSenderID());

            String[] requestStatusAndGroupsInfo = getUserGroupsRequest(userIdData);

            String requestStatus = requestStatusAndGroupsInfo[0];
            JSONArray groupsInfo = new JSONArray(requestStatusAndGroupsInfo[1]);

            if (requestStatus.equals("OK")) {
                log.debug("Groups info : " + groupsInfo.toString());

                for (int i = 0; i < groupsInfo.length(); i++) {
                    JSONObject groupData = groupsInfo.getJSONObject(i);

                    String groupName = groupData.getString("name");
                    boolean isPM = groupData.getBoolean("is_pm");

                    String groupID = groupData.getString("id");
                    log.debug(String.format("ID du groupe n°%d/%d (groupName : \"%s\", isPM : %b) : \"%s\"", i + 1, groupsInfo.length(), groupName, isPM, groupID));

                    String rawUserIDs = groupData.getString("members");
                    String[] userIDs = rawUserIDs.split(",");
                    // converting from String[] to JSONArray
                    JSONArray userIDsJSONArray = new JSONArray();
                    for (String userID : userIDs) {
                        userIDsJSONArray.put(userID);
                    }
                    log.debug("User IDs : " + userIDsJSONArray.toString());

                    addItemToHashMap(groupName, groupID);

                    /* ------------------------------------------------------ */

                    // adding group thumbnail + the group object

                    incrementNbGroupsYouAreStillPartOf();

                    URL groupVisualizerURL = new File("src/main/pages/groupThumbnail.fxml").toURI().toURL();
                    FXMLLoader groupThumbnailLoader = new FXMLLoader(groupVisualizerURL);
                    Parent groupThumbnailRoot = groupThumbnailLoader.load();

                    GroupThumbnailController groupThumbnailController = groupThumbnailLoader.getController();

                    OperationTypesEnum operationType;
                    GroupStatusesEnum groupStatus;
                    String groupDescription;
                    if (isPM) {
                        operationType = OperationTypesEnum.CREATE_PM;
                        groupStatus = GroupStatusesEnum.PRIVATE;
                        groupDescription = "MP pré-existant";
                    }
                    else {
                        operationType = OperationTypesEnum.CREATE_GROUP;
                        groupStatus = GroupStatusesEnum.PUBLIC;
                        groupDescription = "Groupe pré-existant";
                    }

                    groupThumbnailController.build(groupName, groupStatus, groupDescription, operationType);

                    //

                    GroupThumbnailObject newGroupThumbnailObject = new GroupThumbnailObject(groupThumbnailController, groupThumbnailRoot);
                    addGroup(newGroupThumbnailObject);

                    GroupObject newGroupObject = new GroupObject(groupName, userIDsJSONArray);
                    DiscussionController.addGroupObject(newGroupObject);
                }

                log.debug("GroupNames --> GroupIDs : " + groupNamesToGroupIDs.toString());
            }
            else {
                log.error("La communication avec le serveur est corrompue (getUserGroupsStatus : \"" + requestStatus + "\")");
                System.exit(1);
            }
        }
        catch (JSONException jsonException) {
            log.error("Erreur JSON détectée : " + jsonException);
            System.exit(1);
        }
        catch (IOException ioException) {
            log.error("Erreur lors de l'appel à FXMLLoader.load() : " + ioException);
            System.exit(1);
        }
    }

    public static String[] getUserGroupsRequest(JSONObject userIdData) {
        String getUserGroupsRequest = RequestBuilder.buildWithData("getUserGroups", userIdData).toString();
        String responseFromServer = MainController.getTcpClient().sendRequest(getUserGroupsRequest);

        JSONObject wholeReceivedData = new JSONObject(responseFromServer);
        String requestStatus = wholeReceivedData.getString("status");

        JSONObject usefulReceivedData = wholeReceivedData.getJSONObject("data");
        JSONArray groupsInfo = usefulReceivedData.getJSONArray("groups");

        String[] requestStatusAndGroupsInfo = {requestStatus, groupsInfo.toString()};
        return requestStatusAndGroupsInfo;
    }

    private static Stage currentGroupSettingsStage;

    public static void setCurrentGroupSettingsStage(Stage groupSettingsStage) {
        currentGroupSettingsStage = groupSettingsStage;
    }

    public static void closeCurrentGroupSettingsStage() {
        currentGroupSettingsStage.close();
        setCurrentGroupSettingsStage(null);
    }

    private static Stage currentConfirmLeaveGroupStage;

    public static void setCurrentConfirmLeaveGroupStage(Stage confirmLeaveGroupStage) {
        currentConfirmLeaveGroupStage = confirmLeaveGroupStage;
    }

    public static void closeCurrentConfirmLeaveGroupStage() {
        currentConfirmLeaveGroupStage.close();
        setCurrentConfirmLeaveGroupStage(null);
    }

    /**
     * Action linked to the "join or create group" icon/JFXButton.
     * Opens a new GroupSettings stage, where it's possible to configurate
     * the settings of the group you want to join/create.
     *
     * @throws IOException If error when FXMLLoader.load() is called
     */
    @FXML
    private void joinOrCreateGroup() throws IOException {
        log.info("Vous venez d'appuyer sur le bouton \"Join or create group\"");

        URL groupSettingsURL = new File("src/main/pages/groupSettings.fxml").toURI().toURL();
        Parent groupSettingsRoot = FXMLLoader.load(groupSettingsURL);
        Scene scene = new Scene(groupSettingsRoot, 415, 330);

        Stage currentGroupSettingsStage = new Stage();
        currentGroupSettingsStage.getIcons().add(new Image("settings-icon.png"));
        currentGroupSettingsStage.setTitle("Group Settings");
        currentGroupSettingsStage.setScene(scene);
        currentGroupSettingsStage.initModality(Modality.APPLICATION_MODAL);
        currentGroupSettingsStage.setOnCloseRequest(e -> setCurrentGroupSettingsStage(null));

        setCurrentGroupSettingsStage(currentGroupSettingsStage);

        currentGroupSettingsStage.show();
    }

    /**
     * Action linked to the "Déconnexion" JFXButton. Disconnects from the server,
     * leaves the Home page, then switches to the Login scene.
     */
    @FXML
    private void actionDisconnectButton() {
        MainController.getTcpClient().disconnectFromServer();
        log.info("Déconnexion");
        MainController.switchToLoginScene();
    }
}
