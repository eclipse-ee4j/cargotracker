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
package net.java.cargotracker.domain.model.handling;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import net.java.cargotracker.domain.model.cargo.Cargo;
import net.java.cargotracker.domain.model.cargo.RouteSpecification;
import net.java.cargotracker.domain.model.cargo.TrackingId;
import net.java.cargotracker.domain.model.location.SampleLocations;
import net.java.cargotracker.domain.model.voyage.SampleVoyages;

import org.junit.Test;

// TODO This set of tests is very trivial, consider removing them.
public class HandlingEventTest {

	private final TrackingId trackingId = new TrackingId("XYZ");
	private final RouteSpecification routeSpecification = new RouteSpecification(
			SampleLocations.HONGKONG, SampleLocations.NEWYORK, new Date());
	private final Cargo cargo = new Cargo(trackingId, routeSpecification);

	@Test
	public void testNewWithCarrierMovement() {
		HandlingEvent event1 = new HandlingEvent(cargo, new Date(), new Date(),
				HandlingEvent.Type.LOAD, SampleLocations.HONGKONG,
				SampleVoyages.CM003);
		assertEquals(SampleLocations.HONGKONG, event1.getLocation());

		HandlingEvent event2 = new HandlingEvent(cargo, new Date(), new Date(),
				HandlingEvent.Type.UNLOAD, SampleLocations.NEWYORK,
				SampleVoyages.CM003);
		assertEquals(SampleLocations.NEWYORK, event2.getLocation());

		// These event types prohibit a carrier movement association
		for (HandlingEvent.Type type : Arrays.asList(HandlingEvent.Type.CLAIM,
				HandlingEvent.Type.RECEIVE, HandlingEvent.Type.CUSTOMS)) {
			try {
				new HandlingEvent(cargo, new Date(), new Date(), type,
						SampleLocations.HONGKONG, SampleVoyages.CM003);
				fail("Handling event type " + type
						+ " prohibits carrier movement");
			} catch (IllegalArgumentException expected) {
			}
		}

		// These event types requires a carrier movement association
		for (HandlingEvent.Type type : Arrays.asList(HandlingEvent.Type.LOAD,
				HandlingEvent.Type.UNLOAD)) {
			try {
				new HandlingEvent(cargo, new Date(), new Date(), type,
						SampleLocations.HONGKONG, null);
				fail("Handling event type " + type
						+ " requires carrier movement");
			} catch (NullPointerException expected) {
			}
		}
	}

	@Test
	public void testNewWithLocation() {
		HandlingEvent event1 = new HandlingEvent(cargo, new Date(), new Date(),
				HandlingEvent.Type.CLAIM, SampleLocations.HELSINKI);
		assertEquals(SampleLocations.HELSINKI, event1.getLocation());
	}

	@Test
	public void testCurrentLocationLoadEvent() throws Exception {
		HandlingEvent event = new HandlingEvent(cargo, new Date(), new Date(),
				HandlingEvent.Type.LOAD, SampleLocations.CHICAGO,
				SampleVoyages.CM004);

		assertEquals(SampleLocations.CHICAGO, event.getLocation());
	}

	@Test
	public void testCurrentLocationUnloadEvent() throws Exception {
		HandlingEvent ev = new HandlingEvent(cargo, new Date(), new Date(),
				HandlingEvent.Type.UNLOAD, SampleLocations.HAMBURG,
				SampleVoyages.CM004);

		org.junit.Assert
				.assertEquals(SampleLocations.HAMBURG, ev.getLocation());
	}

	@Test
	public void testCurrentLocationReceivedEvent() throws Exception {
		HandlingEvent event = new HandlingEvent(cargo, new Date(), new Date(),
				HandlingEvent.Type.RECEIVE, SampleLocations.CHICAGO);

		assertEquals(SampleLocations.CHICAGO, event.getLocation());
	}

	@Test
	public void testCurrentLocationClaimedEvent() throws Exception {
		HandlingEvent event = new HandlingEvent(cargo, new Date(), new Date(),
				HandlingEvent.Type.CLAIM, SampleLocations.CHICAGO);

		assertEquals(SampleLocations.CHICAGO, event.getLocation());
	}
}
