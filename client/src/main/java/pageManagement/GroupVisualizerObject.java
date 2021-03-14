package pageManagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.Parent;

// "backpack" class to contain all the relevant information about a group visualizer/thumbnail
public class GroupVisualizerObject {

    // Logging
    private final Logger log = LogManager.getLogger(GroupVisualizerObject.class);

    private GroupVisualizerController groupVisualizerController;
    private Parent groupVisualizerRoot;

    public GroupVisualizerObject(GroupVisualizerController controller, Parent root) {
        groupVisualizerController = controller;
        groupVisualizerRoot = root;
    }

    public GroupVisualizerController getController() {
        return groupVisualizerController;
    }

    public Parent getRoot() {
        return groupVisualizerRoot;
    }

    //

}
