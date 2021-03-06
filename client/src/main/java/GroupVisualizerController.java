import javafx.fxml.FXML;

// class handling the group thumbnails that will be added to the Home page
public class GroupVisualizerController {

    private int groupNumber;

    public int getGroupNumber() {
        return groupNumber;
    }

    public GroupVisualizerController(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    @FXML
    void initialize() {
        System.out.printf("\ninit group visualizer controller n°%d", groupNumber);
    }

    //

}
