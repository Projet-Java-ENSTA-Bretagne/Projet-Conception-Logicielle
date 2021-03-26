package fsm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocols.CreateUserProtocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FiniteStateMachineImpl implements IFiniteStateMachine {
    private IState startState;
    private IState endState;
    private IState currentState;
    private ArrayList<IState> allStates = new ArrayList<>();
    private HashMap<String, ArrayList<IState>> mapForAllStates = new HashMap<>();

    private final Logger log = LogManager.getLogger(FiniteStateMachineImpl.class);

    public FiniteStateMachineImpl() {}

    @Override
    public void setStartState(IState startState) {
        this.startState = startState;
        this.currentState = startState;
        this.allStates.add(startState);
        mapForAllStates.put(startState.getStateDesc(), new ArrayList<IState>());
    }

    @Override
    public void setEndState(IState endState) {
        this.endState = endState;
        this.allStates.add(endState);
        this.mapForAllStates.put(endState.getStateDesc(), new ArrayList());
    }

    @Override
    public void addState(IState state) {
        if (!this.allStates.contains(state)) {
            log.debug("Adding state in the FSM: " + state.getStateDesc());
            this.allStates.add(state);
            this.mapForAllStates.put(state.getStateDesc(), new ArrayList<>());
        } else {
            log.warn("State already in the FSM: " + state.getStateDesc());
        }
    }

    @Override
    public void addTransit(IState startState, IState endState, Action action) {
        if (!this.allStates.contains(startState) || !this.allStates.contains(endState)) {
            log.error("One of those states is not in the FSM: " + startState.getStateDesc() + " | " + endState.getStateDesc());
            throw new RuntimeException("State not present in the FSM");
        }
        log.debug("Adding action : " + startState.getStateDesc() + " -- " + action.getActionName() + " --> " + endState.getStateDesc());
        // updating the map
        this.mapForAllStates.get(startState.getStateDesc()).add(endState);
        // updating the state
        startState.addTransit(action, endState);
    }

    @Override
    public void removeState(String targetStateDesc) {
        // validate state
        if (!this.mapForAllStates.containsKey(targetStateDesc)) {
            throw new RuntimeException("Don't have state: " + targetStateDesc);
        } else {
            // remove from mapping
            this.mapForAllStates.remove(targetStateDesc);
        }
        log.debug("Removing state: " + targetStateDesc);

        // update all state
        IState targetState = null;
        for (IState state : this.allStates) {
            if (state.getStateDesc().equals(targetStateDesc)) {
                targetState = state;
            } else {
                state.removeTransit(targetStateDesc);
            }
        }
        this.allStates.remove(targetState);
    }

    @Override
    public IState getCurrentState() {
        return this.currentState;
    }

    @Override
    public void transit(Action action) {
        if (this.currentState == null) {
            log.error("No start state setup");
            throw new RuntimeException("Please setup start state");
        }
        Map<String, IState> localMapping = this.currentState.getAdjacentStates();
        if (localMapping.containsKey(action.toString())) {
            log.debug("Transit: " + getCurrentState().getStateDesc() + " --> " + localMapping.get(action.toString()).getStateDesc());
            this.currentState = localMapping.get(action.toString());
        } else {
            log.error("No action start from current state");
            throw new RuntimeException("No action start from current state");
        }
    }

    @Override
    public IState getStartState() {
        return this.startState;
    }

    @Override
    public IState getEndState() {
        return this.endState;
    }
}
