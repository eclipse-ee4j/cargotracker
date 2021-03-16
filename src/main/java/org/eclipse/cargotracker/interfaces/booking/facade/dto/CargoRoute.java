package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.cargotracker.application.util.DateConverter;

/** DTO for registering and routing a cargo. */
public class CargoRoute implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String trackingId;
  private final Location origin;
  private final Location finalDestination;
  private final String arrivalDeadline;
  private final boolean misrouted;
  private final List<Leg> legs;
  private final boolean claimed;
  private final Location lastKnownLocation;
  private final String transportStatus;
  private String nextLocation;

  public CargoRoute(
      String trackingId,
      Location origin,
      Location finalDestination,
      LocalDate arrivalDeadline,
      boolean misrouted,
      boolean claimed,
      Location lastKnownLocation,
      String transportStatus) {
    this.trackingId = trackingId;
    this.origin = origin;
    this.finalDestination = finalDestination;
    this.arrivalDeadline = DateConverter.toString(arrivalDeadline);
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
    return origin.getName();
  }

  public String getOriginName() {
    return origin.getNameOnly();
  }

  public String getOriginCode() {
    return origin.getUnLocode();
  }

  public String getFinalDestination() {
    return finalDestination.getName();
  }

  public String getFinalDestinationName() {
    return finalDestination.getNameOnly();
  }

  public String getFinalDestinationCode() {
    return finalDestination.getUnLocode();
  }

  // TODO [Clean Code] See if this can be done in a more DDD friendly way.
  public void addLeg(
      String voyageNumber,
      Location from,
      Location to,
      LocalDateTime loadTime,
      LocalDateTime unloadTime) {
    legs.add(new Leg(voyageNumber, from, to, loadTime, unloadTime));
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
    return lastKnownLocation.getName();
  }

  public String getLastKnownLocationName() {
    return lastKnownLocation.getNameOnly();
  }

  public String getLastKnownLocationCode() {
    return lastKnownLocation.getUnLocode();
  }

  public String getTransportStatus() {
    // TODO [Clean Code] This needs to be a richer status, with a more readable description.
    return this.transportStatus;
  }

  public String getNextLocation() {
    return this.nextLocation;
  }
}
