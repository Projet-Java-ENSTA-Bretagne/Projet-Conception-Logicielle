import fsm.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class FSMTest {
    @Test
    public void FSMinitTest() {

        // creating the states (a circle in a graph)
        IState State1 = new State("start");
        IState State2 = new State("mid");
        IState State3 = new State("end");
        // creating the transitions
        Action actiontest1 =  new Action("action1");
        Action actiontest2 = new Action("action2");

        IFiniteStateMachine fsm;
        fsm = new FiniteStateMachine();
        fsm.setStartState(State1);
        fsm.setEndState(State3);
        assertEquals(fsm.getStartState(), State1);
        assertEquals(fsm.getCurrentState(), State1);
        assertEquals(fsm.getEndState(), State3);

        // assigning transition between a start state and end state
        fsm.addState(State1, State2, actiontest1);
        fsm.addState(State2, State1, actiontest2);

        // to transit the current state
        fsm.transit(actiontest1);
        assertEquals(fsm.getCurrentState(), State2);

    }


}
