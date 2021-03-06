import javafx.fxml.FXML;
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

    private static ArrayList<String> groupNameList = new ArrayList<>();

    public static ArrayList<String> getGroupNameList() {
        return groupNameList;
    }

    @FXML
    void initialize() {
        System.out.println("init home controller");
        currentGroupSettingsStage = null;
    }

    //

    @FXML
    void actionDisconnectButton() {
        System.out.println("deconnexion");
        MainController.switchToLoginScene();
    }

    private static Stage currentGroupSettingsStage;

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
