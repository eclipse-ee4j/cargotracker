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
package net.java.cargotracker.infrastructure.routing;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import net.java.cargotracker.application.util.JsonMoxyConfigurationContextResolver;
import net.java.cargotracker.domain.model.cargo.Itinerary;
import net.java.cargotracker.domain.model.cargo.Leg;
import net.java.cargotracker.domain.model.cargo.RouteSpecification;
import net.java.cargotracker.domain.model.location.LocationRepository;
import net.java.cargotracker.domain.model.location.UnLocode;
import net.java.cargotracker.domain.model.voyage.VoyageNumber;
import net.java.cargotracker.domain.model.voyage.VoyageRepository;
import net.java.cargotracker.domain.service.RoutingService;
import net.java.pathfinder.api.TransitEdge;
import net.java.pathfinder.api.TransitPath;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;

/**
 * Our end of the routing service. This is basically a data model translation
 * layer between our domain model and the API put forward by the routing team,
 * which operates in a different context from us.
 *
 */
@Stateless
public class ExternalRoutingService implements RoutingService {

    @Resource(lookup = "java:app/configuration/GraphTraversalUrl")
    private String graphTraversalUrl;
    // TODO Can I use injection?
    private final Client jaxrsClient = ClientBuilder.newClient();
    private WebTarget graphTraversalResource;
    @Inject
    private LocationRepository locationRepository;
    @Inject
    private VoyageRepository voyageRepository;
    // TODO Use injection instead?
    private static final Logger log = Logger.getLogger(
            ExternalRoutingService.class.getName());

    @PostConstruct
    public void init() {
        graphTraversalResource = jaxrsClient.target(graphTraversalUrl);
        graphTraversalResource.register(new MoxyJsonFeature()).register(
                new JsonMoxyConfigurationContextResolver());
    }

    @Override
    public List<Itinerary> fetchRoutesForSpecification(
            RouteSpecification routeSpecification) {
        // The RouteSpecification is picked apart and adapted to the external API.
        String origin = routeSpecification.getOrigin().getUnLocode().getIdString();
        String destination = routeSpecification.getDestination().getUnLocode()
                .getIdString();

        List<TransitPath> transitPaths = graphTraversalResource
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<TransitPath>>() {
                });

        // The returned result is then translated back into our domain model.
        List<Itinerary> itineraries = new ArrayList<>();

        for (TransitPath transitPath : transitPaths) {
            Itinerary itinerary = toItinerary(transitPath);
            // Use the specification to safe-guard against invalid itineraries
            if (routeSpecification.isSatisfiedBy(itinerary)) {
                itineraries.add(itinerary);
            } else {
                log.log(Level.FINE,
                        "Received itinerary that did not satisfy the route specification");
            }
        }

        return itineraries;
    }

    private Itinerary toItinerary(TransitPath transitPath) {
        List<Leg> legs = new ArrayList<>(transitPath.getTransitEdges().size());
        for (TransitEdge edge : transitPath.getTransitEdges()) {
            legs.add(toLeg(edge));
        }
        return new Itinerary(legs);
    }

    private Leg toLeg(TransitEdge edge) {
        return new Leg(
                voyageRepository.find(new VoyageNumber(edge.getVoyageNumber())),
                locationRepository.find(new UnLocode(edge.getFromUnLocode())),
                locationRepository.find(new UnLocode(edge.getToUnLocode())),
                edge.getFromDate(), edge.getToDate());
    }
}
