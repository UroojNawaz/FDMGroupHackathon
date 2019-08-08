package com.fdmgroup.main;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class FSM {

	private Set<State> statesSet = new HashSet<>();
	private Set<Event> eventsSet = new HashSet<>();
	private Map<Transition, State> transitionTable = new HashMap<>();
	private Map<Integer, Event> sensorToEvent = new HashMap<>();
	private Finch finch;

	public FSM(Finch finch) {
		super();
		this.finch = finch;
		sensorToEvent.put(0, Event.NO_OBSTACLES);
		sensorToEvent.put(1, Event.OBSTACLE_LEFT);
		sensorToEvent.put(2, Event.OBSTACLE_FRONT);
		sensorToEvent.put(4, Event.OBSTACLE_RIGHT);
		sensorToEvent.put(3, Event.OBSTACLES_LEFT_FRONT);
		sensorToEvent.put(5, Event.OBSTACLES_LEFT_RIGHT);
		sensorToEvent.put(6, Event.OBSTACLES_FRONT_RIGHT);
		sensorToEvent.put(7, Event.OBSTACLES_ALL);
	}

	private static State initialState = null;
	private static State end = new State() {

		@Override
		public void run() {
			System.out.println("FinchBot: Final State Reached, Freeze All Motor Function");
		}
	};

	public static State getInitialState() {
		return initialState;
	}

	public static void setInitialState(State init) {
		FSM.initialState = init;
	}

	public static State getEnd() {
		return end;
	}

	public void addState(State stateNew) {
		statesSet.add(stateNew);
		transitionTable.put(new Transition(stateNew, Event.TERMINATE), end);
	}

	/**
	 * Add a new entry to transition table by specifying: event, fromState, toState
	 * Transition table is a Map<Transition, State> so only one mapping of (event,
	 * fromState) allowed. That is for each event and fromState pair, there should
	 * only be one resulting toState.
	 * 
	 * @param eventNew
	 * @param fromState
	 * @param toState
	 */
	public void addTransition(Event eventNew, State fromState, State toState) {
		eventsSet.add(eventNew);
		transitionTable.put(new Transition(fromState, eventNew), toState);
	}

	/**
	 * method to start the FSM once states, init, end, and transition tables are
	 * properly set up. It will start running from the init state and run each state
	 * waiting for a new event to transition to another state to run. Will stop
	 * running once no more events come in or when reaching the end State.
	 */
	public void runFSM() {
		File timeLog = new File("timeStamps.txt");
		timeLog.delete();
		Scanner scan = new Scanner(System.in);
		System.out.println("Press Enter to Start");
		@SuppressWarnings("unused")
		String input = scan.nextLine();
		Thread timerThread = new Thread(new TimeLogger());
		timerThread.start();

		Event nextEvent;
		State currentState = initialState;
		currentState.run();
		while (currentState != end) {
			nextEvent = getEvent();
			if ((eventsSet.contains(nextEvent))
					&& (transitionTable.containsKey(new Transition(currentState, nextEvent)))) {
				currentState = transitionTable.get(new Transition(currentState, nextEvent));
			} else {
				System.out.println("Transition not part of accepted set");
				System.out.println("Terminating program");
				break;
			}
			currentState.run();
		}
		finch.stopWheels();
		finch.setLED(255, 0, 0);
		finch.saySomething("Finch bot is done, Freeze all motor functions.");
		System.out.println("Finch bot is done, Freeze all motor functions.");
		scan.close();
	}

	/**
	 * events for finch will be relating to where the obstacles are sensed.
	 * 
	 * @return
	 */
	private Event getEvent() {
		int left = finch.isObstacleLeftSide() ? 1 : 0;
		int front = finch.isObstacle() ? 1 : 0;
		int right = finch.isObstacleRightSide() ? 1 : 0;
		// System.out.println(LOG >> left + "," + front + "," + right);

		return sensorToEvent.get(1 * left + 2 * front + 4 * right);
	}

}
