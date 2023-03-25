package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;
import java.time.LocalDate;
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
      String transportStatus,
      List<Leg> legs) {
    this.trackingId = trackingId;
    this.origin = origin;
    this.finalDestination = finalDestination;
    this.arrivalDeadline = DateConverter.toString(arrivalDeadline);
    this.misrouted = misrouted;
    this.claimed = claimed;
    this.lastKnownLocation = lastKnownLocation;
    this.transportStatus = transportStatus;
    this.legs = Collections.unmodifiableList(legs);
  }

  public String getTrackingId() {
    return trackingId;
  }

  public String getOrigin() {
    return origin.toString();
  }

  public String getOriginName() {
    return origin.getName();
  }

  public String getOriginCode() {
    return origin.getUnLocode();
  }

  public String getFinalDestination() {
    return finalDestination.toString();
  }

  public String getFinalDestinationName() {
    return finalDestination.getName();
  }

  public String getFinalDestinationCode() {
    return finalDestination.getUnLocode();
  }

  public List<Leg> getLegs() {
    return legs;
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
    return lastKnownLocation.toString();
  }

  public String getLastKnownLocationName() {
    return lastKnownLocation.getName();
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
