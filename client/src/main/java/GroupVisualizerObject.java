import javafx.scene.Parent;

// "backpack" class to contain all the relevant information about a group visualizer/thumbnail
public class GroupVisualizerObject {

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
