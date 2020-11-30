package org.eclipse.cargotracker.domain.model.handling;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;

import org.eclipse.cargotracker.application.util.DateUtil;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.RouteSpecification;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.location.SampleLocations;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.junit.Test;

//TODO [Jakarta EE 8] Move to the Java Date-Time API for date manipulation. Avoid hard-coded dates.
public class HandlingHistoryTest {

	Cargo cargo = new Cargo(new TrackingId("ABC"),
			new RouteSpecification(SampleLocations.SHANGHAI, SampleLocations.DALLAS, DateUtil.toDate("2009-04-01")));
	Voyage voyage = new Voyage.Builder(new VoyageNumber("X25"), SampleLocations.HONGKONG)
			.addMovement(SampleLocations.SHANGHAI, new Date(), new Date())
			.addMovement(SampleLocations.DALLAS, new Date(), new Date()).build();
	HandlingEvent event1 = new HandlingEvent(cargo, DateUtil.toDate("2009-03-05"), new Date(100),
			HandlingEvent.Type.LOAD, SampleLocations.SHANGHAI, voyage);
	HandlingEvent event1duplicate = new HandlingEvent(cargo, DateUtil.toDate("2009-03-05"), new Date(200),
			HandlingEvent.Type.LOAD, SampleLocations.SHANGHAI, voyage);
	HandlingEvent event2 = new HandlingEvent(cargo, DateUtil.toDate("2009-03-10"), new Date(150),
			HandlingEvent.Type.UNLOAD, SampleLocations.DALLAS, voyage);
	HandlingHistory handlingHistory = new HandlingHistory(Arrays.asList(event2, event1, event1duplicate));

	@Test
	public void testDistinctEventsByCompletionTime() {
		assertEquals(Arrays.asList(event1, event2), handlingHistory.getDistinctEventsByCompletionTime());
	}

	@Test
	public void testMostRecentlyCompletedEvent() {
		assertEquals(event2, handlingHistory.getMostRecentlyCompletedEvent());
	}
}
