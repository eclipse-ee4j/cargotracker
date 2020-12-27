package org.eclipse.cargotracker.interfaces.booking.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.Location;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.primefaces.PrimeFaces;

@Named
@FlowScoped("booking")
public class Booking implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final long MIN_JOURNEY_DURATION = 1; // Journey should be 1 day minimum.
	private static final long GRACE_PERIOD = DateTimeConstants.MILLIS_PER_HOUR * 4;

	private Date today = null;
	private List<Location> locations;

	private String originUnlocode;
	private String originName;
	private String destinationName;
	private String destinationUnlocode;
	private Date arrivalDeadline;

	private boolean bookable = false;
	private long duration = -1;

	@Inject
	private BookingServiceFacade bookingServiceFacade;

	@PostConstruct
	public void init() {
		today = LocalDate.now().toDate();
		locations = bookingServiceFacade.listShippingLocations();
	}

	public List<Location> getLocations() {
		List<Location> filteredLocations = new ArrayList<>();
		String locationToRemove = null;

		// TODO [Jakarta EE 8] Use injection instead?
		if (FacesContext.getCurrentInstance().getViewRoot().getViewId().endsWith("destination.xhtml")) {
			// In the destination menu, origin can't be selected.
			locationToRemove = originUnlocode;
		} else { // Vice-versa.
			if (destinationUnlocode != null) {
				locationToRemove = destinationUnlocode;
			}
		}

		for (Location location : locations) {
			if (!location.getUnLocode().equalsIgnoreCase(locationToRemove)) {
				filteredLocations.add(location);
			}
		}

		return filteredLocations;
	}

	public String getOriginUnlocode() {
		return originUnlocode;
	}

	public void setOriginUnlocode(String originUnlocode) {
		this.originUnlocode = originUnlocode;
		for (Location location : locations) {
			if (location.getUnLocode().equalsIgnoreCase(originUnlocode)) {
				this.originName = location.getNameOnly();
			}
		}
	}

	public String getOriginName() {
		return originName;
	}

	public String getDestinationUnlocode() {
		return destinationUnlocode;
	}

	public void setDestinationUnlocode(String destinationUnlocode) {
		this.destinationUnlocode = destinationUnlocode;
		for (Location location : locations) {
			if (location.getUnLocode().equalsIgnoreCase(destinationUnlocode)) {
				destinationName = location.getNameOnly();
			}
		}
	}

	public String getDestinationName() {
		return destinationName;
	}

	public Date getArrivalDeadline() {
		return arrivalDeadline;
	}

	public void setArrivalDeadline(Date arrivalDeadline) {
		this.arrivalDeadline = arrivalDeadline;
	}

	public Date getToday() {
		return today;
	}

	public long getDuration() {
		return duration;
	}

	public boolean isBookable() {
		return bookable;
	}

	public void deadlineUpdated() {
		// TODO [Jakarta EE 8] Use Date-Time API instead.
		duration = new Interval(today.getTime(), arrivalDeadline.getTime() + GRACE_PERIOD).toDuration()
				.getStandardDays();

		if (duration >= MIN_JOURNEY_DURATION) {
			bookable = true;
		} else {
			bookable = false;
		}

		PrimeFaces.current().ajax().update("dateForm:durationPanel");
		PrimeFaces.current().ajax().update("dateForm:bookBtn");
	}

	public String register() {
		if (!originUnlocode.equals(destinationUnlocode)) {
			bookingServiceFacade.bookNewCargo(originUnlocode, destinationUnlocode, arrivalDeadline);
		} else {
			// TODO [Jakarta EE 8] See if this can be injected.
			FacesContext context = FacesContext.getCurrentInstance();
			// UI now prevents from selecting same origin/destination
			FacesMessage message = new FacesMessage("Origin and destination cannot be the same.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			return null;
		}

		return "/admin/dashboard.xhtml";
	}

	public String getReturnValue() {
		return "/admin/dashboard.xhtml";
	}
}
