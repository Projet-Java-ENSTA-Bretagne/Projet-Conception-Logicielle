package fsm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a set of states and transitions. It allows use to ensure the unicity of
 * the messages sent and received by the client.
 */
public class FiniteStateMachine implements IFiniteStateMachine {

    private IState startState;
    private IState endState;
    private IState currentState;
    private ArrayList<IState> allStates = new ArrayList<>();
    private HashMap<String, ArrayList<IState>> mapForAllStates = new HashMap<>();

    public void FiniteStateMachineImpl(){
        // TODO
    }

    @Override
    public void setStartState(IState startState) {
        this.startState = startState;
        currentState = startState;
        allStates.add(startState);
        // TODO : might have some value
        mapForAllStates.put(startState.getStateDesc(), new ArrayList<IState>());
    }

    @Override
    public void setEndState(IState endState) {
        this.endState = endState;
        allStates.add(endState);
        mapForAllStates.put(endState.getStateDesc(), new ArrayList<>());
    }

    @Override
    public void addState(IState startState, IState newState, Action action) {
        // validating startState, newState and action

        // update mapping in finite state machine
        allStates.add(newState);
        final String startStateDesc = startState.getStateDesc();
        final String newStateDesc = newState.getStateDesc();
        mapForAllStates.put(newStateDesc, new ArrayList<IState>());
        ArrayList<IState> adjacentStateList = null;
        if (mapForAllStates.containsKey(startStateDesc)) {
            adjacentStateList = mapForAllStates.get(startStateDesc);
            adjacentStateList.add(newState);
        }
        else {
            allStates.add(startState);
            adjacentStateList = new ArrayList<>();
            adjacentStateList.add(newState);
        }
        mapForAllStates.put(startStateDesc, adjacentStateList);

        // update mapping in startState
        for (IState state : allStates) {
            boolean isStartState = state.getStateDesc().equals(startState.getStateDesc());

            if (isStartState) {
                startState.addTransit(action, newState);
            }
        }
    }

    @Override
    public void removeState(String targetStateDesc) {
        // validate state
        if (!mapForAllStates.containsKey(targetStateDesc)) {
            throw new RuntimeException("Doesn't have state : " + targetStateDesc);
        }
        else {
            // remove from mapping
            mapForAllStates.remove(targetStateDesc);
        }

        // update all states
        IState targetState = null;
        for (IState state : allStates) {
            if (state.getStateDesc().equals(targetStateDesc)) {
                targetState = state;
            }
            else {
                state.removeTransit(targetStateDesc);
            }
        }

        allStates.remove(targetState);
    }

    @Override
    public IState getCurrentState() {
        return currentState;
    }

    @Override
    public void transit(Action action) {
        if (currentState == null) {
            throw new RuntimeException("Please setup start state");
        }

        Map<String, IState> localMapping = currentState.getAdjacentStates();

        if (localMapping.containsKey(action.toString())) {
            currentState = localMapping.get(action.toString());
        }
        else {
            throw new RuntimeException("No action start from current state");
        }
    }

    @Override
    public IState getStartState() {
        return startState;
    }

    @Override
    public IState getEndState() {
        return endState;
    }
}
