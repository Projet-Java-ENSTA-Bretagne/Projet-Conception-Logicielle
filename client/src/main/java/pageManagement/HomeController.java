package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.io.IOException;
import java.io.File;
import java.net.URL;

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

        // we do this so that we get the new value of the width of the group thumbnail VBox
        // **as soon as it's increased**, and can therefore automatically scroll to the
        // *very right* of the group thumbnail ScrollPane **as soon as a new group thumbnail
        // is generated**, which CANNOT be done (easily) otherwise
        groupThumbnailHBox.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldHvalue, Object newHvalue) {
                // we don't necessarily want to go to the very right of the group thumbnail
                // HBox when we leave a group
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

    private static int nbGroupsYouAreStillPartOf;

    public static int getNbGroupsYouAreStillPartOf() {
        return nbGroupsYouAreStillPartOf;
    }

    public static void incrementNbGroupsYouAreStillPartOf() {
        nbGroupsYouAreStillPartOf += 1;
    }

    public static void decrementNbGroupsYouAreStillPartOf() {
        nbGroupsYouAreStillPartOf -= 1;
    }

    public static boolean aGroupIsCurrentlyBeingDeleted;

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
    private void joinOrCreateGroup() throws IOException {
        log.info("Vous venez d'appuyer sur le bouton \"Join or create group\"");

        URL groupSettingsURL = new File("src/main/pages/groupSettings.fxml").toURI().toURL();
        Parent groupSettingsRoot = FXMLLoader.load(groupSettingsURL);
        setCurrentGroupSettingsRoot(groupSettingsRoot);
        Scene scene = new Scene(groupSettingsRoot, 415, 415);

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
     * Action linked to the "Déconnexion" JFXButton.
     * Disconnects from the server, leaves the Home page, then switches to the Login scene.
     * TODO : Link this method to network
     */
    @FXML
    private void actionDisconnectButton() {
        System.out.println("");
        log.info("Déconnexion");
        MainController.switchToLoginScene();
    }
}
