package org.eclipse.cargotracker.interfaces.booking.web;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.Location;
import org.primefaces.PrimeFaces;

/**
 * Handles changing the cargo destination. Operates against a dedicated service facade, and could
 * easily be rewritten as a thick Swing client. Completely separated from the domain layer, unlike
 * the tracking user interface.
 *
 * <p>In order to successfully keep the domain model shielded from user interface considerations,
 * this approach is generally preferred to the one taken in the tracking controller. However, there
 * is never any one perfect solution for all situations, so we've chosen to demonstrate two
 * polarized ways to build user interfaces.
 */
@Named
@ViewScoped
public class ChangeDestination implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @Inject private BookingServiceFacade bookingServiceFacade;

  private String trackingId;
  private CargoRoute cargo;
  private List<Location> locations;
  private String destinationUnlocode;

  public String getTrackingId() {
    return trackingId;
  }

  public void setTrackingId(String trackingId) {
    this.trackingId = trackingId;
  }

  public CargoRoute getCargo() {
    return cargo;
  }

  public List<Location> getLocations() {
    return locations;
  }

  public List<Location> getPotentialDestinations() {
    String originCode = cargo.getOriginCode().toLowerCase();
    String destinationCode = cargo.getFinalDestinationCode().toLowerCase();

    return locations.stream()
            .filter(location ->
                    !location.getUnLocode().toLowerCase().equals(originCode) &&
                    !location.getUnLocode().toLowerCase().equals(destinationCode)
            )
            .toList();
  }

  public String getDestinationUnlocode() {
    return destinationUnlocode;
  }

  public void setDestinationUnlocode(String destinationUnlocode) {
    this.destinationUnlocode = destinationUnlocode;
  }

  public void load() {
    locations = bookingServiceFacade.listShippingLocations();
    cargo = bookingServiceFacade.loadCargoForRouting(trackingId);
  }

  public void changeDestination() {
    bookingServiceFacade.changeDestination(trackingId, destinationUnlocode);
    // PF.current().dialog().closeDynamic("DONE");
    PrimeFaces.current().dialog().closeDynamic("DONE");
    // RequestContext.getCurrentInstance().closeDialog("DONE");
  }
}
