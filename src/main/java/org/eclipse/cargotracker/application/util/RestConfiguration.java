package org.eclipse.cargotracker.application.util;

import javax.ws.rs.ApplicationPath;

import org.eclipse.cargotracker.interfaces.booking.rest.CargoMonitoringService;
import org.eclipse.cargotracker.interfaces.handling.rest.HandlingReportService;
import org.eclipse.pathfinder.api.GraphTraversalService;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

/**
 * JAX-RS configuration.
 */
@ApplicationPath("rest")
public class RestConfiguration extends ResourceConfig {

	public RestConfiguration() {
		// Resources
		packages(new String[] { HandlingReportService.class.getPackage().getName(),
				GraphTraversalService.class.getPackage().getName(),
				CargoMonitoringService.class.getPackage().getName() });
		// Enable Bean Validation error messages.
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		// Providers - JSON.
		register(new MoxyJsonFeature());
		register(new JsonMoxyConfigurationContextResolver()); // TODO [Jakarta EE 8] See if this can be removed.
	}
}
