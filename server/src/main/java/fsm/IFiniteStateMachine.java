package fsm;

public interface IFiniteStateMachine {
    void setStartState(IState startState);

    void setEndState(IState endState);

    void addState(IState state);
    void removeState(String targetStateDesc);

    void addTransit(IState startState, IState endState, Action action);

    IState getCurrentState();

    IState getStartState();

    IState getEndState();

    void transit(Action action);
}
