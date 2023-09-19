package org.eclipse.cargotracker.rest;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.eclipse.pathfinder.api.TransitEdge;
import org.eclipse.pathfinder.api.TransitPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GraphTraversalServiceIT {

    @Test
    public void testShortestPath() {

        SimpleDateFormat f = new SimpleDateFormat("MMddyyyy");
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        String deadline = f.format(nextYear.getTime()); 

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:8080/cargo-tracker/rest/graph-traversal/shortest-path");
        WebTarget shortPathTarget = webTarget.queryParam("origin", new Object[] { "USNYC" }).queryParam("destination", new Object[] { "CNHKG" }).queryParam("deadline", new Object[] { deadline });
        Response response = shortPathTarget.request(new String[] { "application/json" }).get();
        Assertions.assertEquals(200, response.getStatus(), "Unexpected response code");

        String responseStr = (String)response.readEntity(String.class);
        Jsonb jsonb = JsonbBuilder.create();
        TransitPath[] transitPaths = (TransitPath[]) jsonb.fromJson(responseStr, TransitPath[].class);
        Assertions.assertTrue((transitPaths.length > 0), "Expected at least 1 transit path.");
        for (TransitPath p : transitPaths) {
            List<TransitEdge> edges = p.getTransitEdges();
            Assertions.assertTrue((edges.size() > 0), "Expected at least 1 transit edge.");
        }
    }

}

