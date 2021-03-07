package org.eclipse.cargotracker.interfaces.booking.sse;

import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.infrastructure.events.cdi.CargoInspected;

/** Sever-sent events service for tracking all cargoes in real time. */
@ApplicationScoped
@Path("/tracking")
public class RealtimeCargoTrackingService {

  @Inject private Logger logger;

  @Context private Sse sse;
  private SseBroadcaster broadcaster;

  @PostConstruct
  public void init() {
    broadcaster = sse.newBroadcaster();
    logger.log(Level.INFO, "Sse broadcaster created");
  }

  @GET
  @Produces(MediaType.SERVER_SENT_EVENTS)
  public void tracking(@Context SseEventSink eventSink) {
    // sending some events is required to tell the client that Sse connection is open.
    eventSink.send(sse.newEventBuilder().comment("EventSource open").build());
    broadcaster.register(eventSink);
    logger.log(Level.INFO, "Sse event sink registered");
  }

  @PreDestroy
  public void terminate() {
    broadcaster.close();
    logger.log(Level.INFO, "Sse broadcaster closed");
  }

  public void onCargoInspected(@ObservesAsync @CargoInspected Cargo cargo) {
    Writer writer = new StringWriter();

    try (JsonGenerator generator = Json.createGenerator(writer)) {
      generator
          .writeStartObject()
          .write("trackingId", cargo.getTrackingId().getIdString())
          .write("origin", cargo.getOrigin().getName())
          .write("destination", cargo.getRouteSpecification().getDestination().getName())
          .write("lastKnownLocation", cargo.getDelivery().getLastKnownLocation().getName())
          .write("transportStatus", cargo.getDelivery().getTransportStatus().toString())
          .writeEnd();
    }

    broadcaster.broadcast(sse.newEvent(writer.toString()));
  }
}
