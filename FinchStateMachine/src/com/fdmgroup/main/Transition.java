package com.fdmgroup.main;

public class Transition {

	private State fromState;
	private Event event;

	public Transition(State fromState, Event event) {
		super();
		this.fromState = fromState;
		this.event = event;
	}

	public State getFromState() {
		return fromState;
	}

	public void setFromState(State fromState) {
		this.fromState = fromState;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Transition) {
			Transition tr = (Transition) obj;
			return (tr.getFromState().equals(this.getFromState())) && (tr.getEvent().equals(this.getEvent()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getFromState().hashCode() + getEvent().hashCode();
	}

}
