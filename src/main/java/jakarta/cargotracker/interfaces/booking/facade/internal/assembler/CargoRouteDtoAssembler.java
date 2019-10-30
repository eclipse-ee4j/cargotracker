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

import jakarta.cargotracker.domain.model.cargo.Cargo;
import jakarta.cargotracker.domain.model.cargo.Leg;
import jakarta.cargotracker.domain.model.cargo.RoutingStatus;
import jakarta.cargotracker.domain.model.cargo.TransportStatus;
import jakarta.cargotracker.interfaces.booking.facade.dto.CargoRoute;

// TODO Convert to a singleton and inject?
public class CargoRouteDtoAssembler {

    public CargoRoute toDto(Cargo cargo) {
        CargoRoute dto = new CargoRoute(
                cargo.getTrackingId().getIdString(),
                cargo.getOrigin().getName() + " (" + cargo.getOrigin().getUnLocode().getIdString() + ")",
                cargo.getRouteSpecification().getDestination().getName() + " (" + cargo.getRouteSpecification().getDestination().getUnLocode().getIdString() + ")",
                cargo.getRouteSpecification().getArrivalDeadline(),
                cargo.getDelivery().getRoutingStatus()
                .sameValueAs(RoutingStatus.MISROUTED),
                cargo.getDelivery().getTransportStatus()
                .sameValueAs(TransportStatus.CLAIMED),
                cargo.getDelivery().getLastKnownLocation().getName() + " (" + cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString() + ")",
                cargo.getDelivery().getTransportStatus().name());

        for (Leg leg : cargo.getItinerary().getLegs()) {
            dto.addLeg(
                    leg.getVoyage().getVoyageNumber().getIdString(),
                    leg.getLoadLocation().getUnLocode().getIdString(),
                    leg.getLoadLocation().getName(),
                    leg.getUnloadLocation().getUnLocode().getIdString(),
                    leg.getUnloadLocation().getName(),
                    leg.getLoadTime(), leg.getUnloadTime());
        }

        return dto;
    }
}
