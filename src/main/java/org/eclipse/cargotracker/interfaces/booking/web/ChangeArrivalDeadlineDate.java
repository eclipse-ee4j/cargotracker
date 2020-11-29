package org.eclipse.cargotracker.interfaces.booking.web;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.primefaces.PrimeFaces;

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
	private Date arrivalDeadlineDate;

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

	public Date getArrivalDeadlineDate() {
		return this.arrivalDeadlineDate;
	}

	public void setArrivalDeadlineDate(Date arrivalDeadlineDate) {
		this.arrivalDeadlineDate = arrivalDeadlineDate;
	}

	public void load() {
		cargo = bookingServiceFacade.loadCargoForRouting(trackingId);
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try {
			arrivalDeadlineDate = df.parse(cargo.getArrivalDeadline());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void changeArrivalDeadline() {
		bookingServiceFacade.changeDeadline(trackingId, arrivalDeadlineDate);
		PrimeFaces.current().dialog().closeDynamic("DONE");
	}
}