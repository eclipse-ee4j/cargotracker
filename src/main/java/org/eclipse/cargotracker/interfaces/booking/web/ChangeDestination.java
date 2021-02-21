package org.eclipse.cargotracker.interfaces.booking.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
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

  private static final long serialVersionUID = 1L;

  private String trackingId;
  private CargoRoute cargo;
  private List<Location> locations;
  private String destinationUnlocode;

  @Inject private BookingServiceFacade bookingServiceFacade;

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
    // Potential destination = All Locations - Origin - Current Destination
    List<Location> destinationsToRemove = new ArrayList<>();
    for (Location loc : locations) {
      if (loc.getName().equalsIgnoreCase(cargo.getOrigin())
          || loc.getName().equalsIgnoreCase(cargo.getFinalDestination())) {
        destinationsToRemove.add(loc);
      }
    }
    locations.removeAll(destinationsToRemove);
    return locations;
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
