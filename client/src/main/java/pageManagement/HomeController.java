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

public class HomeController {

    // Logging
    private static final Logger log = LogManager.getLogger(HomeController.class);

    private static ArrayList<GroupThumbnailObject> groupThumbnailObjectList;

    public static ArrayList<GroupThumbnailObject> getGroupThumbnailObjectList() {
        return groupThumbnailObjectList;
    }

    public static void addGroup(GroupThumbnailObject groupThumbnailObject) {
        groupThumbnailHBox.getChildren().add(groupThumbnailObject.getRoot());
        groupThumbnailObjectList.add(groupThumbnailObject);
    }

    public static void deleteGroupByName(String nameOfTheGroupToDelete) {
        for (GroupThumbnailObject groupThumbnailObject : groupThumbnailObjectList) {
            GroupThumbnailController groupThumbnailController = groupThumbnailObject.getController();
            String groupName = groupThumbnailController.getGroupName();

            if (groupName.equals(nameOfTheGroupToDelete)) {
                Parent groupThumbnailRoot = groupThumbnailObject.getRoot();
                groupThumbnailHBox.getChildren().remove(groupThumbnailRoot);

                groupThumbnailObjectList.remove(groupThumbnailObject);

                groupThumbnailController = null;
                groupThumbnailRoot = null;
                groupThumbnailObject = null;

                decrementNbGroupsYouAreStillPartOf();
                System.out.println("");
                log.debug("Vous venez de quitter le groupe \"" + groupName + "\"");
                log.debug("Nombre total de groupes restants : " + nbGroupsYouAreStillPartOf + "\n");

                return;
            }
        }

        // just in case (but should theoretically never happen)
        System.out.println("");
        log.error("Le nom de groupe \"" + nameOfTheGroupToDelete + "\" n'existe pas !\n");
    }

    private static HBox groupThumbnailHBox;

    public static HBox getGroupThumbnailHBox() {
        return groupThumbnailHBox;
    }

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

    @FXML
    void initialize() {
        log.info("Initializing home controller\n");

        groupThumbnailHBox = null;
        groupThumbnailObjectList = new ArrayList<>();
        nbCreatedGroups = 0;
        nbGroupsYouAreStillPartOf = 0;
        currentGroupSettingsStage = null;
        currentConfirmLeaveGroupStage = null;
    }

    //

    @FXML
    void actionDisconnectButton() {
        System.out.println("");
        log.info("Deconnexion");
        MainController.switchToLoginScene();
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

    @FXML
    void joinOrCreateGroup() throws IOException {
        log.info("Vous venez d'appuyer sur le bouton \"Join or create group\"");

        URL groupSettingsURL = new File("src/main/pages/groupSettings.fxml").toURI().toURL();
        Parent groupSettingsRoot = FXMLLoader.load(groupSettingsURL);
        Scene scene = new Scene(groupSettingsRoot, 400, 375);

        Stage currentGroupSettingsStage = new Stage();
        currentGroupSettingsStage.getIcons().add(new Image("settings-icon.png"));
        currentGroupSettingsStage.setTitle("Group Settings");
        currentGroupSettingsStage.setScene(scene);
        currentGroupSettingsStage.initModality(Modality.APPLICATION_MODAL);
        currentGroupSettingsStage.setOnCloseRequest(e -> setCurrentGroupSettingsStage(null));

        setCurrentGroupSettingsStage(currentGroupSettingsStage);

        currentGroupSettingsStage.show();
    }

    //

}
