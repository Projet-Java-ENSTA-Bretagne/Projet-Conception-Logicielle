package FSM;

import java.util.Map;

public interface IState {
        // Returns the mapping for which one action will lead to another state
        Map<String, IState> getAdjacentStates();

        String getStateDesc();

        void addTransit(Action action, IState nextState);

        void removeTransit(String targetStateDesc);

}
