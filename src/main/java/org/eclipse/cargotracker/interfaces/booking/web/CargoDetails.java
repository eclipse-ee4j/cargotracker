package org.eclipse.cargotracker.interfaces.booking.web;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;

/**
 * Handles viewing cargo details. Operates against a dedicated service facade, and could easily be
 * rewritten as a thick Swing client. Completely separated from the domain layer, unlike the
 * tracking user interface.
 *
 * <p>In order to successfully keep the domain model shielded from user interface considerations,
 * this approach is generally preferred to the one taken in the tracking controller. However, there
 * is never any one perfect solution for all situations, so we've chosen to demonstrate two
 * polarized ways to build user interfaces.
 */
@Named
@RequestScoped
public class CargoDetails {

  private String trackingId;
  private CargoRoute cargo;
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

  public void load() {
    cargo = bookingServiceFacade.loadCargoForRouting(trackingId);
  }
}
