package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.eclipse.cargotracker.application.util.DateConverter;

/** DTO for a leg in an itinerary. */
public record Leg(
    String voyageNumber, Location from, Location to, String loadTime, String unloadTime)
    implements Serializable {

  public Leg(
      String voyageNumber,
      Location from,
      Location to,
      LocalDateTime loadTime,
      LocalDateTime unloadTime) {
    this(
        voyageNumber,
        from,
        to,
        DateConverter.toString(loadTime),
        DateConverter.toString(unloadTime));
  }
}
