package fsm;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a state in a FSM, like "idle" or "sending" for instance.
 */
public class State implements IState {

    private HashMap<String, IState> mapping = new HashMap<>();
    private String stateName;

    public State(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public Map<String, IState> getAdjacentStates() {
        return mapping;
    }

    @Override
    public String getStateDesc() {
        return stateName;
    }

    @Override
    public void addTransit(Action action, IState state) {
        mapping.put(action.toString(), state);
    }

    @Override
    public void removeTransit(String targetStateDesc) {
        // get action which directs to target state
        String targetAction = null;

        for (Map.Entry<String, IState> entry : mapping.entrySet()) {
            IState state = entry.getValue();

            if (state.getStateDesc().equals(targetStateDesc)) {
                targetAction = entry.getKey();
                mapping.remove(targetAction);
                return;
            }
        }
    }
}
