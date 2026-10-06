package org.eclipse.cargotracker.interfaces.booking.sse;

import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseBroadcaster;
import jakarta.ws.rs.sse.SseEventSink;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.CargoRepository;
import org.eclipse.cargotracker.infrastructure.events.cdi.CargoUpdated;

/** Sever-sent events service for tracking all cargo in real time. */
@Singleton
@Path("/cargo")
public class RealtimeCargoTrackingService {
  @Inject private Logger logger;

  @Inject private CargoRepository cargoRepository;

  private Sse sse;
  private SseBroadcaster broadcaster;

  @GET
  @Produces(MediaType.SERVER_SENT_EVENTS)
  public void tracking(@Context SseEventSink eventSink, @Context Sse sse) {
    if (broadcaster == null) {
      this.sse = sse;
      broadcaster = sse.newBroadcaster();
      logger.log(Level.FINEST, "SSE broadcaster created.");
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
    logger.log(
        Level.FINEST, "SSE event broadcast for cargo with tracking ID: {0}", cargo.getTrackingId());
    if (broadcaster != null) {
      broadcaster.broadcast(cargoToSseEvent(cargo));
    } else {
      logger.log(Level.FINEST, "No SSE subscribers registered yet.");
    }
  }

  private OutboundSseEvent cargoToSseEvent(Cargo cargo) {
    return sse.newEventBuilder()
        .mediaType(MediaType.APPLICATION_JSON_TYPE)
        .data(new RealtimeCargoTrackingViewAdapter(cargo))
        .build();
  }
}
