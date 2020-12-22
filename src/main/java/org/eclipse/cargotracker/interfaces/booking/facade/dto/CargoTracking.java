package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.Delivery;
import org.eclipse.cargotracker.domain.model.cargo.HandlingActivity;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.location.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CargoTracking {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");

    private final Cargo cargo;
    private final List<HandlingTrackingEvents> events;

    public CargoTracking(Cargo cargo, List<HandlingTrackingEvents> handlingEvents) {
        this.cargo = cargo;
        this.events = new ArrayList<>(handlingEvents.size());

        for (HandlingTrackingEvents handlingEvent : handlingEvents) {
            events.add(handlingEvent);
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
    public List<HandlingTrackingEvents> getEvents() {
        return Collections.unmodifiableList(events);
    }

}
