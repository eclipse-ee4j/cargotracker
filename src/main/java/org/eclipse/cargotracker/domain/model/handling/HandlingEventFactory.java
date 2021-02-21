package org.eclipse.cargotracker.domain.model.handling;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.CargoRepository;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.LocationRepository;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.eclipse.cargotracker.domain.model.voyage.VoyageRepository;

@ApplicationScoped
public class HandlingEventFactory implements Serializable {

  private static final long serialVersionUID = 1L;
  @Inject private CargoRepository cargoRepository;
  @Inject private VoyageRepository voyageRepository;
  @Inject private LocationRepository locationRepository;

  /**
   * @param registrationTime time when this event was received by the system
   * @param completionTime when the event was completed, for example finished loading
   * @param trackingId cargo tracking id
   * @param voyageNumber voyage number
   * @param unlocode United Nations Location Code for the location of the event
   * @param type type of event
   * @return A handling event.
   * @throws UnknownVoyageException if there's no voyage with this number
   * @throws UnknownCargoException if there's no cargo with this tracking id
   * @throws UnknownLocationException if there's no location with this UN Locode
   */
  // TODO [Clean Code] Look at the exception handling more seriously.
  public HandlingEvent createHandlingEvent(
      LocalDateTime registrationTime,
      LocalDateTime completionTime,
      TrackingId trackingId,
      VoyageNumber voyageNumber,
      UnLocode unlocode,
      HandlingEvent.Type type)
      throws CannotCreateHandlingEventException {
    Cargo cargo = findCargo(trackingId);
    Voyage voyage = findVoyage(voyageNumber);
    Location location = findLocation(unlocode);

    try {
      if (voyage == null) {
        return new HandlingEvent(cargo, completionTime, registrationTime, type, location);
      } else {
        return new HandlingEvent(cargo, completionTime, registrationTime, type, location, voyage);
      }
    } catch (Exception e) {
      throw new CannotCreateHandlingEventException(e);
    }
  }

  private Cargo findCargo(TrackingId trackingId) throws UnknownCargoException {
    Cargo cargo = cargoRepository.find(trackingId);

    if (cargo == null) {
      throw new UnknownCargoException(trackingId);
    }

    return cargo;
  }

  private Voyage findVoyage(VoyageNumber voyageNumber) throws UnknownVoyageException {
    if (voyageNumber == null) {
      return null;
    }

    Voyage voyage = voyageRepository.find(voyageNumber);

    if (voyage == null) {
      throw new UnknownVoyageException(voyageNumber);
    }

    return voyage;
  }

  private Location findLocation(UnLocode unlocode) throws UnknownLocationException {
    Location location = locationRepository.find(unlocode);

    if (location == null) {
      throw new UnknownLocationException(unlocode);
    }

    return location;
  }
}
