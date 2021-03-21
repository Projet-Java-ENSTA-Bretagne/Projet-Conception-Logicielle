package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 * Class handling the JavaFX objects from the Home scene (defined in home.fxml).
 */
public class HomeController {
    // Logging
    private static final Logger log = LogManager.getLogger(HomeController.class);

    // Object containing all the current group thumbnails
    private static ArrayList<GroupThumbnailObject> groupThumbnailObjectList;

    public static ArrayList<GroupThumbnailObject> getGroupThumbnailObjectList() {
        return groupThumbnailObjectList;
    }

    /**
     * Adds a group (thumbnail) to the Home page.
     *
     * @param groupThumbnailObject The group (thumbnail) object to add
     */
    public static void addGroup(GroupThumbnailObject groupThumbnailObject) {
        groupThumbnailHBox.getChildren().add(groupThumbnailObject.getRoot());
        groupThumbnailObjectList.add(groupThumbnailObject);
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

        // just in case (but should theoretically never happen)
        System.out.println("");
        log.error("Le nom de groupe \"" + nameOfTheGroupToDelete + "\" n'existe pas !\n");
    }

    private static HBox groupThumbnailHBox;

    public static void initializeGroupThumbnailHBox(HBox newGroupThumbnailHBox) {
        groupThumbnailHBox = newGroupThumbnailHBox;
    }

    // number of created **or joined** groups
    private static int nbCreatedGroups;

    public static int getNbCreatedGroups() {
        return nbCreatedGroups;
    }

    // nbCreatedGroups can only increase
    public static void incrementNbCreatedGroups() {
        nbCreatedGroups += 1;
    }

    private static int nbGroupsYouAreStillPartOf;

    public static void incrementNbGroupsYouAreStillPartOf() {
        nbGroupsYouAreStillPartOf += 1;
    }

    public static void decrementNbGroupsYouAreStillPartOf() {
        nbGroupsYouAreStillPartOf -= 1;
    }

    /**
     * Method that is executed right before "home.fxml" is loaded.
     */
    @FXML
    void initialize() {
        log.info("Initializing home controller");

        groupThumbnailHBox = null;
        groupThumbnailObjectList = new ArrayList<>();
        nbCreatedGroups = 0;
        nbGroupsYouAreStillPartOf = 0;
        currentGroupSettingsStage = null;
        currentConfirmLeaveGroupStage = null;
    }

    /**
     * Action linked to the "Déconnexion" JFXButton.
     * Disconnects from the server, leaves the Home page, then switches to the Login scene.
     * TODO : Link this method to network
     */
    @FXML
    void actionDisconnectButton() {
        System.out.println("");
        log.info("Deconnexion");
        MainController.switchToLoginScene();
    }

    private static Stage currentGroupSettingsStage;

    public static void setCurrentGroupSettingsStage(Stage groupSettingsStage) {
        currentGroupSettingsStage = groupSettingsStage;
        if (groupSettingsStage == null) {
            setCurrentGroupSettingsRoot(null);
        }
    }

    public static void closeCurrentGroupSettingsStage() {
        currentGroupSettingsStage.close();
        setCurrentGroupSettingsStage(null);
    }

    private static Parent currentGroupSettingsRoot;

    public static Parent getCurrentGroupSettingsRoot() {
        return currentGroupSettingsRoot;
    }

    public static void setCurrentGroupSettingsRoot(Parent groupSettingsRoot) {
        currentGroupSettingsRoot = groupSettingsRoot;
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
     * TODO : Link this method to network
     *
     * @throws IOException If error when FXMLLoader.load() is called
     */
    @FXML
    void joinOrCreateGroup() throws IOException {
        log.info("Vous venez d'appuyer sur le bouton \"Join or create group\"");

        URL groupSettingsURL = new File("src/main/pages/groupSettings.fxml").toURI().toURL();
        Parent groupSettingsRoot = FXMLLoader.load(groupSettingsURL);
        setCurrentGroupSettingsRoot(groupSettingsRoot);
        Scene scene = new Scene(groupSettingsRoot, 400, 415);

        Stage currentGroupSettingsStage = new Stage();
        currentGroupSettingsStage.getIcons().add(new Image("settings-icon.png"));
        currentGroupSettingsStage.setTitle("Group Settings");
        currentGroupSettingsStage.setScene(scene);
        currentGroupSettingsStage.initModality(Modality.APPLICATION_MODAL);
        currentGroupSettingsStage.setOnCloseRequest(e -> setCurrentGroupSettingsStage(null));

        setCurrentGroupSettingsStage(currentGroupSettingsStage);

        currentGroupSettingsStage.show();
    }
}
