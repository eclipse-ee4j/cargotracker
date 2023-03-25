package org.eclipse.cargotracker.interfaces.booking.facade;

import java.time.LocalDate;
import java.util.List;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoStatus;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.Location;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.RouteCandidate;

/**
 * This facade shields the domain layer - model, services, repositories - from concerns about such
 * things as the user interface and remote communication.
 */
public interface BookingServiceFacade {

  String bookNewCargo(String origin, String destination, LocalDate arrivalDeadline);

  CargoRoute loadCargoForRouting(String trackingId);

  CargoStatus loadCargoForTracking(String trackingId);

  void assignCargoToRoute(String trackingId, RouteCandidate route);

  void changeDestination(String trackingId, String destinationUnLocode);

  void changeDeadline(String trackingId, LocalDate arrivalDeadline);

  List<RouteCandidate> requestPossibleRoutesForCargo(String trackingId);

  List<Location> listShippingLocations();

  // TODO [DDD] Is this the right DTO here?
  List<CargoRoute> listAllCargos();

  List<String> listAllTrackingIds();
}
