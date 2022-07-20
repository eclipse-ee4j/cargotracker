package org.eclipse.cargotracker.infrastructure.messaging.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.eclipse.cargotracker.application.HandlingEventService;
import org.eclipse.cargotracker.domain.model.handling.CannotCreateHandlingEventException;
import org.eclipse.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;

/** Consumes handling event registration attempt messages and delegates to proper registration. */
@MessageDriven(
    activationConfig = {
      @ActivationConfigProperty(
          propertyName = "destinationType",
          propertyValue = "javax.jms.Queue"),
      @ActivationConfigProperty(
          propertyName = "destinationLookup",
          propertyValue = "java:app/jms/HandlingEventRegistrationAttemptQueue")
    })
public class HandlingEventRegistrationAttemptConsumer implements MessageListener {

  @Inject private HandlingEventService handlingEventService;

  @Override
  public void onMessage(Message message) {
    try {
      ObjectMessage objectMessage = (ObjectMessage) message;
      HandlingEventRegistrationAttempt attempt =
          (HandlingEventRegistrationAttempt) objectMessage.getObject();
      handlingEventService.registerHandlingEvent(
          attempt.getCompletionTime(),
          attempt.getTrackingId(),
          attempt.getVoyageNumber(),
          attempt.getUnLocode(),
          attempt.getType());
    } catch (JMSException | CannotCreateHandlingEventException e) {
      // Poison messages will be placed on dead-letter queue.
      throw new RuntimeException("Error occurred processing message", e);
    }
  }
}
