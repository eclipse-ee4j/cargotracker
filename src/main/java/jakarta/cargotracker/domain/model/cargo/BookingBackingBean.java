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
package jakarta.cargotracker.domain.model.cargo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import static jakarta.cargotracker.application.util.DateUtil.computeDuration;
import jakarta.cargotracker.interfaces.booking.facade.dto.Location;
import jakarta.cargotracker.interfaces.booking.facade.BookingServiceFacade;
//import org.primefaces.PF;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
/**
 *
 * @author davidd
 */
@Named
@FlowScoped("booking")
public class BookingBackingBean implements Serializable{

    private static final long serialVersionUID = 1L;
    private static final String FORMAT = "yyyy-MM-dd";
    List<Location> locations;
    private Date arrivalDeadline;
    private String originUnlocode;
    private String originName;
    private String destinationName;
    private String destinationUnlocode;
    private String newTrackingId = null;
    private Date today = new Date();
    private boolean bookable = false;
    private long duration = -1;
    private final long MIN_JOURNEY_DURATION = 1; // journey should be 1 day min.

    @Inject
    private BookingServiceFacade bookingServiceFacade;

    @PostConstruct
    public void init() {
        locations = bookingServiceFacade.listShippingLocations();
    }

    @SuppressWarnings("SuspiciousIndentAfterControlStatement")
    public List<Location> getLocations() {

        List<Location> filteredLocations = new ArrayList<>();
        String locationToRemove = null;

        // TODO: there should be a better way to do tihs
        if (FacesContext.getCurrentInstance().getViewRoot().getViewId().endsWith("destination.xhtml")) {
            // in Destination menu, Orign can't be selected
            locationToRemove = originUnlocode;
        } else // and vice-versa
        if (destinationUnlocode != null) {
            locationToRemove = destinationUnlocode;
        }

        for (Location loc : locations) {
            if (!loc.getUnLocode().equalsIgnoreCase(locationToRemove)) {
                filteredLocations.add(loc);
            }
        }
        return filteredLocations;
    }

    public Date getArrivalDeadline() {
        return arrivalDeadline;
    }

    public String getOriginUnlocode() {
        return originUnlocode;
    }

    public String getOriginName() {
        return originName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setOriginUnlocode(String originUnlocode) {
        this.originUnlocode = originUnlocode;
        for (Location loc : locations) {
            if (loc.getUnLocode().equalsIgnoreCase(originUnlocode)) {
                this.originName = loc.getNameOnly();
            }
        }
    }

    public String getDestinationUnlocode() {
        return destinationUnlocode;
    }

    public void setDestinationUnlocode(String destinationUnlocode) {
        this.destinationUnlocode = destinationUnlocode;
        for (Location loc : locations) {
            if (loc.getUnLocode().equalsIgnoreCase(destinationUnlocode)) {
                destinationName = loc.getNameOnly();
            }
        }
    }

    public Date getToday() {
        return today;
    }

    public long getDuration() {
        return duration;
    }

    public void setArrivalDeadline(Date arrivalDeadline) {
        this.arrivalDeadline = arrivalDeadline;
    }

    public String getNewTrackingId() {
        return newTrackingId;
    }

    public boolean isBookable() {
        return bookable;
    }

    public String register() {

        String trackingId = null;
        try {
            if (!originUnlocode.equals(destinationUnlocode)) {
                trackingId = bookingServiceFacade.bookNewCargo(
                        originUnlocode,
                        destinationUnlocode,
                        //new SimpleDateFormat(FORMAT).parse(arrivalDeadline));
                        //new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(arrivalDeadline)); // davidd
                        arrivalDeadline);

            } else {
                // TODO See if this can be injected.
                FacesContext context = FacesContext.getCurrentInstance();
                // UI now prevents from selecting same origin/destination
                FacesMessage message = new FacesMessage("Origin and destination cannot be the same.");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage(null, message);
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error parsing date", e); // todo, not parsing anymore
        }
        //return "show_original.xhtml?faces-redirect=true&trackingId=" + trackingId;
        return "/admin/dashboard.xhtml";
    }

    public String getReturnValue() {
        return "/admin/track";
    }

    public void deadlineUpdated() {
        duration = computeDuration(arrivalDeadline);
        if (duration >= MIN_JOURNEY_DURATION) {
            bookable = true;
        } else {
            bookable = false;
        }
        //PF.current().ajax().update("dateForm:durationPanel");
        //PF.current().ajax().update("dateForm:bookBtn");
        PrimeFaces.current().ajax().update("dateForm:durationPanel");
        PrimeFaces.current().ajax().update("dateForm:bookBtn");
        //RequestContext.getCurrentInstance().update("dateForm:durationPanel");
        //RequestContext.getCurrentInstance().update("dateForm:bookBtn");
    }
    
}
