package fsm;

/**
 * Class defining the triggers of the transitions between states.
 */
public class Action {

    private String actionName;

    public Action(String actionName) {
        this.actionName = actionName;
    }

    String getActionName() {
        return actionName;
    }

    @Override
    public String toString() {
        return actionName;
    }
}
