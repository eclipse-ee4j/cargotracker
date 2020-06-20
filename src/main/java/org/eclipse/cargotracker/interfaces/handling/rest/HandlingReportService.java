package org.eclipse.cargotracker.interfaces.handling.rest;

import org.eclipse.cargotracker.application.ApplicationEvents;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.eclipse.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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

    public HandlingReportService() {
    }

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
