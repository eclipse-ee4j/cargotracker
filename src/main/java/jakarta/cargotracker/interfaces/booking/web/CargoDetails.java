package jakarta.cargotracker.interfaces.booking.web;

import jakarta.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import jakarta.cargotracker.interfaces.booking.facade.dto.CargoRoute;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Handles viewing cargo details. Operates against a dedicated service facade,
 * and could easily be rewritten as a thick Swing client. Completely separated
 * from the domain layer, unlike the tracking user interface.
 * <p/>
 * In order to successfully keep the domain model shielded from user interface
 * considerations, this approach is generally preferred to the one taken in the
 * tracking controller. However, there is never any one perfect solution for all
 * situations, so we've chosen to demonstrate two polarized ways to build user
 * interfaces.
 *
 * @see jakarta.cargotracker.interfaces.tracking.CargoTrackingController
 */
@Named
@RequestScoped
public class CargoDetails {

    // TODO Use this format for date rendering.
    private static final String FORMAT = "yyyy-MM-dd hh:mm";
    private String trackingId;
    private CargoRoute cargo;
    @Inject
    private BookingServiceFacade bookingServiceFacade;

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
