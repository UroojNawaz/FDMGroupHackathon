package com.fdmgroup.test;

import com.fdmgroup.main.Event;
import com.fdmgroup.main.State;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class App_test {

	public static void main(String[] args) {

		Finch myfinch = new Finch();
		
		State light = new State() {
			@Override
			public void run() {
				System.out.println("state zero");
				myfinch.setLED(255,0,0);
				myfinch.sleep(1000);
				myfinch.setLED(0,0,0);
			}
		};
		
		FSM_test fsm = new FSM_test(myfinch);
		
		// add States to FSM accepted states
		fsm.addState(light);

		// create transition table: event, fromState, toState
		fsm.addTransition(Event.NO_OBSTACLES, light, light);
		fsm.addTransition(Event.OBSTACLE_FRONT, light, FSM_test.getEnd());
		
		// set initial state
		FSM_test.setInitialState(light);
		
		// setting the events array is only for testing. once fully functional the
		// events will be read from finch input in the get FSM.getEvent() method
		fsm.events = new Event[] { Event.NO_OBSTACLES, Event.NO_OBSTACLES, Event.OBSTACLE_FRONT};
		
		fsm.runFSM();
		
	}

}
