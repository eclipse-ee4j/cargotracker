package jakarta.cargotracker.application;

import jakarta.cargotracker.domain.model.cargo.TrackingId;
import jakarta.cargotracker.domain.model.handling.CannotCreateHandlingEventException;
import jakarta.cargotracker.domain.model.handling.HandlingEvent;
import jakarta.cargotracker.domain.model.location.UnLocode;
import jakarta.cargotracker.domain.model.voyage.VoyageNumber;

import java.util.Date;

public interface HandlingEventService {

    /**
     * Registers a handling event in the system, and notifies interested parties
     * that a cargo has been handled.
     */
    void registerHandlingEvent(Date completionTime,
                               TrackingId trackingId,
                               VoyageNumber voyageNumber,
                               UnLocode unLocode,
                               HandlingEvent.Type type) throws CannotCreateHandlingEventException;
}
