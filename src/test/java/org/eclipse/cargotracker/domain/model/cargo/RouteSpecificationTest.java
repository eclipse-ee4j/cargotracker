package org.eclipse.cargotracker.domain.model.cargo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.eclipse.cargotracker.application.util.DateUtil;
import org.eclipse.cargotracker.domain.model.location.SampleLocations;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.junit.Test;

// TODO [Jakarta EE 8] Move to the Java Date-Time API for date manipulation. Avoid hard-coded dates.
public class RouteSpecificationTest {

    Voyage hongKongTokyoNewYork =
            new Voyage.Builder(new VoyageNumber("V001"), SampleLocations.HONGKONG)
                    .addMovement(
                            SampleLocations.TOKYO,
                            DateUtil.toDateTime("2009-02-01", "00:00"),
                            DateUtil.toDateTime("2009-02-05", "00:00"))
                    .addMovement(
                            SampleLocations.NEWYORK,
                            DateUtil.toDateTime("2009-02-06", "00:00"),
                            DateUtil.toDateTime("2009-02-10", "00:00"))
                    .addMovement(
                            SampleLocations.HONGKONG,
                            DateUtil.toDateTime("2009-02-11", "00:00"),
                            DateUtil.toDateTime("2009-02-14", "00:00"))
                    .build();
    Voyage dallasNewYorkChicago =
            new Voyage.Builder(new VoyageNumber("V002"), SampleLocations.DALLAS)
                    .addMovement(
                            SampleLocations.NEWYORK,
                            DateUtil.toDateTime("2009-02-06", "00:00"),
                            DateUtil.toDateTime("2009-02-07", "00:00"))
                    .addMovement(
                            SampleLocations.CHICAGO,
                            DateUtil.toDateTime("2009-02-12", "00:00"),
                            DateUtil.toDateTime("2009-02-20", "00:00"))
                    .build();
    Itinerary itinerary =
            new Itinerary(
                    Arrays.asList(
                            new Leg(
                                    hongKongTokyoNewYork,
                                    SampleLocations.HONGKONG,
                                    SampleLocations.NEWYORK,
                                    DateUtil.toDateTime("2009-02-01", "00:00"),
                                    DateUtil.toDateTime("2009-02-10", "00:00")),
                            new Leg(
                                    dallasNewYorkChicago,
                                    SampleLocations.NEWYORK,
                                    SampleLocations.CHICAGO,
                                    DateUtil.toDateTime("2009-02-12", "00:00"),
                                    DateUtil.toDateTime("2009-02-20", "00:00"))));

    @Test
    public void testIsSatisfiedBySuccess() {
        RouteSpecification routeSpecification =
                new RouteSpecification(
                        SampleLocations.HONGKONG,
                        SampleLocations.CHICAGO,
                        DateUtil.toDate("2009-03-01"));

        assertTrue(routeSpecification.isSatisfiedBy(itinerary));
    }

    @Test
    public void testIsNotSatisfiedByWrongOrigin() {
        RouteSpecification routeSpecification =
                new RouteSpecification(
                        SampleLocations.HANGZOU,
                        SampleLocations.CHICAGO,
                        DateUtil.toDate("2009-03-01"));

        assertFalse(routeSpecification.isSatisfiedBy(itinerary));
    }

    @Test
    public void testIsNotSatisfiedByWrongDestination() {
        RouteSpecification routeSpecification =
                new RouteSpecification(
                        SampleLocations.HONGKONG,
                        SampleLocations.DALLAS,
                        DateUtil.toDate("2009-03-01"));

        assertFalse(routeSpecification.isSatisfiedBy(itinerary));
    }

    @Test
    public void testIsNotSatisfiedByMissedDeadline() {
        RouteSpecification routeSpecification =
                new RouteSpecification(
                        SampleLocations.HONGKONG,
                        SampleLocations.CHICAGO,
                        DateUtil.toDate("2009-02-15"));

        assertFalse(routeSpecification.isSatisfiedBy(itinerary));
    }
}
