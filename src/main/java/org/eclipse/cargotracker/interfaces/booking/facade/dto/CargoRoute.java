package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import org.eclipse.cargotracker.application.util.DateUtil;
import org.eclipse.cargotracker.application.util.LocationUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO for registering and routing a cargo.
 */
public class CargoRoute implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMAT
            = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a z");

    private final String trackingId;
    private final String origin;
    private final String finalDestination;
    private final String arrivalDeadline;
    private final boolean misrouted;
    private final List<Leg> legs;
    private final boolean claimed;
    private final String lastKnownLocation;
    private final String transportStatus;
    private String nextLocation;

    public CargoRoute(String trackingId, String origin, String finalDestination,
                      LocalDateTime arrivalDeadline, boolean misrouted, boolean claimed, String lastKnownLocation, String transportStatus) {
        this.trackingId = trackingId;
        this.origin = origin;
        this.finalDestination = finalDestination;
        OffsetDateTime odt = OffsetDateTime.now();
        ZonedDateTime localArrivalDeadline = ZonedDateTime.of(arrivalDeadline, odt.getOffset());
        this.arrivalDeadline = localArrivalDeadline.format(DATE_FORMAT);
        this.misrouted = misrouted;
        this.claimed = claimed;
        this.lastKnownLocation = lastKnownLocation;
        this.transportStatus = transportStatus;
        this.legs = new ArrayList<>();
    }

    public String getTrackingId() {
        return trackingId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getOriginName() {
    	// TODO See if this can be done in a more DDD friendly way.
        return LocationUtil.getLocationName(origin);
    }

    public String getOriginCode() {
    	// TODO See if this can be done in a more DDD friendly way.
        return LocationUtil.getLocationCode(origin);
    }

    public String getFinalDestination() {
        return finalDestination;
    }

    public String getFinalDestinationName() {
    	// TODO See if this can be done in a more DDD friendly way.
        return LocationUtil.getLocationName(finalDestination);
    }

    public String getFinalDestinationCode() {
    	// TODO See if this can be done in a more DDD friendly way.
        return LocationUtil.getLocationCode(finalDestination);
    }

	// TODO See if this can be done in a more DDD friendly way.    
    public void addLeg(
            String voyageNumber,
            String fromUnLocode, String fromName,
            String toUnLocode, String toName,
            LocalDateTime loadTime, LocalDateTime unloadTime) {
        legs.add(new Leg(voyageNumber,
                fromUnLocode, fromName,
                toUnLocode, toName,
                loadTime, unloadTime));
    }

    public List<Leg> getLegs() {
        return Collections.unmodifiableList(legs);
    }

    public boolean isMisrouted() {
        return misrouted;
    }

    public boolean isRouted() {
        return !legs.isEmpty();
    }

    public String getArrivalDeadline() {
        return arrivalDeadline;
    }

    public String getArrivalDeadlineDate() {
        return DateUtil.getDateFromDateTime(arrivalDeadline);
    }

    public String getArrivalDeadlineTime() {
        return DateUtil.getTimeFromDateTime(arrivalDeadline);
    }

    public boolean isClaimed() {
        return claimed;
    }

    public String getLastKnownLocation() {
        return this.lastKnownLocation;
    }

    public String getLastKnownLocationName() {
        return LocationUtil.getLocationName(lastKnownLocation);
    }

    public String getLastKnownLocationCode() {
        return LocationUtil.getLocationCode(lastKnownLocation);
    }

    public String getTransportStatus() {
    	// TODO This needs to be a richer status, with a more readable description.
        return this.transportStatus;
    }

    public String getNextLocation() {
        return this.nextLocation;
    }
}
