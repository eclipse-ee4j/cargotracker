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
package net.java.cargotracker.interfaces.booking.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import net.java.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import net.java.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import net.java.cargotracker.interfaces.booking.facade.dto.Location;
//import org.primefaces.PF;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;

/**
 * Handles changing the cargo destination. Operates against a dedicated service
 * facade, and could easily be rewritten as a thick Swing client. Completely
 * separated from the domain layer, unlike the tracking user interface.
 * <p/>
 * In order to successfully keep the domain model shielded from user interface
 * considerations, this approach is generally preferred to the one taken in the
 * tracking controller. However, there is never any one perfect solution for all
 * situations, so we've chosen to demonstrate two polarized ways to build user
 * interfaces.
 *
 * @see net.java.cargotracker.interfaces.tracking.CargoTrackingController
 */
@Named
@ViewScoped
public class ChangeDestination implements Serializable {

    private static final long serialVersionUID = 1L;
    private String trackingId;
    private CargoRoute cargo;
    private List<Location> locations;
    private String destinationUnlocode;
    @Inject
    private BookingServiceFacade bookingServiceFacade;

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public CargoRoute getCargo() {
        return cargo;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<Location> getPotentialDestinations() {
        // Potential destination = All Locations - Origin - Current Destination
        List<Location> destinationsToRemove = new ArrayList<Location>();
        for (Location loc : locations) {
            if (loc.getName().equalsIgnoreCase(cargo.getOrigin()) || loc.getName().equalsIgnoreCase(cargo.getFinalDestination())) {
                destinationsToRemove.add(loc);
            }
        }
        locations.removeAll(destinationsToRemove);
        return locations;
    }

    public String getDestinationUnlocode() {
        return destinationUnlocode;
    }

    public void setDestinationUnlocode(String destinationUnlocode) {
        this.destinationUnlocode = destinationUnlocode;
    }

    public void load() {
        locations = bookingServiceFacade.listShippingLocations();
        cargo = bookingServiceFacade.loadCargoForRouting(trackingId);
    }

    public void changeDestination() {
        bookingServiceFacade.changeDestination(trackingId, destinationUnlocode);
        //PF.current().dialog().closeDynamic("DONE");
        PrimeFaces.current().dialog().closeDynamic("DONE");
        //RequestContext.getCurrentInstance().closeDialog("DONE");
    }
}
