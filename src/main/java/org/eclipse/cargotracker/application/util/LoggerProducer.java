package org.eclipse.cargotracker.application.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.logging.Logger;

@Named
@Singleton
public class LoggerProducer {

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        String loggerName = extractLoggerName(injectionPoint);

        return Logger.getLogger(loggerName);
    }

    private String extractLoggerName(InjectionPoint ip) {
        if (ip.getBean() == null) {
            return ip.getMember().getDeclaringClass().getName();
        }

        if (ip.getBean().getName() == null) {
            return ip.getBean().getBeanClass().getName();
        }

        return ip.getBean().getName();
    }
}
