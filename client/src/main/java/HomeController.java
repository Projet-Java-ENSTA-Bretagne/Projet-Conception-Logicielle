import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Modality;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.io.File;
import javafx.scene.image.Image;

public class HomeController {

    @FXML
    void initialize() {
        System.out.println("init home controller");

        // just in case
        discussionScrollPane = new ScrollPane();
        discussionHBox = new HBox();
        discussionScrollPane.setContent(discussionHBox);
    }

    @FXML
    private static ScrollPane discussionScrollPane;

    public static ScrollPane getDiscussionScrollPane() {
        return discussionScrollPane;
    }

    @FXML
    private static HBox discussionHBox;

    public static HBox getDiscussionHBox() {
        return discussionHBox;
    }

    //

    @FXML
    void actionDisconnectButton() {
        System.out.println("deconnexion");
        MainController.switchToLoginScene();
    }

    private static Stage currentGroupSettingsStage = null;

    public static Stage getCurrentGroupSettingsStage() {
        return currentGroupSettingsStage;
    }

    public static void setCurrentGroupSettingsStage(Stage groupSettingsStage) {
        currentGroupSettingsStage = groupSettingsStage;
    }

    @FXML
    void joinOrCreateGroup() throws IOException {
        System.out.println("\nVous venez d'appuyer sur le bouton \"Join or create group\"");

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
