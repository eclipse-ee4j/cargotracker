package org.eclipse.cargotracker.infrastructure.logging;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import java.io.Serializable;
import java.util.logging.Logger;

@ApplicationScoped
public class LoggerProducer implements Serializable {

  private static final long serialVersionUID = 1L;

  @Produces
  public Logger produceLogger(InjectionPoint injectionPoint) {
    String loggerName = extractLoggerName(injectionPoint);

    return Logger.getLogger(loggerName);
  }

  private String extractLoggerName(InjectionPoint injectionPoint) {
    if (injectionPoint.getBean() == null) {
      return injectionPoint.getMember().getDeclaringClass().getName();
    }

    if (injectionPoint.getBean().getName() == null) {
      return injectionPoint.getBean().getBeanClass().getName();
    }

    return injectionPoint.getBean().getName();
  }
}
