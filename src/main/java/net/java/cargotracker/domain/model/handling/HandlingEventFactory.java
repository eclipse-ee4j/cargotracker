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
package net.java.cargotracker.domain.model.handling;

import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import net.java.cargotracker.domain.model.cargo.Cargo;
import net.java.cargotracker.domain.model.cargo.CargoRepository;
import net.java.cargotracker.domain.model.cargo.TrackingId;
import net.java.cargotracker.domain.model.location.Location;
import net.java.cargotracker.domain.model.location.LocationRepository;
import net.java.cargotracker.domain.model.location.UnLocode;
import net.java.cargotracker.domain.model.voyage.Voyage;
import net.java.cargotracker.domain.model.voyage.VoyageNumber;
import net.java.cargotracker.domain.model.voyage.VoyageRepository;

@ApplicationScoped
public class HandlingEventFactory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private CargoRepository cargoRepository;
    @Inject
    private VoyageRepository voyageRepository;
    @Inject
    private LocationRepository locationRepository;

    /**
     * @param registrationTime time when this event was received by the system
     * @param completionTime when the event was completed, for example finished
     * loading
     * @param trackingId cargo tracking id
     * @param voyageNumber voyage number
     * @param unlocode United Nations Location Code for the location of the
     * event
     * @param type type of event
     * @throws UnknownVoyageException if there's no voyage with this number
     * @throws UnknownCargoException if there's no cargo with this tracking id
     * @throws UnknownLocationException if there's no location with this UN
     * Locode
     * @return A handling event.
     */
    // TODO Look at the exception handling more seriously.
    public HandlingEvent createHandlingEvent(Date registrationTime,
            Date completionTime, TrackingId trackingId,
            VoyageNumber voyageNumber, UnLocode unlocode,
            HandlingEvent.Type type) throws CannotCreateHandlingEventException {
        Cargo cargo = findCargo(trackingId);
        Voyage voyage = findVoyage(voyageNumber);
        Location location = findLocation(unlocode);

        try {
            if (voyage == null) {
                return new HandlingEvent(cargo, completionTime,
                        registrationTime, type, location);
            } else {
                return new HandlingEvent(cargo, completionTime,
                        registrationTime, type, location, voyage);
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

    private Voyage findVoyage(VoyageNumber voyageNumber)
            throws UnknownVoyageException {
        if (voyageNumber == null) {
            return null;
        }

        Voyage voyage = voyageRepository.find(voyageNumber);

        if (voyage == null) {
            throw new UnknownVoyageException(voyageNumber);
        }

        return voyage;
    }

    private Location findLocation(UnLocode unlocode)
            throws UnknownLocationException {
        Location location = locationRepository.find(unlocode);

        if (location == null) {
            throw new UnknownLocationException(unlocode);
        }

        return location;
    }
}
