package pageManagement;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Class handling the JavaFX objects from the Group Thumbnails (defined in groupThumbnail.fxml)
 * that will be added to the Home scene.
 */
public class MessageController {
    // Logging
    private final Logger log = LogManager.getLogger(MessageController.class);

    private int messageNb;
    private String sender;
    private String date;
    private String content;


    public int getMessageNb() {
        return messageNb;
    }

    public String getSender() {
        return sender;
    }

    public String getdate() {
        return date;
    }

    public String getContent() {return content; }


    public MessageController(int messageNb, String sender, String date, String content) {

        this.messageNb = messageNb;
        this.sender = sender;
        this.date = date;
        this.content = content;
    }

    /**
     * Method that is executed right before "groupThumbnail.fxml" is loaded.
     */
    @FXML
    void initialize() {
        System.out.println("");
        log.info("Initializing group thumbnail controller, message number = \"" + getMessageNb() + "\"");
    }

    /**
     * Action linked to the "OPEN" JFXButton.
     * Opens the discussion scene associated with the chosen group. Loads the message
     * from that same group.
     * /!\ THIS METHOD IS NOT (DIRECTLY) LINKED TO THE ASSOCIATED FXML FILE /!\
     *
     * TODO : Link this method to network
     * TODO : Link this method to the associated Discussion scene
     */
    public void actionOpenGroupButton() {
        log.info("Bouton \"OPEN\" appuye, message number = \"" + getMessageNb() + "\"");

        // TODO : open discussion.fxml
    }



}
