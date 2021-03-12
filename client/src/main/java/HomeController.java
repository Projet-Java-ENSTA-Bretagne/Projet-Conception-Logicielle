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

    private static ArrayList<GroupVisualizerObject> groupObjectList = new ArrayList<>();

    public static ArrayList<GroupVisualizerObject> getGroupObjectList() {
        return groupObjectList;
    }

    public static void deleteGroupByName(String nameOfTheGroupToDelete) {
        for (GroupVisualizerObject groupVisualizerObject : getGroupObjectList()) {
            GroupVisualizerController groupVisualizerController = groupVisualizerObject.getController();
            String groupName = groupVisualizerController.getGroupName();

            if (groupName.equals(nameOfTheGroupToDelete)) {
                Parent groupVisualizerRoot = groupVisualizerObject.getRoot();
                discussionHBox.getChildren().remove(groupVisualizerRoot);

                groupVisualizerController = null;
                groupVisualizerRoot = null;
                groupVisualizerObject = null;

                nbGroupsYouAreStillPartOf -= 1;
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

    private static HBox discussionHBox;

    public static HBox getDiscussionHBox() {
        return discussionHBox;
    }

    public static void setDiscussionHBox(HBox newDiscussionHBox) {
        discussionHBox = newDiscussionHBox;
    }

    // number of created **or joined** groups
    private static int nbCreatedGroups;

    public static int getNbCreatedGroups() {
        return nbCreatedGroups;
    }

    public static void setNbCreatedGroups(int newNbCreatedGroups) {
        nbCreatedGroups = newNbCreatedGroups;
    }

    private static int nbGroupsYouAreStillPartOf;

    public static int getNbGroupsYouAreStillPartOf() {
        return nbGroupsYouAreStillPartOf;
    }

    public static void setNbGroupsYouAreStillPartOf(int newNbGroupsYouAreStillPartOf) {
        nbGroupsYouAreStillPartOf = newNbGroupsYouAreStillPartOf;
    }

    @FXML
    void initialize() {
        log.info("Initializing home controller\n");
        discussionHBox = null;
        nbCreatedGroups = 0;
        nbGroupsYouAreStillPartOf = 0;
        currentGroupSettingsStage = null;
        currentConfirmLeaveGroupStage = null;
    }

    //

    @FXML
    void actionDisconnectButton() {
        log.info("Deconnexion");
        MainController.switchToLoginScene();
    }

    private static Stage currentGroupSettingsStage;

    public static Stage getCurrentGroupSettingsStage() {
        return currentGroupSettingsStage;
    }

    public static void setCurrentGroupSettingsStage(Stage groupSettingsStage) {
        currentGroupSettingsStage = groupSettingsStage;
    }

    private static Stage currentConfirmLeaveGroupStage;

    public static Stage getCurrentConfirmLeaveGroupStage() {
        return currentConfirmLeaveGroupStage;
    }

    public static void setCurrentConfirmLeaveGroupStage(Stage newCurrentConfirmLeaveGroupStage) {
        currentConfirmLeaveGroupStage = newCurrentConfirmLeaveGroupStage;
    }

    @FXML
    void joinOrCreateGroup() throws IOException {
        log.info("Vous venez d'appuyer sur le bouton \"Join or create group\"");

        URL groupSettingsURL = new File("src/main/pages/groupSettings.fxml").toURI().toURL();
        Parent groupSettingsRoot = FXMLLoader.load(groupSettingsURL);
        Scene scene = new Scene(groupSettingsRoot, 400, 375);

        Stage secondaryStage = new Stage(); // new stage for GroupSettings
        secondaryStage.getIcons().add(new Image("settings-icon.png"));
        secondaryStage.setTitle("Group Settings");
        secondaryStage.setScene(scene);
        secondaryStage.initModality(Modality.APPLICATION_MODAL);

        setCurrentGroupSettingsStage(secondaryStage);

        secondaryStage.show();
    }

    //

}
