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
import java.util.Date;
import jakarta.cargotracker.application.util.DateUtil;

/**
 * DTO for a leg in an itinerary.
 */
public class Leg implements Serializable {

    private static final SimpleDateFormat DATE_FORMAT
            = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");

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
            Date loadTime,
            Date unloadTime) {
        this.voyageNumber = voyageNumber;
        this.fromUnLocode = fromUnLocode;
        this.fromName = fromName;
        this.toUnLocode = toUnLocode;
        this.toName = toName;
        this.loadTime = DATE_FORMAT.format(loadTime);
        this.unloadTime = DATE_FORMAT.format(unloadTime);
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
        return "Leg{" + "voyageNumber=" + voyageNumber + ", from=" + fromUnLocode + ", to=" + toUnLocode + ", loadTime=" + loadTime + ", unloadTime=" + unloadTime + '}';
    }
}
