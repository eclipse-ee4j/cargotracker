package org.eclipse.cargotracker.interfaces.handling.mobile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.cargotracker.application.ApplicationEvents;
import org.eclipse.cargotracker.application.util.LocationUtil;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.eclipse.cargotracker.domain.model.voyage.VoyageRepository;
import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.eclipse.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;
import org.primefaces.event.FlowEvent;

@Named
@ViewScoped
// TODO Rename this to something more meaningful to the domain.
public class EventWizard implements Serializable {

	private static final long serialVersionUID = 1L;

	// TODO This facade is actually intended for use with the booking interface. It
	// will be hard to maintain if it is dual purpose. Since this is a fairly
	// limited interface, we should probably use the view adapter pattern here
	// similar to what is done for the tracking interface.
	@Inject
	private BookingServiceFacade bookingServiceFacade;

	@Inject
	private ApplicationEvents applicationEvents;

	@Inject
	private VoyageRepository voyageRepository;

	private List<SelectItem> trackIds;
	private List<SelectItem> locations;
	private List<SelectItem> voyages;

	/**
	 * wizard's data
	 */
	private String voyageNumber;
	private Date completionDate;
	private String eventType;
	private String location;
	private String trackId;

	@PostConstruct
	public void init() {
		List<CargoRoute> cargos = bookingServiceFacade.listAllCargos();

		// fill the TrackingId dropdown list
		trackIds = new ArrayList<>();
		for (CargoRoute route : cargos) {
			if (route.isRouted() && !route.isClaimed()) { // we just need getRoutedUnclaimedCargos
				String routedUnclaimedId = route.getTrackingId();
				trackIds.add(new SelectItem(routedUnclaimedId, routedUnclaimedId));
			}
		}

		// fill the Port dropdown list
		locations = new ArrayList<>();
		List<Location> allLocations = LocationUtil.getLocationsCode();
		for (Location tempLoc : allLocations) {
			String code = tempLoc.getUnLocode().getIdString();
			locations.add(new SelectItem(code, tempLoc.getName() + " (" + code + ")"));
		}

		// fill the Voyage dropdown list (only needed for LOAD & UNLOAD events)
		List<Voyage> allVoyages = voyageRepository.findAll();
		List<SelectItem> allVoyagesModel = new ArrayList<>(allVoyages.size());

		for (Voyage voyage : allVoyages) {
			allVoyagesModel.add(
					new SelectItem(voyage.getVoyageNumber().getIdString(), voyage.getVoyageNumber().getIdString()));
		}

		this.voyages = allVoyagesModel;
	}

	public String onFlowProcess(FlowEvent event) {
		// here we can customize the flow of the wizard
		if (!validate(event.getOldStep())) {
			return event.getOldStep();
		}

		if ("dateTab".equals(event.getNewStep())) {
			completionDate = Calendar.getInstance().getTime();
		}

		return event.getNewStep();
	}

	private boolean validate(final String step) {
		CargoRoute cargoRoute = bookingServiceFacade.loadCargoForRouting(trackId);

		if ("eventTab".equals(step) && "RECEIVE".equals(eventType) && !isOriginLocation(cargoRoute)) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"A cargo can be RECEIVEd only in his origin port, please fix the errors to continue.", "");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return false;
		}

		if ("eventTab".equals(step) && "CLAIM".equals(eventType) && !isDestination(cargoRoute)) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"A cargo can be CLAIMed only in his destination port, please fix errors to continue.", "");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return false;
		}

		if ("voyageTab".equals(step) && ("LOAD".equals(eventType) || "UNLOAD".equals(eventType))
				&& voyageNumber == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"When a cargo is LOADed or UNLOADed a Voyage should be selected, please fix errors to continue.",
					"");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return false;
		}

		return true;
	}

	public boolean isOriginLocation(CargoRoute cargoRoute) {
		if (cargoRoute == null || location == null) {
			return true;
		}

		return cargoRoute.getOriginCode().equals(location);
	}

	public boolean isDestination(CargoRoute cargoRoute) {
		if (cargoRoute == null || location == null) {
			return true;
		}

		return cargoRoute.getFinalDestinationCode().equals(location);
	}

	public void save() {
		VoyageNumber selectedVoyage;

		Date registrationTime = new Date();
		TrackingId trackingId = new TrackingId(trackId);
		UnLocode unLocode = new UnLocode(this.location);
		HandlingEvent.Type type = HandlingEvent.Type.valueOf(eventType);

		// Only Load & Unload could have a Voyage set
		if ("LOAD".equals(eventType) || "UNLOAD".equals(eventType)) {
			selectedVoyage = new VoyageNumber(voyageNumber);
		} else {
			selectedVoyage = null;
		}

		HandlingEventRegistrationAttempt attempt = new HandlingEventRegistrationAttempt(registrationTime,
				completionDate, trackingId, selectedVoyage, type, unLocode);

		applicationEvents.receivedHandlingEventRegistrationAttempt(attempt);

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Event submitted", ""));
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public void setVoyageNumber(String voyageNumber) {
		this.voyageNumber = voyageNumber;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public String getTrackId() {
		return trackId;
	}

	public List<SelectItem> getTrackIds() {
		return trackIds;
	}

	public String getVoyageNumber() {
		return voyageNumber;
	}

	public List<SelectItem> getVoyages() {
		return voyages;
	}

	public String getLocation() {
		return location;
	}

	public List<SelectItem> getLocations() {
		return locations;
	}

	public String getEventType() {
		return eventType;
	}

	public Date getCompletionDate() {
		return completionDate;
	}
}
