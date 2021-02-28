package org.eclipse.cargotracker.interfaces.tracking.web;

import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.CargoRepository;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.handling.HandlingEventRepository;

/**
 * Backing bean for tracking cargo. This interface sits immediately on top of the domain layer,
 * unlike the booking interface which has a facade and supporting DTOs in between.
 *
 * <p>An adapter class, designed for the tracking use case, is used to wrap the domain model to make
 * it easier to work with in a web page rendering context. We do not want to apply view rendering
 * constraints to the design of our domain model and the adapter helps us shield the domain model
 * classes where needed.
 *
 * <p>In some very simplistic cases, it is fine to not use even an adapter.
 */
@Named("public.track")
@ViewScoped
public class Track implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject private CargoRepository cargoRepository;
  @Inject private HandlingEventRepository handlingEventRepository;

  private String trackingId;
  private CargoTrackingViewAdapter cargo;

  public String getTrackingId() {
    return trackingId;
  }

  public void setTrackingId(String trackingId) {
    if (trackingId != null) {
      trackingId = trackingId.trim();
    }

    this.trackingId = trackingId;
  }

  public CargoTrackingViewAdapter getCargo() {
    return cargo;
  }

  public void onTrackById() {
    Cargo cargo = cargoRepository.find(new TrackingId(trackingId));

    if (cargo != null) {
      List<HandlingEvent> handlingEvents =
          handlingEventRepository
              .lookupHandlingHistoryOfCargo(new TrackingId(trackingId))
              .getDistinctEventsByCompletionTime();
      this.cargo = new CargoTrackingViewAdapter(cargo, handlingEvents);
    } else {
      this.cargo = null;
    }
  }
}
