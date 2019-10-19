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
package jakarta.cargotracker.domain.model.cargo;

import jakarta.cargotracker.domain.model.cargo.RouteSpecification;
import jakarta.cargotracker.domain.model.cargo.Leg;
import jakarta.cargotracker.domain.model.cargo.Itinerary;
import java.util.Arrays;
import jakarta.cargotracker.application.util.DateUtil;
import jakarta.cargotracker.domain.model.location.SampleLocations;
import jakarta.cargotracker.domain.model.voyage.Voyage;
import jakarta.cargotracker.domain.model.voyage.VoyageNumber;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class RouteSpecificationTest {

	Voyage hongKongTokyoNewYork = new Voyage.Builder(new VoyageNumber("V001"),
			SampleLocations.HONGKONG)
			.addMovement(SampleLocations.TOKYO, DateUtil.toDate("2009-02-01"),
					DateUtil.toDate("2009-02-05"))
			.addMovement(SampleLocations.NEWYORK,
					DateUtil.toDate("2009-02-06"),
					DateUtil.toDate("2009-02-10"))
			.addMovement(SampleLocations.HONGKONG,
					DateUtil.toDate("2009-02-11"),
					DateUtil.toDate("2009-02-14")).build();
	Voyage dallasNewYorkChicago = new Voyage.Builder(new VoyageNumber("V002"),
			SampleLocations.DALLAS)
			.addMovement(SampleLocations.NEWYORK,
					DateUtil.toDate("2009-02-06"),
					DateUtil.toDate("2009-02-07"))
			.addMovement(SampleLocations.CHICAGO,
					DateUtil.toDate("2009-02-12"),
					DateUtil.toDate("2009-02-20")).build();
	// TODO:
	// It shouldn't be possible to create Legs that have load/unload locations
	// and/or dates that don't match the voyage's carrier movements.
	Itinerary itinerary = new Itinerary(Arrays.asList(
			new Leg(hongKongTokyoNewYork, SampleLocations.HONGKONG,
					SampleLocations.NEWYORK, DateUtil.toDate("2009-02-01"),
					DateUtil.toDate("2009-02-10")), new Leg(
					dallasNewYorkChicago, SampleLocations.NEWYORK,
					SampleLocations.CHICAGO, DateUtil.toDate("2009-02-12"),
					DateUtil.toDate("2009-02-20"))));

	@Test
	public void testIsSatisfiedBySuccess() {
		RouteSpecification routeSpecification = new RouteSpecification(
				SampleLocations.HONGKONG, SampleLocations.CHICAGO,
				DateUtil.toDate("2009-03-01"));

		assertTrue(routeSpecification.isSatisfiedBy(itinerary));
	}

	@Test
	public void testIsNotSatisfiedByWrongOrigin() {
		RouteSpecification routeSpecification = new RouteSpecification(
				SampleLocations.HANGZOU, SampleLocations.CHICAGO,
				DateUtil.toDate("2009-03-01"));

		assertFalse(routeSpecification.isSatisfiedBy(itinerary));
	}

	@Test
	public void testIsNotSatisfiedByWrongDestination() {
		RouteSpecification routeSpecification = new RouteSpecification(
				SampleLocations.HONGKONG, SampleLocations.DALLAS,
				DateUtil.toDate("2009-03-01"));

		assertFalse(routeSpecification.isSatisfiedBy(itinerary));
	}

	@Test
	public void testIsNotSatisfiedByMissedDeadline() {
		RouteSpecification routeSpecification = new RouteSpecification(
				SampleLocations.HONGKONG, SampleLocations.CHICAGO,
				DateUtil.toDate("2009-02-15"));

		assertFalse(routeSpecification.isSatisfiedBy(itinerary));
	}
}