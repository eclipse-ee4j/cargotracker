package org.eclipse.cargotracker.interfaces.booking.web;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.Location;

/**
 * Handles registering cargo. Operates against a dedicated service facade, and
 * could easily be rewritten as a thick Swing client. Completely separated from
 * the domain layer, unlike the tracking user interface.
 * <p/>
 * In order to successfully keep the domain model shielded from user interface
 * considerations, this approach is generally preferred to the one taken in the
 * tracking controller. However, there is never any one perfect solution for all
 * situations, so we've chosen to demonstrate two polarized ways to build user
 * interfaces.
 */
@Named
@ViewScoped
public class Registration implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String FORMAT = "yyyy-MM-dd";

	List<Location> locations;
	private String arrivalDeadline;
	private String originUnlocode;
	private String destinationUnlocode;

	@Inject
	private BookingServiceFacade bookingServiceFacade;

	public List<Location> getLocations() {
		return locations;
	}

	public String getArrivalDeadline() {
		return arrivalDeadline;
	}

	public void setArrivalDeadline(String arrivalDeadline) {
		this.arrivalDeadline = arrivalDeadline;
	}

	public String getOriginUnlocode() {
		return originUnlocode;
	}

	public void setOriginUnlocode(String originUnlocode) {
		this.originUnlocode = originUnlocode;
	}

	public String getDestinationUnlocode() {
		return destinationUnlocode;
	}

	public void setDestinationUnlocode(String destinationUnlocode) {
		this.destinationUnlocode = destinationUnlocode;
	}

	@PostConstruct
	public void init() {
		locations = bookingServiceFacade.listShippingLocations();
	}

	public String register() {
		String trackingId = null;

		try {
			if (!originUnlocode.equals(destinationUnlocode)) {
				trackingId = bookingServiceFacade.bookNewCargo(originUnlocode, destinationUnlocode,
						new SimpleDateFormat(FORMAT).parse(arrivalDeadline));
			} else {
				// TODO [Jakarta EE 8] See if this can be injected.
				FacesContext context = FacesContext.getCurrentInstance();
				FacesMessage message = new FacesMessage("Origin and destination cannot be the same.");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				context.addMessage(null, message);
				return null;
			}
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing date", e);
		}

		return "show.xhtml?faces-redirect=true&trackingId=" + trackingId;
	}
}
