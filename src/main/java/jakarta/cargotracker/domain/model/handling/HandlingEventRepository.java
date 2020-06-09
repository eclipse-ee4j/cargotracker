package jakarta.cargotracker.domain.model.handling;

import jakarta.cargotracker.domain.model.cargo.TrackingId;

public interface HandlingEventRepository {

    void store(HandlingEvent event);

    HandlingHistory lookupHandlingHistoryOfCargo(TrackingId trackingId);
}
