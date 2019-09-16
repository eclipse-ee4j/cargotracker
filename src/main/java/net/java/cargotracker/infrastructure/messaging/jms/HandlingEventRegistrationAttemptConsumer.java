/*
    The MIT License
    
    Copyright (c) 2019 Oracle and/or its affiliates
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/
package net.java.cargotracker.infrastructure.messaging.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import net.java.cargotracker.application.HandlingEventService;
import net.java.cargotracker.domain.model.handling.CannotCreateHandlingEventException;
import net.java.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;

/**
 * Consumes handling event registration attempt messages and delegates to proper
 * registration.
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup",
            propertyValue = "java:app/jms/HandlingEventRegistrationAttemptQueue")
})
public class HandlingEventRegistrationAttemptConsumer implements MessageListener {

    @Inject
    private HandlingEventService handlingEventService;
//    private static final Logger logger = Logger.getLogger(
//            HandlingEventRegistrationAttemptConsumer.class.getName());

    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            HandlingEventRegistrationAttempt attempt
                    = (HandlingEventRegistrationAttempt) objectMessage.getObject();
            handlingEventService.registerHandlingEvent(
                    attempt.getCompletionTime(),
                    attempt.getTrackingId(),
                    attempt.getVoyageNumber(),
                    attempt.getUnLocode(),
                    attempt.getType());
        } catch (JMSException | CannotCreateHandlingEventException e) {
            // Poison messages will be placed on dead-letter queue.
            throw new RuntimeException("Error occurred processing message", e);
//        } catch (JMSException e) {
            // logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
