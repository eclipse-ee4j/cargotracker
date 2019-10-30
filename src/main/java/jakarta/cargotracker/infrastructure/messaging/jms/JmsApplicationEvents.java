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
package jakarta.cargotracker.infrastructure.messaging.jms;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import jakarta.cargotracker.application.ApplicationEvents;
import jakarta.cargotracker.domain.model.cargo.Cargo;
import jakarta.cargotracker.domain.model.handling.HandlingEvent;
import jakarta.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;

@ApplicationScoped
public class JmsApplicationEvents implements ApplicationEvents, Serializable {

    private static final int LOW_PRIORITY = 0;
    @Inject
    JMSContext jmsContext;
    @Resource(lookup = "java:app/jms/CargoHandledQueue")
    private Destination cargoHandledQueue;
    @Resource(lookup = "java:app/jms/MisdirectedCargoQueue")
    private Destination misdirectedCargoQueue;
    @Resource(lookup = "java:app/jms/DeliveredCargoQueue")
    private Destination deliveredCargoQueue;
    @Resource(lookup = "java:app/jms/HandlingEventRegistrationAttemptQueue")
    private Destination handlingEventQueue;
    private static final Logger logger = Logger.getLogger(
            JmsApplicationEvents.class.getName());

    @Override
    public void cargoWasHandled(HandlingEvent event) {
        Cargo cargo = event.getCargo();
        logger.log(Level.INFO, "Cargo was handled {0}", cargo);
        jmsContext.createProducer()
                .setPriority(LOW_PRIORITY)
                .setDisableMessageID(true)
                .setDisableMessageTimestamp(true)
                .send(cargoHandledQueue,
                        cargo.getTrackingId().getIdString());
    }

    @Override
    public void cargoWasMisdirected(Cargo cargo) {
        logger.log(Level.INFO, "Cargo was misdirected {0}", cargo);
        jmsContext.createProducer()
                .setPriority(LOW_PRIORITY)
                .setDisableMessageID(true)
                .setDisableMessageTimestamp(true)
                .send(misdirectedCargoQueue,
                        cargo.getTrackingId().getIdString());
    }

    @Override
    public void cargoHasArrived(Cargo cargo) {
        logger.log(Level.INFO, "Cargo has arrived {0}", cargo);
        jmsContext.createProducer()
                .setPriority(LOW_PRIORITY)
                .setDisableMessageID(true)
                .setDisableMessageTimestamp(true)
                .send(deliveredCargoQueue,
                        cargo.getTrackingId().getIdString());
    }

    @Override
    public void receivedHandlingEventRegistrationAttempt(
            HandlingEventRegistrationAttempt attempt) {
        logger.log(Level.INFO, "Received handling event registration attempt {0}",
                attempt);
        jmsContext.createProducer()
                .setPriority(LOW_PRIORITY)
                .setDisableMessageID(true)
                .setDisableMessageTimestamp(true)
                .setTimeToLive(1000)
                .send(handlingEventQueue, attempt);
    }
}