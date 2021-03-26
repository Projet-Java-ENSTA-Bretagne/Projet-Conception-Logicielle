package FSM;

public class Action {
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