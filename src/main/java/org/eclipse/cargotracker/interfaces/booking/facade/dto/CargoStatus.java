package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.util.Collections;
import java.util.List;

public record CargoStatus(
    String trackingId,
    String destination,
    String statusText,
    boolean misdirected,
    String eta,
    String nextExpectedActivity,
    List<TrackingEvents> events) {

  public CargoStatus {
    events = List.copyOf(events);
  }

  @Override
  public List<TrackingEvents> events() {
    return Collections.unmodifiableList(events);
  }

  /**
   * @return A readable string describing the cargo status.
   */
  public String getStatusText() {
    return statusText;
  }
}
