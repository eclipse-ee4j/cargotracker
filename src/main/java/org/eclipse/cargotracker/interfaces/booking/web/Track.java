package org.eclipse.cargotracker.interfaces.booking.web;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoStatus;

/**
 * Handles tracking cargo. Operates against a dedicated service facade, and could easily be
 * rewritten as a thick Swing client. Completely separated from the domain layer, unlike the public
 * tracking user interface.
 *
 * <p>In order to successfully keep the domain model shielded from user interface considerations,
 * this approach is generally preferred to the one taken in the public tracking controller. However,
 * there is never any one perfect solution for all situations, so we've chosen to demonstrate two
 * polarized ways to build user interfaces.
 */
@Named("admin.track")
@ViewScoped
public class Track implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject private BookingServiceFacade bookingServiceFacade;

  private List<String> trackingIds;
  private String trackingId;
  private CargoStatus cargo;

  public List<String> getTrackingIds(String query) {
    return trackingIds;
  }

  public String getTrackingId() {
    return trackingId;
  }

  public void setTrackingId(String trackingId) {
    this.trackingId = trackingId;
  }

  public CargoStatus getCargo() {
    return this.cargo;
  }

  @PostConstruct
  public void init() {
    trackingIds = bookingServiceFacade.listAllTrackingIds();
  }

  public void onTrackById() {
    cargo = bookingServiceFacade.loadCargoForTracking(this.trackingId);
  }
}
