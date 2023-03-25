package org.eclipse.cargotracker.domain.service;

import java.util.List;
import org.eclipse.cargotracker.domain.model.cargo.Itinerary;
import org.eclipse.cargotracker.domain.model.cargo.RouteSpecification;

public interface RoutingService {

  /**
   * @param routeSpecification Route specification
   * @return A list of itineraries that satisfy the specification. May be an empty list if no route
   *     is found.
   */
  List<Itinerary> fetchRoutesForSpecification(RouteSpecification routeSpecification);
}
