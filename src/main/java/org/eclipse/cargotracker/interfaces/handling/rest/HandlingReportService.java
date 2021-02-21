package org.eclipse.cargotracker.interfaces.handling.rest;

import java.time.LocalDateTime;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.eclipse.cargotracker.application.ApplicationEvents;
import org.eclipse.cargotracker.application.util.DateUtil;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.eclipse.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;

/**
 * This REST end-point implementation performs basic validation and parsing of incoming data, and in
 * case of a valid registration attempt, sends an asynchronous message with the information to the
 * handling event registration system for proper registration.
 */
@Stateless
@Path("/handling")
public class HandlingReportService {

  @Inject private ApplicationEvents applicationEvents;

  @POST
  @Path("/reports")
  @Consumes({"application/json", "application/xml"})
  public void submitReport(
      @NotNull(message = "Missing handling report.") @Valid HandlingReport handlingReport) {
    LocalDateTime completionTime = DateUtil.toDateTime(handlingReport.getCompletionTime());
    VoyageNumber voyageNumber = null;

    if (handlingReport.getVoyageNumber() != null) {
      voyageNumber = new VoyageNumber(handlingReport.getVoyageNumber());
    }

    HandlingEvent.Type type = HandlingEvent.Type.valueOf(handlingReport.getEventType());
    UnLocode unLocode = new UnLocode(handlingReport.getUnLocode());

    TrackingId trackingId = new TrackingId(handlingReport.getTrackingId());

    HandlingEventRegistrationAttempt attempt =
        new HandlingEventRegistrationAttempt(
            LocalDateTime.now(), completionTime, trackingId, voyageNumber, type, unLocode);

    applicationEvents.receivedHandlingEventRegistrationAttempt(attempt);
  }
}
