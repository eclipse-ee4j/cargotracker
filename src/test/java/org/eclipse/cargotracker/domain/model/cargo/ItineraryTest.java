package org.eclipse.cargotracker.domain.model.cargo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.location.SampleLocations;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.junit.Test;

public class ItineraryTest {

	private Voyage voyage = new Voyage.Builder(new VoyageNumber("0123"), SampleLocations.SHANGHAI)
			.addMovement(SampleLocations.ROTTERDAM, new Date(), new Date())
			.addMovement(SampleLocations.GOTHENBURG, new Date(), new Date()).build();
	private Voyage wrongVoyage = new Voyage.Builder(new VoyageNumber("666"), SampleLocations.NEWYORK)
			.addMovement(SampleLocations.STOCKHOLM, new Date(), new Date())
			.addMovement(SampleLocations.HELSINKI, new Date(), new Date()).build();

	@Test
	public void testCargoOnTrack() {
		TrackingId trackingId = new TrackingId("CARGO1");
		RouteSpecification routeSpecification = new RouteSpecification(SampleLocations.SHANGHAI,
				SampleLocations.GOTHENBURG, new Date());
		Cargo cargo = new Cargo(trackingId, routeSpecification);

		Itinerary itinerary = new Itinerary(Arrays.asList(
				new Leg(voyage, SampleLocations.SHANGHAI, SampleLocations.ROTTERDAM, new Date(), new Date()),
				new Leg(voyage, SampleLocations.ROTTERDAM, SampleLocations.GOTHENBURG, new Date(), new Date())));

		// Happy path
		HandlingEvent event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.RECEIVE,
				SampleLocations.SHANGHAI);
		assertTrue(itinerary.isExpected(event));

		event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.LOAD, SampleLocations.SHANGHAI,
				voyage);
		assertTrue(itinerary.isExpected(event));

		event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.UNLOAD, SampleLocations.ROTTERDAM,
				voyage);
		assertTrue(itinerary.isExpected(event));

		event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.LOAD, SampleLocations.ROTTERDAM,
				voyage);
		assertTrue(itinerary.isExpected(event));

		event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.UNLOAD, SampleLocations.GOTHENBURG,
				voyage);
		assertTrue(itinerary.isExpected(event));

		event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.CLAIM, SampleLocations.GOTHENBURG);
		assertTrue(itinerary.isExpected(event));

		// Customs event changes nothing
		event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.CUSTOMS,
				SampleLocations.GOTHENBURG);
		assertTrue(itinerary.isExpected(event));

		// Received at the wrong location
		event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.RECEIVE, SampleLocations.HANGZOU);
		assertFalse(itinerary.isExpected(event));

		// Loaded to onto the wrong ship, correct location
		event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.LOAD, SampleLocations.ROTTERDAM,
				wrongVoyage);
		assertFalse(itinerary.isExpected(event));

		// Unloaded from the wrong ship in the wrong location
		event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.UNLOAD, SampleLocations.HELSINKI,
				wrongVoyage);
		assertFalse(itinerary.isExpected(event));

		event = new HandlingEvent(cargo, new Date(), new Date(), HandlingEvent.Type.CLAIM, SampleLocations.ROTTERDAM);
		assertFalse(itinerary.isExpected(event));
	}

	@Test
	public void testNextExpectedEvent() {
		// TODO [TDD] Complete this test.
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
