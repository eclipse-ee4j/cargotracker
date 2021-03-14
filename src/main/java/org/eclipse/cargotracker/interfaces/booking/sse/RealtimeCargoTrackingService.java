package org.eclipse.cargotracker.interfaces.booking.sse;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
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
import org.eclipse.cargotracker.domain.model.cargo.RoutingStatus;
import org.eclipse.cargotracker.domain.model.cargo.TransportStatus;
import org.eclipse.cargotracker.infrastructure.events.cdi.CargoInspected;

/** Sever-sent events service for tracking all cargo in real time. */
@Singleton
@Path("/cargo")
public class RealtimeCargoTrackingService {
  @Inject private Logger logger;

  @Inject private CargoRepository cargoRepository;

  @Context private Sse sse;
  private SseBroadcaster broadcaster;

  @PostConstruct
  public void init() {
    broadcaster = sse.newBroadcaster();
    logger.log(Level.INFO, "SSE broadcaster created.");
  }

  @GET
  @Produces(MediaType.SERVER_SENT_EVENTS)
  public void tracking(@Context SseEventSink eventSink) {
    cargoRepository.findAll().stream().map(this::cargoToSseEvent).forEach(eventSink::send);

    broadcaster.register(eventSink);
    logger.log(Level.INFO, "SSE event sink registered.");
  }

  @PreDestroy
  public void close() {
    broadcaster.close();
    logger.log(Level.INFO, "SSE broadcaster closed.");
  }

  public void onCargoInspected(@ObservesAsync @CargoInspected Cargo cargo) {
    logger.log(Level.INFO, "SSE event broadcast.");
    broadcaster.broadcast(cargoToSseEvent(cargo));
  }

  private OutboundSseEvent cargoToSseEvent(Cargo cargo) {
    JsonObject cargoInJson =
        Json.createObjectBuilder()
            .add("trackingId", cargo.getTrackingId().getIdString())
            .add("routingStatus", cargo.getDelivery().getRoutingStatus().toString())
            .add("misdirected", cargo.getDelivery().isMisdirected())
            .add("transportStatus", cargo.getDelivery().getTransportStatus().toString())
            .add("atDestination", cargo.getDelivery().isUnloadedAtDestination())
            .add("origin", cargo.getOrigin().getUnLocode().getIdString())
            .add(
                "lastKnownLocation",
                cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString())
            .add(
                "locationCode",
                cargo.getDelivery().getTransportStatus() == TransportStatus.NOT_RECEIVED
                    ? cargo.getOrigin().getUnLocode().getIdString()
                    : cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString())
            .add("statusCode", statusCode(cargo))
            .build();

    return sse.newEventBuilder()
        .mediaType(MediaType.APPLICATION_JSON_TYPE)
        .data(cargoInJson)
        .build();
  }

  private String statusCode(Cargo cargo) {
    RoutingStatus routingStatus = cargo.getDelivery().getRoutingStatus();

    if (routingStatus == RoutingStatus.NOT_ROUTED || routingStatus == RoutingStatus.MISROUTED) {
      return routingStatus.toString();
    }

    if (cargo.getDelivery().isMisdirected()) {
      return "MISDIRECTED";
    }

    if (cargo.getDelivery().isUnloadedAtDestination()) {
      return "AT_DESTINATION";
    }

    return cargo.getDelivery().getTransportStatus().toString();
  }
}
