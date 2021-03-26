package fsm;

public class Action {
    private String actionName;

    public Action(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

    @Override
    public String toString() {
        return actionName;
    }
}
