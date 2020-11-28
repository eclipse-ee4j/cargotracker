package org.eclipse.cargotracker.domain.model.cargo;

import static org.eclipse.cargotracker.application.util.DateUtil.computeDuration;

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
import org.primefaces.PrimeFaces;

// TODO This needs to be moved out of the domain layer to the appropriate interface layer.
@Named
@FlowScoped("booking")
public class BookingBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;
	List<Location> locations;
	private Date arrivalDeadline;
	private String originUnlocode;
	private String originName;
	private String destinationName;
	private String destinationUnlocode;
	private String newTrackingId = null;
	private Date today = new Date();
	private boolean bookable = false;
	private long duration = -1;
	private final long MIN_JOURNEY_DURATION = 1; // journey should be 1 day min.

	@Inject
	private BookingServiceFacade bookingServiceFacade;

	@PostConstruct
	public void init() {
		locations = bookingServiceFacade.listShippingLocations();
	}

	public List<Location> getLocations() {

		List<Location> filteredLocations = new ArrayList<>();
		String locationToRemove = null;

		// TODO: there should be a better way to do tihs
		if (FacesContext.getCurrentInstance().getViewRoot().getViewId().endsWith("destination.xhtml")) {
			// in Destination menu, Orign can't be selected
			locationToRemove = originUnlocode;
		} else // and vice-versa
		if (destinationUnlocode != null) {
			locationToRemove = destinationUnlocode;
		}

		for (Location loc : locations) {
			if (!loc.getUnLocode().equalsIgnoreCase(locationToRemove)) {
				filteredLocations.add(loc);
			}
		}
		return filteredLocations;
	}

	public Date getArrivalDeadline() {
		return arrivalDeadline;
	}

	public String getOriginUnlocode() {
		return originUnlocode;
	}

	public String getOriginName() {
		return originName;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setOriginUnlocode(String originUnlocode) {
		this.originUnlocode = originUnlocode;
		for (Location loc : locations) {
			if (loc.getUnLocode().equalsIgnoreCase(originUnlocode)) {
				this.originName = loc.getNameOnly();
			}
		}
	}

	public String getDestinationUnlocode() {
		return destinationUnlocode;
	}

	public void setDestinationUnlocode(String destinationUnlocode) {
		this.destinationUnlocode = destinationUnlocode;
		for (Location loc : locations) {
			if (loc.getUnLocode().equalsIgnoreCase(destinationUnlocode)) {
				destinationName = loc.getNameOnly();
			}
		}
	}

	public Date getToday() {
		return today;
	}

	public long getDuration() {
		return duration;
	}

	public void setArrivalDeadline(Date arrivalDeadline) {
		this.arrivalDeadline = arrivalDeadline;
	}

	public String getNewTrackingId() {
		return newTrackingId;
	}

	public boolean isBookable() {
		return bookable;
	}

	public String register() {
		try {
			if (!originUnlocode.equals(destinationUnlocode)) {
				bookingServiceFacade.bookNewCargo(originUnlocode, destinationUnlocode,
						// new SimpleDateFormat(FORMAT).parse(arrivalDeadline));
						// new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(arrivalDeadline));
						// // davidd
						arrivalDeadline);

			} else {
				// TODO See if this can be injected.
				FacesContext context = FacesContext.getCurrentInstance();
				// UI now prevents from selecting same origin/destination
				FacesMessage message = new FacesMessage("Origin and destination cannot be the same.");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				context.addMessage(null, message);
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException("Error parsing date", e); // todo, not parsing anymore
		}
		// return "show_original.xhtml?faces-redirect=true&trackingId=" + trackingId;
		return "/admin/dashboard.xhtml";
	}

	public String getReturnValue() {
		return "/admin/track";
	}

	public void deadlineUpdated() {
		duration = computeDuration(arrivalDeadline);
		if (duration >= MIN_JOURNEY_DURATION) {
			bookable = true;
		} else {
			bookable = false;
		}
		// PF.current().ajax().update("dateForm:durationPanel");
		// PF.current().ajax().update("dateForm:bookBtn");
		PrimeFaces.current().ajax().update("dateForm:durationPanel");
		PrimeFaces.current().ajax().update("dateForm:bookBtn");
		// RequestContext.getCurrentInstance().update("dateForm:durationPanel");
		// RequestContext.getCurrentInstance().update("dateForm:bookBtn");
	}

}
