package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import org.eclipse.cargotracker.application.util.DateConverter;

/** DTO for registering and routing a cargo. */
public record CargoRoute(
    String trackingId,
    Location origin,
    Location finalDestination,
    String arrivalDeadline,
    boolean misrouted,
    boolean claimed,
    Location lastKnownLocation,
    String transportStatus,
    List<Leg> legs)
    implements Serializable {

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
    this(
        trackingId,
        origin,
        finalDestination,
        DateConverter.toString(arrivalDeadline),
        misrouted,
        claimed,
        lastKnownLocation,
        transportStatus,
        legs);
  }

  public boolean isRouted() {
    return !legs.isEmpty();
  }
}
