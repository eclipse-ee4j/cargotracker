package org.eclipse.cargotracker.infrastructure.routing;

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

import org.eclipse.cargotracker.application.util.JsonMoxyConfigurationContextResolver;
import org.eclipse.cargotracker.domain.model.cargo.Itinerary;
import org.eclipse.cargotracker.domain.model.cargo.Leg;
import org.eclipse.cargotracker.domain.model.cargo.RouteSpecification;
import org.eclipse.cargotracker.domain.model.location.LocationRepository;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.eclipse.cargotracker.domain.model.voyage.VoyageRepository;
import org.eclipse.cargotracker.domain.service.RoutingService;
import org.eclipse.pathfinder.api.TransitEdge;
import org.eclipse.pathfinder.api.TransitPath;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;

/**
 * Our end of the routing service. This is basically a data model translation
 * layer between our domain model and the API put forward by the routing team,
 * which operates in a different context from us.
 */
@Stateless
public class ExternalRoutingService implements RoutingService {

	@Inject
	private Logger logger;
	
	@Resource(lookup = "java:app/configuration/GraphTraversalUrl")
	private String graphTraversalUrl;
	
	private final Client jaxrsClient = ClientBuilder.newClient();
	private WebTarget graphTraversalResource;
	
	@Inject
	private LocationRepository locationRepository;
	@Inject
	private VoyageRepository voyageRepository;

	@PostConstruct
	public void init() {
		graphTraversalResource = jaxrsClient.target(graphTraversalUrl);
		graphTraversalResource.register(new MoxyJsonFeature()).register(new JsonMoxyConfigurationContextResolver());
	}

	@Override
	public List<Itinerary> fetchRoutesForSpecification(RouteSpecification routeSpecification) {
		// The RouteSpecification is picked apart and adapted to the external API.
		String origin = routeSpecification.getOrigin().getUnLocode().getIdString();
		String destination = routeSpecification.getDestination().getUnLocode().getIdString();

		List<TransitPath> transitPaths = graphTraversalResource.queryParam("origin", origin)
				.queryParam("destination", destination).request(MediaType.APPLICATION_JSON_TYPE)
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
				logger.log(Level.FINE, "Received itinerary that did not satisfy the route specification");
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
		return new Leg(voyageRepository.find(new VoyageNumber(edge.getVoyageNumber())),
				locationRepository.find(new UnLocode(edge.getFromUnLocode())),
				locationRepository.find(new UnLocode(edge.getToUnLocode())), edge.getFromDate(), edge.getToDate());
	}
}
