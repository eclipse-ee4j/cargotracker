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
