package org.eclipse.cargotracker.interfaces.booking.sse;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.CargoRepository;
import org.eclipse.cargotracker.infrastructure.events.cdi.CargoUpdated;

/** Sever-sent events service for tracking all cargo in real time. */
@Singleton
@Path("/cargo")
public class RealtimeCargoTrackingService {
  @Inject private Logger logger;

  @Inject private CargoRepository cargoRepository;

  @Context private Sse sse;

  private SseBroadcaster broadcaster;

  @GET
  @Produces(MediaType.SERVER_SENT_EVENTS)
  public void tracking(@Context SseEventSink eventSink) {
    synchronized (RealtimeCargoTrackingService.class) {
      try {
        String name = "java:app/jms/CargoHandledQueue";
        InitialContext ctx = new InitialContext();
        Object obj = InitialContext.doLookup(name);
      } catch (NamingException e) {
        e.printStackTrace();
      }
      if (broadcaster == null) {
        broadcaster = sse.newBroadcaster();
      }
    }
    cargoRepository.findAll().stream().map(this::cargoToSseEvent).forEach(eventSink::send);
    broadcaster.register(eventSink);
    logger.log(Level.FINEST, "SSE event sink registered.");
  }

  @PreDestroy
  public void close() {
    if (broadcaster != null) {
      broadcaster.close();
      logger.log(Level.FINEST, "SSE broadcaster closed.");
    }
  }

  public void onCargoUpdated(@ObservesAsync @CargoUpdated Cargo cargo) {
    if (broadcaster != null && (sse.newEventBuilder() != null)) {
      logger.log(Level.FINEST, "SSE event broadcast for cargo: {0}", cargo);
      broadcaster.broadcast(cargoToSseEvent(cargo));
    }
  }

  private OutboundSseEvent cargoToSseEvent(Cargo cargo) {
    return sse.newEventBuilder()
        .mediaType(MediaType.APPLICATION_JSON_TYPE)
        .data(new RealtimeCargoTrackingViewAdapter(cargo))
        .build();
  }
}
