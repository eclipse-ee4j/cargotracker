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
package jakarta.cargotracker.interfaces.booking.socket;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import jakarta.cargotracker.infrastructure.events.cdi.CargoInspected;
import jakarta.cargotracker.domain.model.cargo.Cargo;

/**
 * WebSocket service for tracking all cargoes in real time.
 *
 * @author Vijay Nair
 */
@Singleton
@ServerEndpoint("/tracking")
public class RealtimeCargoTrackingService {

    private static final Logger logger = Logger.getLogger(
            RealtimeCargoTrackingService.class.getName());

    private final Set<Session> sessions = new HashSet<>();

    @OnOpen
    public void onOpen(final Session session) {
        // Infinite by default on GlassFish. We need this principally for WebLogic.
        session.setMaxIdleTimeout(5 * 60 * 1000); 
        sessions.add(session);
    }

    @OnClose
    public void onClose(final Session session) {
        sessions.remove(session);
    }

    public void onCargoInspected(@Observes @CargoInspected Cargo cargo) {
        Writer writer = new StringWriter();

        try (JsonGenerator generator = Json.createGenerator(writer)) {
            generator
                    .writeStartObject()
                    .write("trackingId", cargo.getTrackingId().getIdString())
                    .write("origin", cargo.getOrigin().getName())
                    .write("destination", cargo.getRouteSpecification().getDestination().getName())
                    .write("lastKnownLocation", cargo.getDelivery().getLastKnownLocation().getName())
                    .write("transportStatus", cargo.getDelivery().getTransportStatus().toString())
                    .writeEnd();
        }

        String jsonValue = writer.toString();

        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(jsonValue);
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Unable to publish WebSocket message", ex);
            }
        }

    }
}