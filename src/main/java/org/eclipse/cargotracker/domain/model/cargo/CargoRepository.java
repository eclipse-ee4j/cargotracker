package org.eclipse.cargotracker.domain.model.cargo;

import java.util.List;

public interface CargoRepository {

  Cargo find(TrackingId trackingId);

  Cargo findByTrackingIdWithItineraryLegs(TrackingId trackingId);

  List<Cargo> findAll();

  List<Cargo> findAllWithItineraryLegs();

  void store(Cargo cargo);

  TrackingId nextTrackingId();
}
