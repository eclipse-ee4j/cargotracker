package org.eclipse.cargotracker.interfaces.tracking.web;

import static org.eclipse.cargotracker.application.util.LocationUtil.getCoordinatesForLocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.Delivery;
import org.eclipse.cargotracker.domain.model.cargo.HandlingActivity;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 * View adapter for displaying a cargo in a tracking context.
 */
public class CargoTrackingViewAdapter {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");

	private final Cargo cargo;
	private final List<HandlingEventViewAdapter> events;

	public CargoTrackingViewAdapter(Cargo cargo, List<HandlingEvent> handlingEvents) {
		this.cargo = cargo;
		this.events = new ArrayList<>(handlingEvents.size());

		for (HandlingEvent handlingEvent : handlingEvents) {
			events.add(new HandlingEventViewAdapter(handlingEvent));
		}
	}

	public String getTrackingId() {
		return cargo.getTrackingId().getIdString();
	}

	public String getOrigin() {
		return getDisplayText(cargo.getOrigin());
	}

	public String getDestination() {
		return getDisplayText(cargo.getRouteSpecification().getDestination());
	}

	/**
	 * @return A formatted string for displaying the location.
	 */
	private String getDisplayText(Location location) {
		return location.getName();
	}

	/**
	 * @return A readable string describing the cargo status.
	 */
	public String getStatusText() {
		Delivery delivery = cargo.getDelivery();

		switch (delivery.getTransportStatus()) {
		case IN_PORT:
			return "In port " + getDisplayText(delivery.getLastKnownLocation());
		case ONBOARD_CARRIER:
			return "Onboard voyage " + delivery.getCurrentVoyage().getVoyageNumber().getIdString();
		case CLAIMED:
			return "Claimed";
		case NOT_RECEIVED:
			return "Not received";
		case UNKNOWN:
			return "Unknown";
		default:
			return "[Unknown status]"; // Should never happen.
		}
	}

	public boolean isMisdirected() {
		return cargo.getDelivery().isMisdirected();
	}

	public String getEta() {
		Date eta = cargo.getDelivery().getEstimatedTimeOfArrival();

		if (eta == null) {
			return "?";
		} else {
			return DATE_FORMAT.format(eta);
		}
	}

	public String getNextExpectedActivity() {
		HandlingActivity activity = cargo.getDelivery().getNextExpectedActivity();

		if ((activity == null) || (activity.isEmpty())) {
			return "";
		}

		String text = "Next expected activity is to ";
		HandlingEvent.Type type = activity.getType();

		if (type.sameValueAs(HandlingEvent.Type.LOAD)) {
			return text + type.name().toLowerCase() + " cargo onto voyage " + activity.getVoyage().getVoyageNumber()
					+ " in " + activity.getLocation().getName();
		} else if (type.sameValueAs(HandlingEvent.Type.UNLOAD)) {
			return text + type.name().toLowerCase() + " cargo off of " + activity.getVoyage().getVoyageNumber() + " in "
					+ activity.getLocation().getName();
		} else {
			return text + type.name().toLowerCase() + " cargo in " + activity.getLocation().getName();
		}
	}

	/**
	 * @return An unmodifiable list of handling event view adapters.
	 */
	public List<HandlingEventViewAdapter> getEvents() {
		return Collections.unmodifiableList(events);
	}

	/**
	 * @return The model for Google maps showing origin, destination and last known
	 *         location.
	 */
	public MapModel getMapModel() {
		MapModel mapModel = new DefaultMapModel();

		String origin = cargo.getOrigin().getUnLocode().getIdString();
		String destination = cargo.getRouteSpecification().getDestination().getUnLocode().getIdString();
		String lastKnownLocation = cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString();

		if (origin != null && !origin.isEmpty()) {
			mapModel.addOverlay(new Marker(getCoordinatesForLocation(origin), "Origin: " + getOrigin()));
		}

		if (destination != null && !destination.isEmpty()) {
			mapModel.addOverlay(
					new Marker(getCoordinatesForLocation(destination), "Final destination: " + getDestination()));
		}

		if (lastKnownLocation != null && !lastKnownLocation.isEmpty()
				&& !lastKnownLocation.toUpperCase().contains("XXXX")) {
			mapModel.addOverlay(new Marker(getCoordinatesForLocation(lastKnownLocation),
					"Last known location: " + getDisplayText(cargo.getDelivery().getLastKnownLocation())));
		}

		return mapModel;
	}

	public String getDestinationCoordinates() {
		LatLng coordinates = getCoordinatesForLocation(
				cargo.getRouteSpecification().getDestination().getUnLocode().getIdString());
		return "" + coordinates.getLat() + "," + coordinates.getLng();
	}

	/**
	 * Handling event view adapter component.
	 */
	public class HandlingEventViewAdapter {

		private final HandlingEvent handlingEvent;

		public HandlingEventViewAdapter(HandlingEvent handlingEvent) {
			this.handlingEvent = handlingEvent;
		}

		public String getLocation() {
			return handlingEvent.getLocation().getName();
		}

		/**
		 *
		 * @return the date in the format MM/dd/yyyy hh:mm a z
		 */
		public String getTime() {
			return DATE_FORMAT.format(handlingEvent.getCompletionTime());
		}

		public String getType() {
			return handlingEvent.getType().toString();
		}

		public String getVoyageNumber() {
			Voyage voyage = handlingEvent.getVoyage();
			return voyage.getVoyageNumber().getIdString();
		}

		public boolean isExpected() {
			return cargo.getItinerary().isExpected(handlingEvent);
		}

		public String getDescription() {
			switch (handlingEvent.getType()) {
			case LOAD:
				return "Loaded onto voyage " + handlingEvent.getVoyage().getVoyageNumber().getIdString() + " in "
						+ handlingEvent.getLocation().getName();
			case UNLOAD:
				return "Unloaded off voyage " + handlingEvent.getVoyage().getVoyageNumber().getIdString() + " in "
						+ handlingEvent.getLocation().getName();
			case RECEIVE:
				return "Received in " + handlingEvent.getLocation().getName();
			case CLAIM:
				return "Claimed in " + handlingEvent.getLocation().getName();
			case CUSTOMS:
				return "Cleared customs in " + handlingEvent.getLocation().getName();
			default:
				return "[Unknown]";
			}
		}
	}
}
