package org.eclipse.cargotracker.interfaces.booking.web;

import static java.util.stream.Collectors.toList;

import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;

/**
 * Handles listing cargo. Operates against a dedicated service facade, and could easily be rewritten
 * as a thick client. Completely separated from the domain layer, unlike the tracking user
 * interface.
 *
 * <p>In order to successfully keep the domain model shielded from user interface considerations,
 * this approach is generally preferred to the one taken in the tracking controller. However, there
 * is never any one perfect solution for all situations, so we've chosen to demonstrate two
 * polarized ways to build user interfaces.
 */
@Named
@RequestScoped
public class ListCargo {

  @Inject private BookingServiceFacade bookingServiceFacade;

  private List<CargoRoute> notRoutedCargos;
  private List<CargoRoute> routedUnclaimedCargos;
  private List<CargoRoute> claimedCargos;

  @PostConstruct
  public void init() {
    List<CargoRoute> cargos = bookingServiceFacade.listAllCargos();
    notRoutedCargos = cargos.stream().filter(route -> !route.isRouted()).collect(toList());
    routedUnclaimedCargos =
        cargos.stream().filter(route -> route.isRouted() && !route.isClaimed()).collect(toList());
    claimedCargos = cargos.stream().filter(CargoRoute::isClaimed).collect(toList());
  }

  public List<CargoRoute> getNotRoutedCargos() {
    return notRoutedCargos;
  }

  public List<CargoRoute> getRoutedUnclaimedCargos() {
    return routedUnclaimedCargos;
  }

  public List<CargoRoute> getClaimedCargos() {
    return claimedCargos;
  }
}
