package org.eclipse.cargotracker.interfaces.booking.facade.dto;

public class TrackingEvents {

  private final boolean expected;
  private final String description;
  private final String time;

  public TrackingEvents(boolean expected, String description, String time) {
    this.expected = expected;
    this.description = description;
    this.time = time;
  }

  public boolean isExpected() {
    return expected;
  }

  public String getDescription() {
    return description;
  }

  public String getTime() {
    return time;
  }
}
