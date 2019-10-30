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
package jakarta.cargotracker.interfaces.handling.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import jakarta.cargotracker.application.ApplicationEvents;
import jakarta.cargotracker.domain.model.cargo.TrackingId;
import jakarta.cargotracker.domain.model.handling.HandlingEvent;
import jakarta.cargotracker.domain.model.location.UnLocode;
import jakarta.cargotracker.domain.model.voyage.VoyageNumber;
import jakarta.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;


/**
 * This REST endpoint implementation performs basic validation and parsing of
 * incoming data, and in case of a valid registration attempt, sends an
 * asynchronous message with the information to the handling event registration
 * system for proper registration.
 */
@Stateless // TODO Make this a stateless bean for better scalability.
@Path("/handling")
public class HandlingReportService {

    public static final String ISO_8601_FORMAT = "yyyy-MM-dd HH:mm";
    @Inject
    private ApplicationEvents applicationEvents;

    public HandlingReportService() {}
    
    @POST
    @Path("/reports")
    @Consumes(MediaType.APPLICATION_JSON)
    // TODO Better exception handling.
    public void submitReport(@NotNull @Valid HandlingReport handlingReport) {
        try {
            Date completionTime = new SimpleDateFormat(ISO_8601_FORMAT).parse(
                    handlingReport.getCompletionTime());
            VoyageNumber voyageNumber = null;

            if (handlingReport.getVoyageNumber() != null) {
                voyageNumber = new VoyageNumber(
                        handlingReport.getVoyageNumber());
            }
            
            HandlingEvent.Type type = HandlingEvent.Type.valueOf(
                    handlingReport.getEventType());
            UnLocode unLocode = new UnLocode(handlingReport.getUnLocode());

            TrackingId trackingId = new TrackingId(handlingReport.getTrackingId());

            Date registrationTime = new Date();
            HandlingEventRegistrationAttempt attempt =
                    new HandlingEventRegistrationAttempt(registrationTime,
                    completionTime, trackingId, voyageNumber, type, unLocode);

            applicationEvents.receivedHandlingEventRegistrationAttempt(attempt);
        } catch (ParseException ex) {
            throw new RuntimeException("Error parsing completion time", ex);
        }
    }
}