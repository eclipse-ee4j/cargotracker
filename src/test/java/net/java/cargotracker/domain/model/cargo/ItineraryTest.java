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
package net.java.cargotracker.domain.model.cargo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import net.java.cargotracker.domain.model.handling.HandlingEvent;
import net.java.cargotracker.domain.model.location.SampleLocations;
import net.java.cargotracker.domain.model.voyage.Voyage;
import net.java.cargotracker.domain.model.voyage.VoyageNumber;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class ItineraryTest {

    private Voyage voyage = new Voyage.Builder(new VoyageNumber("0123"),
            SampleLocations.SHANGHAI)
            .addMovement(SampleLocations.ROTTERDAM, new Date(), new Date())
            .addMovement(SampleLocations.GOTHENBURG, new Date(), new Date())
            .build();
    private Voyage wrongVoyage = new Voyage.Builder(new VoyageNumber("666"),
            SampleLocations.NEWYORK)
            .addMovement(SampleLocations.STOCKHOLM, new Date(), new Date())
            .addMovement(SampleLocations.HELSINKI, new Date(), new Date())
            .build();

    @Test
    public void testCargoOnTrack() {
        TrackingId trackingId = new TrackingId("CARGO1");
        RouteSpecification routeSpecification = new RouteSpecification(
                SampleLocations.SHANGHAI, SampleLocations.GOTHENBURG,
                new Date());
        Cargo cargo = new Cargo(trackingId, routeSpecification);

        Itinerary itinerary = new Itinerary(Arrays.asList(new Leg(voyage,
                SampleLocations.SHANGHAI, SampleLocations.ROTTERDAM,
                new Date(), new Date()), new Leg(voyage,
                        SampleLocations.ROTTERDAM, SampleLocations.GOTHENBURG,
                        new Date(), new Date())));

        // Happy path
        HandlingEvent event = new HandlingEvent(cargo, new Date(), new Date(),
                HandlingEvent.Type.RECEIVE, SampleLocations.SHANGHAI);
        assertTrue(itinerary.isExpected(event));

        event = new HandlingEvent(cargo, new Date(), new Date(),
                HandlingEvent.Type.LOAD, SampleLocations.SHANGHAI, voyage);
        assertTrue(itinerary.isExpected(event));

        event = new HandlingEvent(cargo, new Date(), new Date(),
                HandlingEvent.Type.UNLOAD, SampleLocations.ROTTERDAM, voyage);
        assertTrue(itinerary.isExpected(event));

        event = new HandlingEvent(cargo, new Date(), new Date(),
                HandlingEvent.Type.LOAD, SampleLocations.ROTTERDAM, voyage);
        assertTrue(itinerary.isExpected(event));

        event = new HandlingEvent(cargo, new Date(), new Date(),
                HandlingEvent.Type.UNLOAD, SampleLocations.GOTHENBURG, voyage);
        assertTrue(itinerary.isExpected(event));

        event = new HandlingEvent(cargo, new Date(), new Date(),
                HandlingEvent.Type.CLAIM, SampleLocations.GOTHENBURG);
        assertTrue(itinerary.isExpected(event));

        // Customs event changes nothing
        event = new HandlingEvent(cargo, new Date(), new Date(),
                HandlingEvent.Type.CUSTOMS, SampleLocations.GOTHENBURG);
        assertTrue(itinerary.isExpected(event));

        // Received at the wrong location
        event = new HandlingEvent(cargo, new Date(), new Date(),
                HandlingEvent.Type.RECEIVE, SampleLocations.HANGZOU);
        assertFalse(itinerary.isExpected(event));

        // Loaded to onto the wrong ship, correct location
        event = new HandlingEvent(cargo, new Date(), new Date(),
                HandlingEvent.Type.LOAD, SampleLocations.ROTTERDAM, wrongVoyage);
        assertFalse(itinerary.isExpected(event));

        // Unloaded from the wrong ship in the wrong location
        event = new HandlingEvent(cargo, new Date(), new Date(),
                HandlingEvent.Type.UNLOAD, SampleLocations.HELSINKI,
                wrongVoyage);
        assertFalse(itinerary.isExpected(event));

        event = new HandlingEvent(cargo, new Date(), new Date(),
                HandlingEvent.Type.CLAIM, SampleLocations.ROTTERDAM);
        assertFalse(itinerary.isExpected(event));
    }

    @Test
    public void testNextExpectedEvent() {
        // TODO
    }

    @Test
    public void testCreateItinerary() {
        try {
            @SuppressWarnings("unused")
            Itinerary itinerary = new Itinerary(new ArrayList<Leg>());
            fail("An empty itinerary is not OK");
        } catch (IllegalArgumentException iae) {
            // Expected
        }

        try {
            List<Leg> legs = null;
            @SuppressWarnings("unused")
            Itinerary itinerary = new Itinerary(legs);
            fail("Null itinerary is not OK");
        } catch (NullPointerException npe) {
            // Expected
        }
    }
}
