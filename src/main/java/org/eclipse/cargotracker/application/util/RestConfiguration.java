package org.eclipse.cargotracker.application.util;

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
    // properties.put(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    // for portable in other application servers.
    // see:
    // https://eclipse-ee4j.github.io/jersey.github.io/apidocs/latest/jersey/org/glassfish/jersey/server/ServerProperties.html#BV_SEND_ERROR_IN_RESPONSE
    properties.put("jersey.config.beanValidation.enableOutputValidationErrorEntity.server", true);
    return properties;
  }
}
