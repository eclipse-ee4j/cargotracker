/*
    The MIT License
    
    Copyright (c) 2019 Oracle and/or its affiliates
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/
package jakarta.cargotracker.interfaces.booking.facade.internal.assembler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import jakarta.cargotracker.domain.model.cargo.Itinerary;
import jakarta.cargotracker.domain.model.cargo.Leg;
import jakarta.cargotracker.domain.model.location.Location;
import jakarta.cargotracker.domain.model.location.LocationRepository;
import jakarta.cargotracker.domain.model.location.UnLocode;
import jakarta.cargotracker.domain.model.voyage.Voyage;
import jakarta.cargotracker.domain.model.voyage.VoyageNumber;
import jakarta.cargotracker.domain.model.voyage.VoyageRepository;
import jakarta.cargotracker.interfaces.booking.facade.dto.RouteCandidate;

public class ItineraryCandidateDtoAssembler {

    private static final SimpleDateFormat DATE_FORMAT
            = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");

    public RouteCandidate toDTO(Itinerary itinerary) {
        List<jakarta.cargotracker.interfaces.booking.facade.dto.Leg> legDTOs = new ArrayList<>(
                itinerary.getLegs().size());
        for (Leg leg : itinerary.getLegs()) {
            legDTOs.add(toLegDTO(leg));
        }
        return new RouteCandidate(legDTOs);
    }

    protected jakarta.cargotracker.interfaces.booking.facade.dto.Leg toLegDTO(
            Leg leg) {
        VoyageNumber voyageNumber = leg.getVoyage().getVoyageNumber();
        return new jakarta.cargotracker.interfaces.booking.facade.dto.Leg(
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

        for (jakarta.cargotracker.interfaces.booking.facade.dto.Leg legDTO
                : routeCandidateDTO.getLegs()) {
            VoyageNumber voyageNumber = new VoyageNumber(
                    legDTO.getVoyageNumber());
            Voyage voyage = voyageRepository.find(voyageNumber);
            Location from = locationRepository.find(new UnLocode(legDTO
                    .getFromUnLocode()));
            Location to = locationRepository.find(new UnLocode(legDTO.getToUnLocode()));

            try {
                legs.add(new Leg(voyage, from, to,
                        DATE_FORMAT.parse(legDTO.getLoadTime()),
                        DATE_FORMAT.parse(legDTO.getUnloadTime())));
            } catch (ParseException ex) {
                throw new RuntimeException("Could not parse date", ex);
            }
        }

        return new Itinerary(legs);
    }
}
