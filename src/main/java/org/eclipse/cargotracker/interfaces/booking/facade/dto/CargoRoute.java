package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.cargotracker.application.util.DateUtil;
import org.eclipse.cargotracker.application.util.LocationUtil;

/** DTO for registering and routing a cargo. */
public class CargoRoute implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String trackingId;
  private final String origin;
  private final String finalDestination;
  private final String arrivalDeadline;
  private final boolean misrouted;
  private final List<Leg> legs;
  private final boolean claimed;
  private final String lastKnownLocation;
  private final String transportStatus;
  private String nextLocation;

  public CargoRoute(
      String trackingId,
      String origin,
      String finalDestination,
      LocalDate arrivalDeadline,
      boolean misrouted,
      boolean claimed,
      String lastKnownLocation,
      String transportStatus) {
    this.trackingId = trackingId;
    this.origin = origin;
    this.finalDestination = finalDestination;
    this.arrivalDeadline = DateUtil.toString(arrivalDeadline);
    this.misrouted = misrouted;
    this.claimed = claimed;
    this.lastKnownLocation = lastKnownLocation;
    this.transportStatus = transportStatus;
    this.legs = new ArrayList<>();
  }

  public String getTrackingId() {
    return trackingId;
  }

  public String getOrigin() {
    return origin;
  }

  public String getOriginName() {
    // TODO [Clean Code] See if this can be done in a more DDD friendly way.
    return LocationUtil.getLocationName(origin);
  }

  public String getOriginCode() {
    // TODO [Clean Code] See if this can be done in a more DDD friendly way.
    return LocationUtil.getLocationCode(origin);
  }

  public String getFinalDestination() {
    return finalDestination;
  }

  public String getFinalDestinationName() {
    // TODO [Clean Code] See if this can be done in a more DDD friendly way.
    return LocationUtil.getLocationName(finalDestination);
  }

  public String getFinalDestinationCode() {
    // TODO [Clean Code] See if this can be done in a more DDD friendly way.
    return LocationUtil.getLocationCode(finalDestination);
  }

  // TODO [Clean Code] See if this can be done in a more DDD friendly way.
  public void addLeg(
      String voyageNumber,
      String fromUnLocode,
      String fromName,
      String toUnLocode,
      String toName,
      LocalDateTime loadTime,
      LocalDateTime unloadTime) {
    legs.add(
        new Leg(voyageNumber, fromUnLocode, fromName, toUnLocode, toName, loadTime, unloadTime));
  }

  public List<Leg> getLegs() {
    return Collections.unmodifiableList(legs);
  }

  public boolean isMisrouted() {
    return misrouted;
  }

  public boolean isRouted() {
    return !legs.isEmpty();
  }

  public String getArrivalDeadline() {
    return arrivalDeadline;
  }

  public boolean isClaimed() {
    return claimed;
  }

  public String getLastKnownLocation() {
    return this.lastKnownLocation;
  }

  public String getLastKnownLocationName() {
    return LocationUtil.getLocationName(lastKnownLocation);
  }

  public String getLastKnownLocationCode() {
    return LocationUtil.getLocationCode(lastKnownLocation);
  }

  public String getTransportStatus() {
    // TODO [Clean Code] This needs to be a richer status, with a more readable description.
    return this.transportStatus;
  }

  public String getNextLocation() {
    return this.nextLocation;
  }
}
