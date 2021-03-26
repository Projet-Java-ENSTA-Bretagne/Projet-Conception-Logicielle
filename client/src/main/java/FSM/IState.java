package FSM;

import java.util.Map;

public interface IState {
        Map<String, IState> getAdjacentStates();

        String getStateDesc();

        void addTransit(Action action, IState nextState);

        void removeTransit(String targetStateDesc);

}
