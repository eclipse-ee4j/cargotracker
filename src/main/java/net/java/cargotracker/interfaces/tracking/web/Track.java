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
package net.java.cargotracker.interfaces.tracking.web;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import net.java.cargotracker.domain.model.cargo.Cargo;
import net.java.cargotracker.domain.model.cargo.CargoRepository;
import net.java.cargotracker.domain.model.cargo.TrackingId;
import net.java.cargotracker.domain.model.handling.HandlingEvent;
import net.java.cargotracker.domain.model.handling.HandlingEventRepository;

/**
 * Backing bean for tracking cargo. This interface sits immediately on top of
 * the domain layer, unlike the booking interface which has a facade and
 * supporting DTOs in between.
 * <p/>
 * An adapter class, designed for the tracking use case, is used to wrap the
 * domain model to make it easier to work with in a web page rendering context.
 * We do not want to apply view rendering constraints to the design of our
 * domain model and the adapter helps us shield the domain model classes where
 * needed.
 * <p/>
 * In some very simplistic cases, it is fine to not use even an adapter.
 */
@Named
@ViewScoped
public class Track implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CargoRepository cargoRepository;
    @Inject
    private HandlingEventRepository handlingEventRepository;

    private String trackingId;
    private CargoTrackingViewAdapter cargo;

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        // TODO See if a more global trimming mechanism is needed.
        if (trackingId != null) {
            trackingId = trackingId.trim();
        }

        this.trackingId = trackingId;
    }

    public CargoTrackingViewAdapter getCargo() {
        return cargo;
    }

    public void setCargo(CargoTrackingViewAdapter cargo) {
        this.cargo = cargo;
    }

    /**
     * @param query The query parameter is required by PrimeFaces but we don't 
     * use it.
     */
    public List<TrackingId> getTrackingIds(String query) {
        return cargoRepository.getAllTrackingIds();
    }

    public void onTrackById() {
        Cargo cargo = cargoRepository.find(new TrackingId(trackingId));

        if (cargo != null) {
            List<HandlingEvent> handlingEvents = handlingEventRepository
                    .lookupHandlingHistoryOfCargo(new TrackingId(trackingId))
                    .getDistinctEventsByCompletionTime();
            this.cargo = new CargoTrackingViewAdapter(cargo, handlingEvents);
        } else {
            // TODO See if this can be injected.
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(
                    "Cargo with tracking ID: " + trackingId + " not found.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(null, message);
            this.cargo = null;
        }
    }
}
