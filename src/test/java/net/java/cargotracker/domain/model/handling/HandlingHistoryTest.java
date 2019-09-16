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

import net.java.cargotracker.application.util.DateUtil;
import net.java.cargotracker.domain.model.cargo.Cargo;
import net.java.cargotracker.domain.model.cargo.RouteSpecification;
import net.java.cargotracker.domain.model.cargo.TrackingId;
import net.java.cargotracker.domain.model.location.SampleLocations;
import net.java.cargotracker.domain.model.voyage.Voyage;
import net.java.cargotracker.domain.model.voyage.VoyageNumber;

import org.junit.Test;

//TODO This set of tests is very trivial, consider removing them.
public class HandlingHistoryTest {

	Cargo cargo = new Cargo(new TrackingId("ABC"), new RouteSpecification(
			SampleLocations.SHANGHAI, SampleLocations.DALLAS,
			DateUtil.toDate("2009-04-01")));
	Voyage voyage = new Voyage.Builder(new VoyageNumber("X25"),
			SampleLocations.HONGKONG)
			.addMovement(SampleLocations.SHANGHAI, new Date(), new Date())
			.addMovement(SampleLocations.DALLAS, new Date(), new Date())
			.build();
	HandlingEvent event1 = new HandlingEvent(cargo,
			DateUtil.toDate("2009-03-05"), new Date(100),
			HandlingEvent.Type.LOAD, SampleLocations.SHANGHAI, voyage);
	HandlingEvent event1duplicate = new HandlingEvent(cargo,
			DateUtil.toDate("2009-03-05"), new Date(200),
			HandlingEvent.Type.LOAD, SampleLocations.SHANGHAI, voyage);
	HandlingEvent event2 = new HandlingEvent(cargo,
			DateUtil.toDate("2009-03-10"), new Date(150),
			HandlingEvent.Type.UNLOAD, SampleLocations.DALLAS, voyage);
	HandlingHistory handlingHistory = new HandlingHistory(Arrays.asList(event2,
			event1, event1duplicate));

	@Test
	public void testDistinctEventsByCompletionTime() {
		assertEquals(Arrays.asList(event1, event2),
				handlingHistory.getDistinctEventsByCompletionTime());
	}

	@Test
	public void testMostRecentlyCompletedEvent() {
		assertEquals(event2, handlingHistory.getMostRecentlyCompletedEvent());
	}
}