package org.eclipse.cargotracker.interfaces.booking.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.Leg;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.Location;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.RouteCandidate;

/**
 * Handles cargo booking and routing. Operates against a dedicated service
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
@RequestScoped
public class CargoAdmin {

	private List<Location> locations;
	private List<String> unlocodes;
	private List<CargoRoute> cargos;
	private Date arrivalDeadline;
	private String originUnlocode;
	private String destinationUnlocode;
	private String trackingId;
	private List<Leg> legs;
	@Inject
	private BookingServiceFacade bookingServiceFacade;

	public List<Location> getLocations() {
		return locations;
	}

	public List<String> getUnlocodes() {
		return unlocodes;
	}

	public List<CargoRoute> getCargos() {
		return cargos;
	}

	public Date getArrivalDeadline() {
		return arrivalDeadline;
	}

	public void setArrivalDeadline(Date arrivalDeadline) {
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

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public List<Leg> getLegs() {
		return legs;
	}

	public void setLegs(List<Leg> legs) {
		this.legs = legs;
	}

	@PostConstruct
	public void init() {
		locations = bookingServiceFacade.listShippingLocations();
		unlocodes = new ArrayList<>();

		for (Location location : locations) {
			unlocodes.add(location.getUnLocode());
		}

		cargos = bookingServiceFacade.listAllCargos();
	}

	public String register() {
		String trackingId = bookingServiceFacade.bookNewCargo(originUnlocode, destinationUnlocode, arrivalDeadline);
		return "show.html?trackingId=" + trackingId;
	}

	public CargoRoute getCargo() {
		return bookingServiceFacade.loadCargoForRouting(trackingId);
	}

	public List<RouteCandidate> getRouteCanditates() {
		return bookingServiceFacade.requestPossibleRoutesForCargo(trackingId);
	}

	public String assignItinerary() {
		RouteCandidate selectedRoute = new RouteCandidate(legs);
		bookingServiceFacade.assignCargoToRoute(trackingId, selectedRoute);

		return "show.html?trackingId=" + trackingId;
	}

	public String changeDestination() {
		bookingServiceFacade.changeDestination(trackingId, destinationUnlocode);

		return "show.html?trackingId=" + trackingId;
	}
}
