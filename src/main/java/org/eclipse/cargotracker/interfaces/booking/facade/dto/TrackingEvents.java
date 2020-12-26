package org.eclipse.cargotracker.interfaces.booking.facade.dto;

public class TrackingEvents {

	private final boolean expected;
	private final String description;

	public TrackingEvents(boolean expected, String description) {
		this.expected = expected;
		this.description = description;
	}

	public boolean isExpected() {
		return expected;
	}

	public String getDescription() {
		return description;
	}
}
