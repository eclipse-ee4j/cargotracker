package org.eclipse.cargotracker.application;

import org.eclipse.cargotracker.application.util.JsonMoxyConfigurationContextResolver;
import org.eclipse.pathfinder.api.GraphTraversalService;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * JAX-RS configuration.
 */
@ApplicationPath("rest")
public class BookingServiceTestRestConfiguration extends ResourceConfig {

    public BookingServiceTestRestConfiguration() {
        // Resources
        packages(new String[]{GraphTraversalService.class.getPackage().getName()});
        // Providers - JSON.
        register(new MoxyJsonFeature());
        register(new JsonMoxyConfigurationContextResolver()); // TODO [Jakarta EE 8] See if this can be removed.
    }
}
