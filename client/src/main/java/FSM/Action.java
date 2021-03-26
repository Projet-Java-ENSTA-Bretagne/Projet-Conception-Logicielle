package FSM;

public class Action {
    /*
    this class defines the triggers responsible for the transitions between states
     */
    private String mActionName;

    public Action(String actionName) {
        mActionName = actionName;
    }

    String getActionName() {
        return mActionName;
    }

    @Override
    public String toString() {
        return mActionName;
    }

}