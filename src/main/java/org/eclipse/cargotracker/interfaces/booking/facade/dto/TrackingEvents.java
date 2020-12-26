package org.eclipse.cargotracker.interfaces.booking.facade.dto;

public class TrackingEvents {

    private final String location;
    private final String time;
    private final String type;
    private final String voyageNumber;
    private final boolean expected;
    private final String description;

    public TrackingEvents(String location, String time, String type, String voyageNumber, boolean expected, String description) {
        this.location = location;
        this.time = time;
        this.type = type;
        this.voyageNumber = voyageNumber;
        this.expected = expected;
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public boolean isExpected() {
        return expected;
    }

    public String getDescription() {
        return description;
    }
}
