package org.eclipse.cargotracker.interfaces.handling;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;

/**
 * This is a simple transfer object for passing incoming handling event registration attempts to the
 * proper registration procedure.
 *
 * <p>It is used as a message queue element.
 */
public class HandlingEventRegistrationAttempt implements Serializable {

  private static final long serialVersionUID = 1L;

  private final LocalDateTime registrationTime;
  private final LocalDateTime completionTime;
  private final TrackingId trackingId;
  private final VoyageNumber voyageNumber;
  private final HandlingEvent.Type type;
  private final UnLocode unLocode;

  public HandlingEventRegistrationAttempt(
      LocalDateTime registrationDate,
      LocalDateTime completionDate,
      TrackingId trackingId,
      VoyageNumber voyageNumber,
      HandlingEvent.Type type,
      UnLocode unLocode) {
    this.registrationTime = registrationDate;
    this.completionTime = completionDate;
    this.trackingId = trackingId;
    this.voyageNumber = voyageNumber;
    this.type = type;
    this.unLocode = unLocode;
  }

  public TrackingId getTrackingId() {
    return trackingId;
  }

  public VoyageNumber getVoyageNumber() {
    return voyageNumber;
  }

  public HandlingEvent.Type getType() {
    return type;
  }

  public UnLocode getUnLocode() {
    return unLocode;
  }

  public LocalDateTime getRegistrationTime() {
    return registrationTime;
  }

  public LocalDateTime getCompletionTime() {
    return completionTime;
  }

  @Override
  public String toString() {
    return "HandlingEventRegistrationAttempt{"
        + "registrationTime="
        + registrationTime
        + ", completionTime="
        + completionTime
        + ", trackingId="
        + trackingId
        + ", voyageNumber="
        + voyageNumber
        + ", type="
        + type
        + ", unLocode="
        + unLocode
        + '}';
  }
}
