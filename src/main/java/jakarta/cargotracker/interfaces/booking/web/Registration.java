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
package jakarta.cargotracker.interfaces.booking.web;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import jakarta.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import jakarta.cargotracker.interfaces.booking.facade.dto.Location;

/**
 * Handles registering cargo. Operates against a dedicated service facade, and
 * could easily be rewritten as a thick Swing client. Completely separated from
 * the domain layer, unlike the tracking user interface.
 * <p/>
 * In order to successfully keep the domain model shielded from user interface
 * considerations, this approach is generally preferred to the one taken in the
 * tracking controller. However, there is never any one perfect solution for all
 * situations, so we've chosen to demonstrate two polarized ways to build user
 * interfaces.
 */
@Named
@ViewScoped
public class Registration implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String FORMAT = "yyyy-MM-dd";
    List<Location> locations;
    private String arrivalDeadline;
    private String originUnlocode;
    private String destinationUnlocode;
    @Inject
    private BookingServiceFacade bookingServiceFacade;

    public List<Location> getLocations() {
        return locations;
    }

    public String getArrivalDeadline() {
        return arrivalDeadline;
    }

    public void setArrivalDeadline(String arrivalDeadline) {
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

    @PostConstruct
    public void init() {
        locations = bookingServiceFacade.listShippingLocations();
    }

    public String register() {
        String trackingId = null;

        try {
            if (!originUnlocode.equals(destinationUnlocode)) {
                trackingId = bookingServiceFacade.bookNewCargo(
                        originUnlocode,
                        destinationUnlocode,
                        new SimpleDateFormat(FORMAT).parse(arrivalDeadline));
            } else {
                // TODO See if this can be injected.
                FacesContext context = FacesContext.getCurrentInstance();
                FacesMessage message = new FacesMessage(
                        "Origin and destination cannot be the same.");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                context.addMessage(null, message);
                return null;
            }
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date", e);
        }

        return "show.xhtml?faces-redirect=true&trackingId=" + trackingId;
    }
}
