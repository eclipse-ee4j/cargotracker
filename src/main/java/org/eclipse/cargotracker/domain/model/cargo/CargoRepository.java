package org.eclipse.cargotracker.domain.model.cargo;

import java.util.List;

public interface CargoRepository {

  Cargo find(TrackingId trackingId);

  List<Cargo> findAll();

  Cargo findByTrackingIdWithItineraryLegs(TrackingId trackingId);

  void store(Cargo cargo);

  TrackingId nextTrackingId();

  List<Cargo> findAllWithItineraryLegs();
}
