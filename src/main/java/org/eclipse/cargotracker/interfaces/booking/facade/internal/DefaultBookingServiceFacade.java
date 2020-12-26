package org.eclipse.cargotracker.interfaces.booking.facade.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	@Inject
	private BookingService bookingService;

	@Inject
	private LocationRepository locationRepository;

	@Inject
	private CargoRepository cargoRepository;

	@Inject
	private VoyageRepository voyageRepository;

	@Inject
	private HandlingEventRepository handlingEventRepository;

	@Override
	public List<org.eclipse.cargotracker.interfaces.booking.facade.dto.Location> listShippingLocations() {
		List<Location> allLocations = locationRepository.findAll();
		LocationDtoAssembler assembler = new LocationDtoAssembler();
		return assembler.toDtoList(allLocations);
	}

	@Override
	public String bookNewCargo(String origin, String destination, Date arrivalDeadline) {
		TrackingId trackingId = bookingService.bookNewCargo(new UnLocode(origin), new UnLocode(destination),
				arrivalDeadline);
		return trackingId.getIdString();
	}

	@Override
	public CargoRoute loadCargoForRouting(String trackingId) {
		Cargo cargo = cargoRepository.find(new TrackingId(trackingId));
		CargoRouteDtoAssembler assembler = new CargoRouteDtoAssembler();
		return assembler.toDto(cargo);
	}

	@Override
	public void assignCargoToRoute(String trackingIdStr, RouteCandidate routeCandidateDTO) {
		Itinerary itinerary = new ItineraryCandidateDtoAssembler().fromDTO(routeCandidateDTO, voyageRepository,
				locationRepository);
		TrackingId trackingId = new TrackingId(trackingIdStr);

		bookingService.assignCargoToRoute(itinerary, trackingId);
	}

	@Override
	public void changeDestination(String trackingId, String destinationUnLocode) {
		bookingService.changeDestination(new TrackingId(trackingId), new UnLocode(destinationUnLocode));
	}

	@Override
	public void changeDeadline(String trackingId, Date arrivalDeadline) {
		bookingService.changeDeadline(new TrackingId(trackingId), arrivalDeadline);
	}

	@Override
	// TODO [DDD] Is this the correct DTO here?
	public List<CargoRoute> listAllCargos() {
		List<Cargo> cargos = cargoRepository.findAll();
		List<CargoRoute> routes = new ArrayList<>(cargos.size());

		CargoRouteDtoAssembler assembler = new CargoRouteDtoAssembler();

		for (Cargo cargo : cargos) {
			routes.add(assembler.toDto(cargo));
		}

		return routes;
	}

	@Override
	public List<String> listAllTrackingIds() {
		List<String> trackingIds = new ArrayList<>();

		for (Cargo cargo : cargoRepository.findAll())
			trackingIds.add(cargo.getTrackingId().getIdString());

		return trackingIds;
	}

	@Override
	public CargoStatus loadCargoForTracking(String trackingIdValue) {
		TrackingId trackingId = new TrackingId(trackingIdValue);
		Cargo cargo = cargoRepository.find(trackingId);

		if (cargo == null) {
			return null;
		}

		CargoStatusDtoAssembler assembler = new CargoStatusDtoAssembler();

		List<HandlingEvent> handlingEvents = handlingEventRepository.lookupHandlingHistoryOfCargo(trackingId)
				.getDistinctEventsByCompletionTime();

		return assembler.toDto(cargo, handlingEvents);
	}

	@Override
	public List<RouteCandidate> requestPossibleRoutesForCargo(String trackingId) {
		List<Itinerary> itineraries = bookingService.requestPossibleRoutesForCargo(new TrackingId(trackingId));

		List<RouteCandidate> routeCandidates = new ArrayList<>(itineraries.size());
		ItineraryCandidateDtoAssembler dtoAssembler = new ItineraryCandidateDtoAssembler();
		for (Itinerary itinerary : itineraries) {
			routeCandidates.add(dtoAssembler.toDto(itinerary));
		}

		return routeCandidates;
	}
}
