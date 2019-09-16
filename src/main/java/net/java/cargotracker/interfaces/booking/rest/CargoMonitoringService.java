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
package net.java.cargotracker.interfaces.booking.rest;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import net.java.cargotracker.domain.model.cargo.Cargo;
import net.java.cargotracker.domain.model.cargo.CargoRepository;

@Stateless
@Path("/cargo")
public class CargoMonitoringService {

    public static final String ISO_8601_FORMAT = "yyyy-MM-dd HH:mm";
    @Inject
    private CargoRepository cargoRepository;
    
    public CargoMonitoringService() {} 

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getAllCargo() {
        List<Cargo> cargos = cargoRepository.findAll();

        JsonArrayBuilder builder = Json.createArrayBuilder();

        for (Cargo cargo : cargos) {
            builder.add(Json.createObjectBuilder()
                    .add("trackingId", cargo.getTrackingId().getIdString())
                    .add("routingStatus", cargo.getDelivery()
                            .getRoutingStatus().toString())
                    .add("misdirected", cargo.getDelivery().isMisdirected())
                    .add("transportStatus", cargo.getDelivery()
                            .getTransportStatus().toString())
                    .add("atDestination", cargo.getDelivery()
                            .isUnloadedAtDestination())
                    .add("origin", cargo.getOrigin().getUnLocode().getIdString())
                    .add("lastKnownLocation",
                            cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString().equals("XXXXX")
                                    ? "Unknown"
                                    : cargo.getDelivery().getLastKnownLocation().getUnLocode().getIdString())
            );
        }

        return builder.build();
    }
}
