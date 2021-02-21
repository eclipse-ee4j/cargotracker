package org.eclipse.cargotracker.interfaces.booking.rest;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.CargoRepository;

@Stateless
@Path("/cargo")
public class CargoMonitoringService {

  public static final String ISO_8601_FORMAT = "yyyy-MM-dd HH:mm";

  @Inject private CargoRepository cargoRepository;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public JsonArray getAllCargo() {
    List<Cargo> cargos = cargoRepository.findAll();

    JsonArrayBuilder builder = Json.createArrayBuilder();

    cargos.stream().map(this::cargoToJson).forEach(builder::add);

    return builder.build();
  }

  private JsonObjectBuilder cargoToJson(Cargo cargo) {
    return Json.createObjectBuilder()
        .add("trackingId", cargo.getTrackingId().getIdString())
        .add("routingStatus", cargo.getDelivery().getRoutingStatus().toString())
        .add("misdirected", cargo.getDelivery().isMisdirected())
        .add("transportStatus", cargo.getDelivery().getTransportStatus().toString())
        .add("atDestination", cargo.getDelivery().isUnloadedAtDestination())
        .add("origin", cargo.getOrigin().getUnLocode().getIdString())
        .add(
            "lastKnownLocation",
            cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString().equals("XXXXX")
                ? "Unknown"
                : cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString());
  }
}
