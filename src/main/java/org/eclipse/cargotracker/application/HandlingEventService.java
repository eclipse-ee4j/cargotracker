package org.eclipse.cargotracker.application;

import java.time.LocalDateTime;
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
      LocalDateTime completionTime,
      TrackingId trackingId,
      VoyageNumber voyageNumber,
      UnLocode unLocode,
      HandlingEvent.Type type)
      throws CannotCreateHandlingEventException;
}
