package org.eclipse.cargotracker.interfaces.booking.web;

import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.primefaces.PrimeFaces;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Handles changing the cargo destination. Operates against a dedicated service
 * facade, and could easily be rewritten as a thick Swing client. Completely
 * separated from the domain layer, unlike the tracking user interface.
 * <p/>
 * In order to successfully keep the domain model shielded from user interface
 * considerations, this approach is generally preferred to the one taken in the
 * tracking controller. However, there is never any one perfect solution for all
 * situations, so we've chosen to demonstrate two polarized ways to build user
 * interfaces.
 *
 */
@Named
@ViewScoped
public class ChangeArrivalDeadlineDate implements Serializable {

    private static final long serialVersionUID = 1L;
    private String trackingId;
    private CargoRoute cargo;
    private LocalDate arrivalDeadlineDate;
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

    public LocalDate getArrivalDeadlineDate() {
        return this.arrivalDeadlineDate;
    }

    public void setArrivalDeadlineDate(LocalDate arrivalDeadlineDate) {
        this.arrivalDeadlineDate = arrivalDeadlineDate;
    }

    public void load() {
        cargo = bookingServiceFacade.loadCargoForRouting(trackingId);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a z");
        try {
            arrivalDeadlineDate = ZonedDateTime.parse(cargo.getArrivalDeadline(), df).toLocalDate();
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
    }

    public void changeArrivalDeadline() {
        bookingServiceFacade.changeDeadline(trackingId, arrivalDeadlineDate.atStartOfDay());
        PrimeFaces.current().dialog().closeDynamic("DONE");
    }
}