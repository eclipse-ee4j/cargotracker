package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.eclipse.cargotracker.application.util.DateConverter;

/** DTO for a leg in an itinerary. */
public record Leg(
    String voyageNumber, Location from, Location to, String loadTime, String unloadTime)
    implements Serializable {
  @Serial private static final long serialVersionUID = 1L;

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

  public String getVoyageNumber() {
    return voyageNumber;
  }

  public String getFrom() {
    return from.toString();
  }

  public String getFromUnLocode() {
    return from.unLocode();
  }

  public String getFromName() {
    return from.name();
  }

  public String getTo() {
    return to.toString();
  }

  public String getToName() {
    return to.name();
  }

  public String getToUnLocode() {
    return to.unLocode();
  }

  public String getLoadTime() {
    return loadTime;
  }

  public String getUnloadTime() {
    return unloadTime;
  }
}
