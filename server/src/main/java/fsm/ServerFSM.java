package fsm;

public class ServerFSM extends FiniteStateMachineImpl {

    public ServerFSM() {
        // Creating the states
        IState idleState = StatesEnum.IDLE.getState();
        IState sendingState = StatesEnum.SENDING.getState();
        IState receivingState = StatesEnum.RECEIVING.getState();
        IState closingState = StatesEnum.CLOSING.getState();

        // Creating the actions
        Action sendMessage = ActionsEnum.SEND.getAction();
        Action receiveMessage = ActionsEnum.RECEIVE.getAction();
        Action acceptConnection = ActionsEnum.ACCEPT.getAction();
        Action closeConnection = ActionsEnum.CLOSE.getAction();

        // adding everything
        addState(idleState);
        addState(sendingState);
        addState(receivingState);
        addState(closingState);

        addTransit(idleState, receivingState, acceptConnection);
        addTransit(receivingState, sendingState, receiveMessage);
        addTransit(sendingState, receivingState, sendMessage);
        addTransit(receivingState, closingState, closeConnection);
        addTransit(sendingState, closingState, closeConnection);

        // setting start and end states
        setStartState(idleState);
        setEndState(closingState);
    }
}
