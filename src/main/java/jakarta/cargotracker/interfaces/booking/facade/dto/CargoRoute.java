/*
    The MIT License
    
    Copyright (c) 2019 Oracle and/or its affiliates
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/
package jakarta.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import jakarta.cargotracker.application.util.DateUtil;
import jakarta.cargotracker.application.util.LocationUtil;

/**
 * DTO for registering and routing a cargo.
 */
public class CargoRoute implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat DATE_FORMAT
            = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");

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
            Date arrivalDeadline, boolean misrouted, boolean claimed, String lastKnownLocation, String transportStatus) {
        this.trackingId = trackingId;
        this.origin = origin;
        this.finalDestination = finalDestination;
        this.arrivalDeadline = DATE_FORMAT.format(arrivalDeadline);
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
        return LocationUtil.getLocationName(origin);
    }

    public String getOriginCode() {
        return LocationUtil.getLocationCode(origin);
    }

    public String getFinalDestination() {
        return finalDestination;
    }
    
    public String getFinalDestinationName() {
        return LocationUtil.getLocationName(finalDestination);
    }
    
    public String getFinalDestinationCode() {
        return LocationUtil.getLocationCode(finalDestination);
    }

    public void addLeg(
            String voyageNumber,
            String fromUnLocode, String fromName,
            String toUnLocode, String toName,
            Date loadTime, Date unloadTime) {
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
        return this.transportStatus;
    }

    public String getNextLocation() {
        return this.nextLocation;
    }
}
