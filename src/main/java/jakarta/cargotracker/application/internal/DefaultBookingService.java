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
package jakarta.cargotracker.application.internal;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import jakarta.cargotracker.application.BookingService;
import jakarta.cargotracker.domain.model.cargo.Cargo;
import jakarta.cargotracker.domain.model.cargo.CargoRepository;
import jakarta.cargotracker.domain.model.cargo.Itinerary;
import jakarta.cargotracker.domain.model.cargo.RouteSpecification;
import jakarta.cargotracker.domain.model.cargo.TrackingId;
import jakarta.cargotracker.domain.model.location.Location;
import jakarta.cargotracker.domain.model.location.LocationRepository;
import jakarta.cargotracker.domain.model.location.UnLocode;
import jakarta.cargotracker.domain.service.RoutingService;

@Stateless
public class DefaultBookingService implements BookingService {

    @Inject
    private CargoRepository cargoRepository;
    @Inject
    private LocationRepository locationRepository;
    @Inject
    private RoutingService routingService;
    // TODO See if the logger can be injected.
    private static final Logger logger = Logger.getLogger(
            DefaultBookingService.class.getName());

    @Override
    public TrackingId bookNewCargo(UnLocode originUnLocode,
            UnLocode destinationUnLocode,
            Date arrivalDeadline) {
        TrackingId trackingId = cargoRepository.nextTrackingId();
        Location origin = locationRepository.find(originUnLocode);
        Location destination = locationRepository.find(destinationUnLocode);
        RouteSpecification routeSpecification = new RouteSpecification(origin,
                destination, arrivalDeadline);

        Cargo cargo = new Cargo(trackingId, routeSpecification);

        cargoRepository.store(cargo);
        logger.log(Level.INFO, "Booked new cargo with tracking id {0}",
                cargo.getTrackingId().getIdString());

        return cargo.getTrackingId();
    }

    @Override
    public List<Itinerary> requestPossibleRoutesForCargo(TrackingId trackingId) {
        Cargo cargo = cargoRepository.find(trackingId);

        if (cargo == null) {
            return Collections.emptyList();
        }

        return routingService.fetchRoutesForSpecification(cargo.getRouteSpecification());
    }

    @Override
    public void assignCargoToRoute(Itinerary itinerary, TrackingId trackingId) {
        Cargo cargo = cargoRepository.find(trackingId);

        cargo.assignToRoute(itinerary);
        cargoRepository.store(cargo);

        logger.log(Level.INFO, "Assigned cargo {0} to new route", trackingId);
    }

    @Override
    public void changeDestination(TrackingId trackingId, UnLocode unLocode) {
        Cargo cargo = cargoRepository.find(trackingId);
        Location newDestination = locationRepository.find(unLocode);

        RouteSpecification routeSpecification = new RouteSpecification(
                cargo.getOrigin(), newDestination,
                cargo.getRouteSpecification().getArrivalDeadline());
        cargo.specifyNewRoute(routeSpecification);

        cargoRepository.store(cargo);

        logger.log(Level.INFO, "Changed destination for cargo {0} to {1}",
                new Object[]{trackingId, routeSpecification.getDestination()});
    }
}
