package org.eclipse.cargotracker.infrastructure.messaging.jms;

import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import org.eclipse.cargotracker.application.CargoInspectionService;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;

/**
 * Consumes JMS messages and delegates notification of misdirected cargo to the tracking service.
 *
 * <p>This is a programmatic hook into the JMS infrastructure to make cargo inspection
 * message-driven.
 */
@MessageDriven(
    activationConfig = {
      @ActivationConfigProperty(
          propertyName = "destinationType",
          propertyValue = "jakarta.jms.Queue"),
      @ActivationConfigProperty(
          propertyName = "destinationLookup",
          propertyValue = "java:app/jms/CargoHandledQueue")
    })
public class CargoHandledConsumer implements MessageListener {

  @Inject private Logger logger;

  @Inject private CargoInspectionService cargoInspectionService;

  @Override
  public void onMessage(Message message) {
    try {
      TextMessage textMessage = (TextMessage) message;
      String trackingIdString = textMessage.getText();

      cargoInspectionService.inspectCargo(new TrackingId(trackingIdString));
    } catch (JMSException e) {
      logger.log(Level.SEVERE, "Error procesing JMS message", e);
    }
  }
}
