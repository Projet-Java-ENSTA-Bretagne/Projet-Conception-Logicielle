package fsm;

import java.util.HashMap;
import java.util.Map;

public class StateImpl implements IState {
    private HashMap<String, IState> mapping = new HashMap<>();
    private String stateName;

    public StateImpl(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String getStateDesc() {
        return stateName;
    }

    @Override
    public Map<String, IState> getAdjacentStates() {
        return mapping;
    }

    @Override
    public void addTransit(Action action, IState state) {
        mapping.put(action.toString(), state);
    }

    @Override
    public void removeTransit(String targetStateDesc) {
        String targetAction = null;
        for (Map.Entry<String, IState> entry: mapping.entrySet()) {
            IState state = entry.getValue();
            if (state.getStateDesc().equals(targetStateDesc)) {
                targetAction = entry.getKey();
            }
        }
        mapping.remove(targetAction);
    }
}
