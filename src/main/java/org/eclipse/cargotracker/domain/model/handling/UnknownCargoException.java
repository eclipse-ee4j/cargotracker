package org.eclipse.cargotracker.domain.model.handling;

import org.eclipse.cargotracker.domain.model.cargo.TrackingId;

/** Thrown when trying to register an event with an unknown tracking id. */
public class UnknownCargoException extends CannotCreateHandlingEventException {

  private static final long serialVersionUID = 1L;
  private final TrackingId trackingId;

  /** @param trackingId cargo tracking id */
  public UnknownCargoException(TrackingId trackingId) {
    this.trackingId = trackingId;
  }

  /** {@inheritDoc} */
  @Override
  public String getMessage() {
    return "No cargo with tracking id " + trackingId.getIdString() + " exists in the system";
  }
}
