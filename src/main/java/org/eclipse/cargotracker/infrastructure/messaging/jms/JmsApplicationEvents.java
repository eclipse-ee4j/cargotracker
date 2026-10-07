package org.eclipse.cargotracker.infrastructure.messaging.jms;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.Destination;
import jakarta.jms.JMSContext;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.cargotracker.application.ApplicationEvents;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;

@ApplicationScoped
public class JmsApplicationEvents implements ApplicationEvents, Serializable {

  private static final int LOW_PRIORITY = 0;
  @Inject JMSContext jmsContext;

  @Resource(lookup = "java:app/jms/CargoHandledQueue")
  private Destination cargoHandledQueue;

  @Resource(lookup = "java:app/jms/MisdirectedCargoQueue")
  private Destination misdirectedCargoQueue;

  @Resource(lookup = "java:app/jms/DeliveredCargoQueue")
  private Destination deliveredCargoQueue;

  @Resource(lookup = "java:app/jms/HandlingEventRegistrationAttemptQueue")
  private Destination handlingEventQueue;

  @Inject private Logger logger;

  @Override
  public void cargoWasHandled(HandlingEvent event) {
    Cargo cargo = event.getCargo();
    logger.log(Level.INFO, "Cargo with tracking ID {0} was handled", cargo.getTrackingId());
    jmsContext
        .createProducer()
        .setPriority(LOW_PRIORITY)
        .setDisableMessageID(true)
        .setDisableMessageTimestamp(true)
        .send(cargoHandledQueue, cargo.getTrackingId().id());
  }

  @Override
  public void cargoWasMisdirected(Cargo cargo) {
    logger.log(Level.INFO, "Cargo with tracking ID {0} was misdirected", cargo.getTrackingId());
    jmsContext
        .createProducer()
        .setPriority(LOW_PRIORITY)
        .setDisableMessageID(true)
        .setDisableMessageTimestamp(true)
        .send(misdirectedCargoQueue, cargo.getTrackingId().id());
  }

  @Override
  public void cargoHasArrived(Cargo cargo) {
    logger.log(Level.INFO, "Cargo with tracking ID {0} has arrived", cargo.getTrackingId());
    jmsContext
        .createProducer()
        .setPriority(LOW_PRIORITY)
        .setDisableMessageID(true)
        .setDisableMessageTimestamp(true)
        .send(deliveredCargoQueue, cargo.getTrackingId().id());
  }

  @Override
  public void receivedHandlingEventRegistrationAttempt(HandlingEventRegistrationAttempt attempt) {
    logger.log(Level.INFO, "Received handling event registration attempt {0}", attempt);
    jmsContext
        .createProducer()
        .setPriority(LOW_PRIORITY)
        .setDisableMessageID(true)
        .setDisableMessageTimestamp(true)
        .setTimeToLive(1000)
        .send(handlingEventQueue, attempt);
  }
}
