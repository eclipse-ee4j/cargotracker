package org.eclipse.cargotracker.interfaces.booking.facade.internal;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.cargotracker.application.BookingService;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.CargoRepository;
import org.eclipse.cargotracker.domain.model.cargo.Itinerary;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.handling.HandlingEventRepository;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.LocationRepository;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.VoyageRepository;
import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoStatus;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.RouteCandidate;
import org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler.CargoRouteDtoAssembler;
import org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler.CargoStatusDtoAssembler;
import org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler.ItineraryCandidateDtoAssembler;
import org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler.LocationDtoAssembler;

@ApplicationScoped
public class DefaultBookingServiceFacade implements BookingServiceFacade, Serializable {

  private static final long serialVersionUID = 1L;

  @Inject private BookingService bookingService;
  @Inject private LocationRepository locationRepository;
  @Inject private CargoRepository cargoRepository;
  @Inject private VoyageRepository voyageRepository;
  @Inject private HandlingEventRepository handlingEventRepository;
  @Inject private CargoRouteDtoAssembler cargoRouteDtoAssembler;
  @Inject private CargoStatusDtoAssembler cargoStatusDtoAssembler;
  @Inject private ItineraryCandidateDtoAssembler itineraryCandidateDtoAssembler;
  @Inject private LocationDtoAssembler locationDtoAssembler;

  @Override
  public List<org.eclipse.cargotracker.interfaces.booking.facade.dto.Location>
      listShippingLocations() {
    List<Location> allLocations = locationRepository.findAll();
    return locationDtoAssembler.toDtoList(allLocations);
  }

  @Override
  public String bookNewCargo(String origin, String destination, LocalDate arrivalDeadline) {
    TrackingId trackingId =
        bookingService.bookNewCargo(
            new UnLocode(origin), new UnLocode(destination), arrivalDeadline);
    return trackingId.getIdString();
  }

  @Override
  public CargoRoute loadCargoForRouting(String trackingId) {
    Cargo cargo = cargoRepository.find(new TrackingId(trackingId));
    return cargoRouteDtoAssembler.toDto(cargo);
  }

  @Override
  public void assignCargoToRoute(String trackingIdStr, RouteCandidate routeCandidateDTO) {
    Itinerary itinerary =
        itineraryCandidateDtoAssembler.fromDTO(
            routeCandidateDTO, voyageRepository, locationRepository);
    TrackingId trackingId = new TrackingId(trackingIdStr);

    bookingService.assignCargoToRoute(itinerary, trackingId);
  }

  @Override
  public void changeDestination(String trackingId, String destinationUnLocode) {
    bookingService.changeDestination(new TrackingId(trackingId), new UnLocode(destinationUnLocode));
  }

  @Override
  public void changeDeadline(String trackingId, LocalDate arrivalDeadline) {
    bookingService.changeDeadline(new TrackingId(trackingId), arrivalDeadline);
  }

  @Override
  // TODO [DDD] Is this the correct DTO here?
  public List<CargoRoute> listAllCargos() {
    List<Cargo> cargos = cargoRepository.findAll();
    List<CargoRoute> routes;

    routes = cargos.stream().map(cargoRouteDtoAssembler::toDto).collect(Collectors.toList());

    return routes;
  }

  @Override
  public List<String> listAllTrackingIds() {
    List<String> trackingIds = new ArrayList<>();
    cargoRepository
        .findAll()
        .forEach(cargo -> trackingIds.add(cargo.getTrackingId().getIdString()));

    return trackingIds;
  }

  @Override
  public CargoStatus loadCargoForTracking(String trackingIdValue) {
    TrackingId trackingId = new TrackingId(trackingIdValue);
    Cargo cargo = cargoRepository.find(trackingId);

    if (cargo == null) {
      return null;
    }

    List<HandlingEvent> handlingEvents =
        handlingEventRepository
            .lookupHandlingHistoryOfCargo(trackingId)
            .getDistinctEventsByCompletionTime();

    return cargoStatusDtoAssembler.toDto(cargo, handlingEvents);
  }

  @Override
  public List<RouteCandidate> requestPossibleRoutesForCargo(String trackingId) {
    List<Itinerary> itineraries =
        bookingService.requestPossibleRoutesForCargo(new TrackingId(trackingId));

    List<RouteCandidate> routeCandidates =
        itineraries
            .stream()
            .map(itineraryCandidateDtoAssembler::toDto)
            .collect(Collectors.toList());

    return routeCandidates;
  }
}
