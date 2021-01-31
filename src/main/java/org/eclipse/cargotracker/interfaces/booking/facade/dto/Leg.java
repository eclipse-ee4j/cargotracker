package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.eclipse.cargotracker.application.util.DateUtil;

/** DTO for a leg in an itinerary. */
public class Leg implements Serializable {

    public static final String DT_PATTERN = "MM/dd/yyyy hh:mm a z";
    private static final long serialVersionUID = 1L;
    // private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DT_PATTERN);
    private final String voyageNumber;
    private final String fromUnLocode;
    private final String fromName;
    private final String toUnLocode;
    private final String toName;
    private final String loadTime;
    private final String unloadTime;

    public Leg(
            String voyageNumber,
            String fromUnLocode,
            String fromName,
            String toUnLocode,
            String toName,
            LocalDateTime loadTime,
            LocalDateTime unloadTime) {
        this.voyageNumber = voyageNumber;
        this.fromUnLocode = fromUnLocode;
        this.fromName = fromName;
        this.toUnLocode = toUnLocode;
        this.toName = toName;
        this.loadTime = loadTime.format(DateTimeFormatter.ofPattern(DT_PATTERN));
        this.unloadTime = unloadTime.format(DateTimeFormatter.ofPattern(DT_PATTERN));
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public String getFrom() {
        return fromName + " (" + fromUnLocode + ")";
    }

    public String getFromUnLocode() {
        return fromUnLocode;
    }

    public String getFromName() {
        return fromName;
    }

    public String getTo() {
        return toUnLocode + " (" + toName + ")";
    }

    public String getToName() {
        return toName;
    }

    public String getToUnLocode() {
        return toUnLocode;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public String getLoadTimeDate() {
        return DateUtil.getDateFromDateTime(loadTime);
    }

    public String getLoadTimeTime() {
        return DateUtil.getTimeFromDateTime(loadTime);
    }

    public String getUnloadTime() {
        return unloadTime;
    }

    public String getUnloadTimeTime() {
        return DateUtil.getTimeFromDateTime(unloadTime);
    }

    public String getUnloadTimeDate() {
        return DateUtil.getDateFromDateTime(unloadTime);
    }

    @Override
    public String toString() {
        return "Leg{"
                + "voyageNumber="
                + voyageNumber
                + ", from="
                + fromUnLocode
                + ", to="
                + toUnLocode
                + ", loadTime="
                + loadTime
                + ", unloadTime="
                + unloadTime
                + '}';
    }
}
