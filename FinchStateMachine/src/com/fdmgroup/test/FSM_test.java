package com.fdmgroup.test;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fdmgroup.main.Event;
import com.fdmgroup.main.State;
import com.fdmgroup.main.Transition;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class FSM_test {

	private Set<State> statesSet = new HashSet<>();
	private Set<Event> eventsSet = new HashSet<>();
	private Map<Transition, State> transitionTable = new HashMap<>();
	private Finch finch;
	
	public FSM_test(Finch finch) {
		super();
		this.finch = finch;
	}

	/*
	 * temporary events array
	 */
	public Event[] events;
	public int counter = 0;

	private static State initialState = null;
	private static State end = new State() {

		@Override
		public void run() {
			System.out.println("FinchBot: Final State Reached, Freeze All Motor Function");
			// Finch Turn light
			// Finch stop all motor function
		}
	};

	public static State getInitialState() {
		return initialState;
	}

	public static void setInitialState(State init) {
		FSM_test.initialState = init;
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
		System.out.println(Instant.now().toString());
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
		System.out.println(Instant.now().toString());
	}

	/**
	 * events for finch will be relating to where the obstacles are sensed.
	 * 
	 * @return
	 */
	private Event getEvent() {
		// TO-DO
		// this method needs to change. Need a method call on the finch to read each of
		// the 3 sensors and then map them to an Event enum type.
		Event event = events[counter];
		counter = counter < events.length ? counter + 1 : counter;
		return event;
	}
}
