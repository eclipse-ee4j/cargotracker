package org.eclipse.cargotracker.application;

import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.handling.CannotCreateHandlingEventException;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;

public interface HandlingEventService {

  /**
   * Registers a handling event in the system, and notifies interested parties that a cargo has been
   * handled.
   */
  void registerHandlingEvent(
      @NotNull(message = "Completion time is required.") LocalDateTime completionTime,
      @NotNull(message = "Tracking ID is required.") @Valid TrackingId trackingId,
      @Valid VoyageNumber voyageNumber,
      @NotNull(message = "Location is required.") @Valid UnLocode unLocode,
      @NotNull(message = "Type is required.") HandlingEvent.Type type)
      throws CannotCreateHandlingEventException;
}
