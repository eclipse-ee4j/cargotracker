package org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler;

import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.RoutingStatus;
import org.eclipse.cargotracker.domain.model.cargo.TransportStatus;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;

// TODO [Clean Code] Convert to a singleton and inject?
public class CargoRouteDtoAssembler {

  public CargoRoute toDto(Cargo cargo) {
    CargoRoute dto =
        new CargoRoute(
            cargo.getTrackingId().getIdString(),
            cargo.getOrigin().getName()
                + " ("
                + cargo.getOrigin().getUnLocode().getIdString()
                + ")",
            cargo.getRouteSpecification().getDestination().getName()
                + " ("
                + cargo.getRouteSpecification().getDestination().getUnLocode().getIdString()
                + ")",
            cargo.getRouteSpecification().getArrivalDeadline(),
            cargo.getDelivery().getRoutingStatus().sameValueAs(RoutingStatus.MISROUTED),
            cargo.getDelivery().getTransportStatus().sameValueAs(TransportStatus.CLAIMED),
            cargo.getDelivery().getLastKnownLocation().getName()
                + " ("
                + cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString()
                + ")",
            cargo.getDelivery().getTransportStatus().name());

    cargo
        .getItinerary()
        .getLegs()
        .forEach(
            leg ->
                dto.addLeg(
                    leg.getVoyage().getVoyageNumber().getIdString(),
                    leg.getLoadLocation().getUnLocode().getIdString(),
                    leg.getLoadLocation().getName(),
                    leg.getUnloadLocation().getUnLocode().getIdString(),
                    leg.getUnloadLocation().getName(),
                    leg.getLoadTime(),
                    leg.getUnloadTime()));

    return dto;
  }
}
