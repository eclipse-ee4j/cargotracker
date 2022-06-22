package org.eclipse.cargotracker.domain.model.handling;

import org.eclipse.cargotracker.domain.model.cargo.TrackingId;

public interface HandlingEventRepository {

  void store(HandlingEvent event);

  HandlingHistory lookupHandlingHistoryOfCargo(TrackingId trackingId);
}
