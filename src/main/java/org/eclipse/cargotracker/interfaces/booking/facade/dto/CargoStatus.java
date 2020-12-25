package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CargoStatus {

    private final String origin;
    private final String trackingId;
    private final String destination;
    private final List<TrackingEvents> events;
    private final String statusText;
    private final boolean misdirected;
    private final String eta;
    private final String nextExpectedActivity;

    public CargoStatus(
            String origin, String trackingId, String destination,
            String statusText, boolean misdirected, String eta,
            String nextExpectedActivity,
            List<TrackingEvents> handlingEvents
    ) {
        this.origin = origin;
        this.trackingId = trackingId;
        this.destination = destination;
        this.statusText = statusText;
        this.misdirected = misdirected;
        this.eta = eta;
        this.nextExpectedActivity = nextExpectedActivity;
        this.events = new ArrayList<>(handlingEvents.size());

        for (TrackingEvents handlingEvent : handlingEvents) {
            events.add(handlingEvent);
        }
    }

    public String getTrackingId() {
        return trackingId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    /**
     * @return A readable string describing the cargo status.
     */
    public String getStatusText() {
        return statusText;
    }

    public boolean isMisdirected() {
        return misdirected;
    }

    public String getEta() {
        return eta;
    }

    public String getNextExpectedActivity() {
        return nextExpectedActivity;
    }

    /**
     * @return An unmodifiable list of handling event view adapters.
     */
    public List<TrackingEvents> getEvents() {
        return Collections.unmodifiableList(events);
    }

}
