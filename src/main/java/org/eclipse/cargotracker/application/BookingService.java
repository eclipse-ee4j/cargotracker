package org.eclipse.cargotracker.application;

import org.eclipse.cargotracker.domain.model.cargo.Itinerary;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.location.UnLocode;

import java.util.Date;
import java.util.List;

/**
 * Cargo booking service.
 */
public interface BookingService {

    /**
     * Registers a new cargo in the tracking system, not yet routed.
     */
    TrackingId bookNewCargo(UnLocode origin, UnLocode destination, Date arrivalDeadline);

    /**
     * Requests a list of itineraries describing possible routes for this cargo.
     *
     * @param trackingId cargo tracking id
     * @return A list of possible itineraries for this cargo
     */
    List<Itinerary> requestPossibleRoutesForCargo(TrackingId trackingId);

    void assignCargoToRoute(Itinerary itinerary, TrackingId trackingId);

    void changeDestination(TrackingId trackingId, UnLocode unLocode);

    void changeDeadline(TrackingId trackingId, Date deadline);
}
