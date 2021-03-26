package fsm;

public enum ActionsEnum {
    ACCEPT(new Action("acceptConnection")),
    SEND(new Action("sendMessage")),
    RECEIVE(new Action("receiveMessage")),
    CLOSE(new Action("closeConnection"));

    private Action action;
    ActionsEnum(Action action) {
        this.action = action;
    }
    public Action getAction() {
        return this.action;
    }
}
