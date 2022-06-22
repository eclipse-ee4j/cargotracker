package org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.cargotracker.application.util.DateConverter;
import org.eclipse.cargotracker.domain.model.cargo.Itinerary;
import org.eclipse.cargotracker.domain.model.cargo.Leg;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.LocationRepository;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.eclipse.cargotracker.domain.model.voyage.VoyageRepository;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.RouteCandidate;

@ApplicationScoped
public class ItineraryCandidateDtoAssembler {

  @Inject private LocationDtoAssembler locationDtoAssembler;

  public RouteCandidate toDto(Itinerary itinerary) {
    List<org.eclipse.cargotracker.interfaces.booking.facade.dto.Leg> legDTOs =
        itinerary.getLegs().stream().map(this::toLegDTO).collect(Collectors.toList());
    return new RouteCandidate(legDTOs);
  }

  protected org.eclipse.cargotracker.interfaces.booking.facade.dto.Leg toLegDTO(Leg leg) {
    VoyageNumber voyageNumber = leg.getVoyage().getVoyageNumber();
    return new org.eclipse.cargotracker.interfaces.booking.facade.dto.Leg(
        voyageNumber.getIdString(),
        locationDtoAssembler.toDto(leg.getLoadLocation()),
        locationDtoAssembler.toDto(leg.getUnloadLocation()),
        leg.getLoadTime(),
        leg.getUnloadTime());
  }

  public Itinerary fromDTO(
      RouteCandidate routeCandidateDTO,
      VoyageRepository voyageRepository,
      LocationRepository locationRepository) {
    List<Leg> legs = new ArrayList<>(routeCandidateDTO.getLegs().size());

    for (org.eclipse.cargotracker.interfaces.booking.facade.dto.Leg legDTO :
        routeCandidateDTO.getLegs()) {
      VoyageNumber voyageNumber = new VoyageNumber(legDTO.getVoyageNumber());
      Voyage voyage = voyageRepository.find(voyageNumber);
      Location from = locationRepository.find(new UnLocode(legDTO.getFromUnLocode()));
      Location to = locationRepository.find(new UnLocode(legDTO.getToUnLocode()));

      legs.add(
          new Leg(
              voyage,
              from,
              to,
              DateConverter.toDateTime(legDTO.getLoadTime()),
              DateConverter.toDateTime(legDTO.getUnloadTime())));
    }

    return new Itinerary(legs);
  }
}
