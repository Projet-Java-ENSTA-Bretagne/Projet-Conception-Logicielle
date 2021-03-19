package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.Parent;

public class GroupThumbnailObject {
    /**
     * This is simply a "backpack class" containing all the relevant information of
     * a given group thumbnail.
     */

    // Logging
    private final Logger log = LogManager.getLogger(GroupThumbnailObject.class);

    private GroupThumbnailController groupThumbnailController;
    private Parent groupThumbnailRoot;

    public GroupThumbnailObject(GroupThumbnailController controller, Parent root) {
        groupThumbnailController = controller;
        groupThumbnailRoot = root;
    }

    public GroupThumbnailController getController() {
        return groupThumbnailController;
    }

    public Parent getRoot() {
        return groupThumbnailRoot;
    }

    //

}
