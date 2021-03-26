package fsm;

public enum StatesEnum {
    IDLE(new StateImpl("idle")),
    SENDING(new StateImpl("sending")),
    RECEIVING(new StateImpl("receiving")),
    CLOSING(new StateImpl("closing"));

    private StateImpl state;
    StatesEnum(StateImpl state) {
        this.state = state;
    }
    public StateImpl getState() {
        return this.state;
    }
}
