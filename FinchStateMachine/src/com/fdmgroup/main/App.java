package com.fdmgroup.main;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class App {

	public static void main(String[] args) {

		Finch myfinch = new Finch();
		
		State advance = new State() {
			@Override
			public void run() {
				System.out.println("state advance");
				myfinch.setLED(255,0,0);
				myfinch.setWheelVelocities(200, 200, 1000);
				myfinch.setLED(0,0,0);
			}
		};

		State turn = new State() {
			@Override
			public void run() {
				System.out.println("state turn");
				myfinch.setLED(0,255,0);
				myfinch.setWheelVelocities(-200, -200, 1000);
				myfinch.setWheelVelocities(-200, 200, 1000);
				myfinch.setLED(0,0,0);
			}
		};
		
		FSM fsm = new FSM(myfinch);
		
		// add States to FSM accepted states
		fsm.addState(advance);
		fsm.addState(turn);
		
		// create transition table: event, fromState, toState
		fsm.addTransition(Event.NO_OBSTACLES, advance, advance);
		fsm.addTransition(Event.OBSTACLE_FRONT, advance, turn);
		fsm.addTransition(Event.OBSTACLE_LEFT, advance, turn);
		fsm.addTransition(Event.OBSTACLE_RIGHT, advance, turn);
		fsm.addTransition(Event.OBSTACLES_FRONT_RIGHT, advance, turn);
		fsm.addTransition(Event.OBSTACLES_LEFT_FRONT, advance, turn);
		fsm.addTransition(Event.OBSTACLES_LEFT_RIGHT, advance, turn);
		fsm.addTransition(Event.OBSTACLES_ALL, advance, turn);
		fsm.addTransition(Event.NO_OBSTACLES, turn, advance);
		
		// set initial state
		FSM.setInitialState(advance);
				
		fsm.runFSM();
		
	}

}
