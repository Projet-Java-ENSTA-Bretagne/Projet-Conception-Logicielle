package fsm;

public class ServerFSM extends FiniteStateMachineImpl {

    public ServerFSM() {
        // Creating the states
        IState idleState = new StateImpl("idle");
        IState sendingState = new StateImpl("sending");
        IState receivingState = new StateImpl("receiving");
        IState closingState = new StateImpl("closing");

        // Creating the actions
        Action sendMessage = new Action("sendMessage");
        Action receiveMessage = new Action("receiveMessage");
        Action acceptConnection = new Action("acceptConnection");
        Action closeConnection = new Action("closeConnection");

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
