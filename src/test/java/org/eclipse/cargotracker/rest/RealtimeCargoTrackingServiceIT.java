package org.eclipse.cargotracker.rest;

import java.util.ArrayList;
import java.util.Map;

import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

public class RealtimeCargoTrackingServiceIT {

    @Test
    public void testGetCargo() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:8080/cargo-tracker/rest/cargo");

        EventInput eventInput = (EventInput)webTarget.request().get(EventInput.class);
        Jsonb jsonb = JsonbBuilder.create();
        ArrayList<String> trackingIDs = new ArrayList<String>();
        int i = 0;
        while ((!eventInput.isClosed()) && (i < 4)) {
            InboundEvent inboundEvent = (InboundEvent)eventInput.read();
            if (inboundEvent == null) {
                break;
            }
            String eventStr = (String)inboundEvent.readData(String.class);
            Map cargoRoute = (Map) jsonb.fromJson(eventStr, Map.class);
            trackingIDs.add(cargoRoute.get("trackingId").toString());
            i++;
        }
        eventInput.close();
        Assertions.assertEquals(4, trackingIDs.size(), "Expected at least 4 cargo routes");
        Assertions.assertTrue(
            (trackingIDs.contains("ABC123")) || 
            (trackingIDs.contains("JKL567")) || 
            (trackingIDs.contains("DEF789")) || 
            (trackingIDs.contains("MNO456")), 
            "Expected either one of the tracking IDs");
    }

}