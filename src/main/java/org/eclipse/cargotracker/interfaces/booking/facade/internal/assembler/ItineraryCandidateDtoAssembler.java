package org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler;

import org.eclipse.cargotracker.domain.model.cargo.Itinerary;
import org.eclipse.cargotracker.domain.model.cargo.Leg;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.LocationRepository;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.eclipse.cargotracker.domain.model.voyage.VoyageRepository;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.RouteCandidate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ItineraryCandidateDtoAssembler {

    private static final DateTimeFormatter DATE_FORMAT
            = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a z");

    public RouteCandidate toDTO(Itinerary itinerary) {
        List<org.eclipse.cargotracker.interfaces.booking.facade.dto.Leg> legDTOs = new ArrayList<>(
                itinerary.getLegs().size());
        for (Leg leg : itinerary.getLegs()) {
            legDTOs.add(toLegDTO(leg));
        }
        return new RouteCandidate(legDTOs);
    }

    protected org.eclipse.cargotracker.interfaces.booking.facade.dto.Leg toLegDTO(
            Leg leg) {
        VoyageNumber voyageNumber = leg.getVoyage().getVoyageNumber();
        return new org.eclipse.cargotracker.interfaces.booking.facade.dto.Leg(
                voyageNumber.getIdString(),
                leg.getLoadLocation().getUnLocode().getIdString(),
                leg.getLoadLocation().getName(),
                leg.getUnloadLocation().getUnLocode().getIdString(),
                leg.getUnloadLocation().getName(),
                leg.getLoadTime(),
                leg.getUnloadTime());
    }

    public Itinerary fromDTO(RouteCandidate routeCandidateDTO,
                             VoyageRepository voyageRepository,
                             LocationRepository locationRepository) {
        List<Leg> legs = new ArrayList<>(routeCandidateDTO.getLegs().size());

        for (org.eclipse.cargotracker.interfaces.booking.facade.dto.Leg legDTO
                : routeCandidateDTO.getLegs()) {
            VoyageNumber voyageNumber = new VoyageNumber(
                    legDTO.getVoyageNumber());
            Voyage voyage = voyageRepository.find(voyageNumber);
            Location from = locationRepository.find(new UnLocode(legDTO
                    .getFromUnLocode()));
            Location to = locationRepository.find(new UnLocode(legDTO.getToUnLocode()));

            try {
                legs.add(new Leg(voyage, from, to,
                        LocalDateTime.from(DATE_FORMAT.parse(legDTO.getLoadTime())),
                        LocalDateTime.from(DATE_FORMAT.parse(legDTO.getUnloadTime()))));
            } catch (DateTimeParseException ex) {
                throw new RuntimeException("Could not parse date", ex);
            }
        }

        return new Itinerary(legs);
    }
}
