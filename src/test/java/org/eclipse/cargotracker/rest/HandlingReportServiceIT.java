package org.eclipse.cargotracker.rest;

import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HandlingReportServiceIT {

    @Test
    public void testHandlingRreports() {

        SimpleDateFormat f = new SimpleDateFormat("M/dd/yyyy");
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        String completionDate = f.format(nextYear.getTime());

        Properties p = new Properties();
        p.put("completionTime", completionDate + " 3:3 a.m.");
        p.put("trackingId", "ABC123");
        p.put("eventType", "LOAD");
        p.put("unLocode", "CNHGH");
        p.put("voyageNumber", "0100S");
        String jsonParms = JsonbBuilder.create().toJson(p);

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://localhost:8080/cargo-tracker/rest/handling/reports");
        Response response = webTarget.request().post(Entity.entity(jsonParms, "application/json"));
        Assertions.assertEquals(204, response.getStatus(), "Unexpected response code");
    }

}

