package org.eclipse.cargotracker.application.util;

import org.glassfish.jersey.server.ServerProperties;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.Map;

/** Jakarta REST configuration. */
@ApplicationPath("rest")
public class RestConfiguration extends Application {

  @Override
  public Map<String, Object> getProperties() {
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    return properties;
  }
}
