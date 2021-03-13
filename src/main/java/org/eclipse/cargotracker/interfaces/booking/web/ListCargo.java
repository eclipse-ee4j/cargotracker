package org.eclipse.cargotracker.interfaces.booking.web;

import static java.util.stream.Collectors.toList;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;

/**
 * Handles listing cargo. Operates against a dedicated service facade, and could easily be rewritten
 * as a thick Swing client. Completely separated from the domain layer, unlike the tracking user
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

  private List<CargoRoute> cargos;
  private List<CargoRoute> routedCargos;
  private List<CargoRoute> claimedCargos;
  private List<CargoRoute> routedUnclaimedCargos;

  public List<CargoRoute> getCargos() {
    return cargos;
  }

  @PostConstruct
  public void init() {
    cargos = bookingServiceFacade.listAllCargos();
  }

  public List<CargoRoute> getRoutedCargos() {
    List<CargoRoute> routedCargos = cargos.stream().filter(CargoRoute::isRouted).collect(toList());

    return routedCargos;
  }

  public List<CargoRoute> getRoutedUnclaimedCargos() {
    List<CargoRoute> routedUnclaimedCargos =
        cargos.stream().filter(route -> route.isRouted() && !route.isClaimed()).collect(toList());

    return routedUnclaimedCargos;
  }

  public List<CargoRoute> getClaimedCargos() {
    List<CargoRoute> claimedCargos =
        cargos.stream().filter(CargoRoute::isClaimed).collect(toList());

    return claimedCargos;
  }

  public List<CargoRoute> getNotRoutedCargos() {
    List<CargoRoute> notRoutedCargos =
        cargos.stream().filter(route -> !route.isRouted()).collect(toList());

    return notRoutedCargos;
  }
}
