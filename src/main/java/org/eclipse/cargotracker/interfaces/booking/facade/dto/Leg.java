package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.eclipse.cargotracker.application.util.DateUtil;

/** DTO for a leg in an itinerary. */
public class Leg implements Serializable {

  private static final long serialVersionUID = 1L;
  private final String voyageNumber;
  private final String fromUnLocode;
  private final String fromName;
  private final String toUnLocode;
  private final String toName;
  private final String loadTime;
  private final String unloadTime;

  public Leg(
      String voyageNumber,
      String fromUnLocode,
      String fromName,
      String toUnLocode,
      String toName,
      LocalDateTime loadTime,
      LocalDateTime unloadTime) {
    this.voyageNumber = voyageNumber;
    this.fromUnLocode = fromUnLocode;
    this.fromName = fromName;
    this.toUnLocode = toUnLocode;
    this.toName = toName;
    this.loadTime = DateUtil.toString(loadTime);
    this.unloadTime = DateUtil.toString(unloadTime);
  }

  public String getVoyageNumber() {
    return voyageNumber;
  }

  public String getFrom() {
    return fromName + " (" + fromUnLocode + ")";
  }

  public String getFromUnLocode() {
    return fromUnLocode;
  }

  public String getFromName() {
    return fromName;
  }

  public String getTo() {
    return toUnLocode + " (" + toName + ")";
  }

  public String getToName() {
    return toName;
  }

  public String getToUnLocode() {
    return toUnLocode;
  }

  public String getLoadTime() {
    return loadTime;
  }

  public String getUnloadTime() {
    return unloadTime;
  }

  @Override
  public String toString() {
    return "Leg{"
        + "voyageNumber="
        + voyageNumber
        + ", from="
        + fromUnLocode
        + ", to="
        + toUnLocode
        + ", loadTime="
        + loadTime
        + ", unloadTime="
        + unloadTime
        + '}';
  }
}
