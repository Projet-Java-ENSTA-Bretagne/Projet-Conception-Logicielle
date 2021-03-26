package fsm;

public class ServerFSM extends FiniteStateMachineImpl {

    public ServerFSM() {
        // Creating the states
        IState idleState = new StateImpl("idle");
        IState sendingState = new StateImpl("sending");
        IState receivingState = new StateImpl("receiving");
        IState closingState = new StateImpl("closing");

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
    }
}
