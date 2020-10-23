package org.eclipse.cargotracker.domain.model.cargo;

import org.eclipse.cargotracker.application.util.DateUtil;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.handling.HandlingHistory;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.SampleLocations;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class CargoTest {

    private final List<HandlingEvent> events = new ArrayList<>();
    private final Voyage voyage = new Voyage.Builder(new VoyageNumber("0123"),
            SampleLocations.STOCKHOLM)
            .addMovement(SampleLocations.HAMBURG, LocalDateTime.now(), LocalDateTime.now())
            .addMovement(SampleLocations.HONGKONG, LocalDateTime.now(), LocalDateTime.now())
            .addMovement(SampleLocations.MELBOURNE, LocalDateTime.now(), LocalDateTime.now())
            .build();

    @Test
    public void testConstruction() {
        TrackingId trackingId = new TrackingId("XYZ");
        LocalDateTime arrivalDeadline = DateUtil.toDate("2009-03-13");
        RouteSpecification routeSpecification = new RouteSpecification(
                SampleLocations.STOCKHOLM, SampleLocations.MELBOURNE,
                arrivalDeadline);

        Cargo cargo = new Cargo(trackingId, routeSpecification);

        assertEquals(RoutingStatus.NOT_ROUTED, cargo.getDelivery()
                .getRoutingStatus());
        assertEquals(TransportStatus.NOT_RECEIVED, cargo.getDelivery()
                .getTransportStatus());
        Assert.assertEquals(Location.UNKNOWN, cargo.getDelivery()
                .getLastKnownLocation());
        Assert.assertEquals(Voyage.NONE, cargo.getDelivery().getCurrentVoyage());
    }

    @Test
    public void testRoutingStatus() {
        Cargo cargo = new Cargo(new TrackingId("XYZ"), new RouteSpecification(
                SampleLocations.STOCKHOLM, SampleLocations.MELBOURNE,
                LocalDateTime.now()));
        final Itinerary good = new Itinerary();
        Itinerary bad = new Itinerary();
        @SuppressWarnings("serial")
        RouteSpecification acceptOnlyGood = new RouteSpecification(
                cargo.getOrigin(), cargo.getRouteSpecification()
                .getDestination(), LocalDateTime.now()) {

            @Override
            public boolean isSatisfiedBy(Itinerary itinerary) {
                return itinerary == good;
            }
        };

        cargo.specifyNewRoute(acceptOnlyGood);

        assertEquals(RoutingStatus.NOT_ROUTED, cargo.getDelivery()
                .getRoutingStatus());

        cargo.assignToRoute(bad);
        assertEquals(RoutingStatus.MISROUTED, cargo.getDelivery()
                .getRoutingStatus());

        cargo.assignToRoute(good);
        assertEquals(RoutingStatus.ROUTED, cargo.getDelivery()
                .getRoutingStatus());
    }

    @Test
    public void testLastKnownLocationUnknownWhenNoEvents() {
        Cargo cargo = new Cargo(new TrackingId("XYZ"), new RouteSpecification(
                SampleLocations.STOCKHOLM, SampleLocations.MELBOURNE,
                LocalDateTime.now()));

        Assert.assertEquals(Location.UNKNOWN, cargo.getDelivery()
                .getLastKnownLocation());
    }

    @Test
    public void testLastKnownLocationReceived() throws Exception {
        Cargo cargo = populateCargoReceivedStockholm();

        Assert.assertEquals(SampleLocations.STOCKHOLM, cargo.getDelivery()
                .getLastKnownLocation());
    }

    @Test
    public void testLastKnownLocationClaimed() throws Exception {
        Cargo cargo = populateCargoClaimedMelbourne();

        Assert.assertEquals(SampleLocations.MELBOURNE, cargo.getDelivery()
                .getLastKnownLocation());
    }

    @Test
    public void testLastKnownLocationUnloaded() throws Exception {
        Cargo cargo = populateCargoOffHongKong();

        Assert.assertEquals(SampleLocations.HONGKONG, cargo.getDelivery()
                .getLastKnownLocation());
    }

    @Test
    public void testLastKnownLocationloaded() throws Exception {
        Cargo cargo = populateCargoOnHamburg();

        Assert.assertEquals(SampleLocations.HAMBURG, cargo.getDelivery()
                .getLastKnownLocation());
    }

    @Test
    public void testIsUnloadedAtFinalDestination() {
        Cargo cargo = setUpCargoWithItinerary(SampleLocations.HANGZOU,
                SampleLocations.TOKYO, SampleLocations.NEWYORK);
        assertFalse(cargo.getDelivery().isUnloadedAtDestination());

        // Adding an event unrelated to unloading at final destination
        events.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 10, zoneOffset()),
                LocalDateTime.now(),
                HandlingEvent.Type.RECEIVE, SampleLocations.HANGZOU));
        cargo.deriveDeliveryProgress(new HandlingHistory(events));
        assertFalse(cargo.getDelivery().isUnloadedAtDestination());

        Voyage voyage = new Voyage.Builder(new VoyageNumber("0123"),
                SampleLocations.HANGZOU).addMovement(SampleLocations.NEWYORK,
                LocalDateTime.now(), LocalDateTime.now()).build();

        // Adding an unload event, but not at the final destination
        events.add(new HandlingEvent(cargo, LocalDateTime
                .ofEpochSecond(0, 20, zoneOffset()),
                LocalDateTime.now(),
                HandlingEvent.Type.UNLOAD, SampleLocations.TOKYO, voyage));
        cargo.deriveDeliveryProgress(new HandlingHistory(events));
        assertFalse(cargo.getDelivery().isUnloadedAtDestination());

        // Adding an event in the final destination, but not unload
        events.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 30, zoneOffset()),
                LocalDateTime.now(),
                HandlingEvent.Type.CUSTOMS, SampleLocations.NEWYORK));
        cargo.deriveDeliveryProgress(new HandlingHistory(events));
        assertFalse(cargo.getDelivery().isUnloadedAtDestination());

        // Finally, cargo is unloaded at final destination
        events.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 40, zoneOffset()),
                LocalDateTime.now(),
                HandlingEvent.Type.UNLOAD, SampleLocations.NEWYORK, voyage));
        cargo.deriveDeliveryProgress(new HandlingHistory(events));
        assertTrue(cargo.getDelivery().isUnloadedAtDestination());
    }

    // TODO: Generate test data some better way
    private Cargo populateCargoReceivedStockholm() throws Exception {
        Cargo cargo = new Cargo(new TrackingId("XYZ"), new RouteSpecification(
                SampleLocations.STOCKHOLM, SampleLocations.MELBOURNE,
                LocalDateTime.now()));

        HandlingEvent event = new HandlingEvent(cargo, getDate("2007-12-01"),
                LocalDateTime.now(), HandlingEvent.Type.RECEIVE,
                SampleLocations.STOCKHOLM);
        events.add(event);
        cargo.deriveDeliveryProgress(new HandlingHistory(events));

        return cargo;
    }

    private Cargo populateCargoClaimedMelbourne() throws Exception {
        Cargo cargo = populateCargoOffMelbourne();

        events.add(new HandlingEvent(cargo, getDate("2007-12-09"), LocalDateTime.now(),
                HandlingEvent.Type.CLAIM, SampleLocations.MELBOURNE));
        cargo.deriveDeliveryProgress(new HandlingHistory(events));

        return cargo;
    }

    private Cargo populateCargoOffHongKong() throws Exception {
        Cargo cargo = new Cargo(new TrackingId("XYZ"), new RouteSpecification(
                SampleLocations.STOCKHOLM, SampleLocations.MELBOURNE,
                LocalDateTime.now()));

        events.add(new HandlingEvent(cargo, getDate("2007-12-01"), LocalDateTime.now(),
                HandlingEvent.Type.LOAD, SampleLocations.STOCKHOLM, voyage));
        events.add(new HandlingEvent(cargo, getDate("2007-12-02"), LocalDateTime.now(),
                HandlingEvent.Type.UNLOAD, SampleLocations.HAMBURG, voyage));

        events.add(new HandlingEvent(cargo, getDate("2007-12-03"), LocalDateTime.now(),
                HandlingEvent.Type.LOAD, SampleLocations.HAMBURG, voyage));
        events.add(new HandlingEvent(cargo, getDate("2007-12-04"), LocalDateTime.now(),
                HandlingEvent.Type.UNLOAD, SampleLocations.HONGKONG, voyage));

        cargo.deriveDeliveryProgress(new HandlingHistory(events));

        return cargo;
    }

    private Cargo populateCargoOnHamburg() throws Exception {
        Cargo cargo = new Cargo(new TrackingId("XYZ"), new RouteSpecification(
                SampleLocations.STOCKHOLM, SampleLocations.MELBOURNE,
                LocalDateTime.now()));

        events.add(new HandlingEvent(cargo, getDate("2007-12-01"), LocalDateTime.now(),
                HandlingEvent.Type.LOAD, SampleLocations.STOCKHOLM, voyage));
        events.add(new HandlingEvent(cargo, getDate("2007-12-02"), LocalDateTime.now(),
                HandlingEvent.Type.UNLOAD, SampleLocations.HAMBURG, voyage));
        events.add(new HandlingEvent(cargo, getDate("2007-12-03"), LocalDateTime.now(),
                HandlingEvent.Type.LOAD, SampleLocations.HAMBURG, voyage));

        cargo.deriveDeliveryProgress(new HandlingHistory(events));

        return cargo;
    }

    private Cargo populateCargoOffMelbourne() throws Exception {
        Cargo cargo = new Cargo(new TrackingId("XYZ"), new RouteSpecification(
                SampleLocations.STOCKHOLM, SampleLocations.MELBOURNE,
                LocalDateTime.now()));

        events.add(new HandlingEvent(cargo, getDate("2007-12-01"), LocalDateTime.now(),
                HandlingEvent.Type.LOAD, SampleLocations.STOCKHOLM, voyage));
        events.add(new HandlingEvent(cargo, getDate("2007-12-02"), LocalDateTime.now(),
                HandlingEvent.Type.UNLOAD, SampleLocations.HAMBURG, voyage));

        events.add(new HandlingEvent(cargo, getDate("2007-12-03"), LocalDateTime.now(),
                HandlingEvent.Type.LOAD, SampleLocations.HAMBURG, voyage));
        events.add(new HandlingEvent(cargo, getDate("2007-12-04"), LocalDateTime.now(),
                HandlingEvent.Type.UNLOAD, SampleLocations.HONGKONG, voyage));

        events.add(new HandlingEvent(cargo, getDate("2007-12-05"), LocalDateTime.now(),
                HandlingEvent.Type.LOAD, SampleLocations.HONGKONG, voyage));
        events.add(new HandlingEvent(cargo, getDate("2007-12-07"), LocalDateTime.now(),
                HandlingEvent.Type.UNLOAD, SampleLocations.MELBOURNE, voyage));

        cargo.deriveDeliveryProgress(new HandlingHistory(events));

        return cargo;
    }

    @Test
    public void testIsMisdirected() throws Exception {
        // A cargo with no itinerary is not misdirected
        Cargo cargo = new Cargo(new TrackingId("TRKID"),
                new RouteSpecification(SampleLocations.SHANGHAI,
                        SampleLocations.GOTHENBURG, LocalDateTime.now()));
        assertFalse(cargo.getDelivery().isMisdirected());

        cargo = setUpCargoWithItinerary(SampleLocations.SHANGHAI,
                SampleLocations.ROTTERDAM, SampleLocations.GOTHENBURG);

        // A cargo with no handling events is not misdirected
        assertFalse(cargo.getDelivery().isMisdirected());

        Collection<HandlingEvent> handlingEvents = new ArrayList<>();

        // Happy path
        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 10, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 20, zoneOffset()),
                HandlingEvent.Type.RECEIVE, SampleLocations.SHANGHAI));
        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 30, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 40, zoneOffset()),
                HandlingEvent.Type.LOAD, SampleLocations.SHANGHAI, voyage));
        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 50, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 60, zoneOffset()),
                HandlingEvent.Type.UNLOAD, SampleLocations.ROTTERDAM, voyage));
        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 70, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 80, zoneOffset()),
                HandlingEvent.Type.LOAD, SampleLocations.ROTTERDAM, voyage));
        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 90, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 100, zoneOffset()),
                HandlingEvent.Type.UNLOAD,
                SampleLocations.GOTHENBURG, voyage));
        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 110, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 120, zoneOffset()),
                HandlingEvent.Type.CLAIM,
                SampleLocations.GOTHENBURG));
        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 130, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 140, zoneOffset()),
                HandlingEvent.Type.CUSTOMS,
                SampleLocations.GOTHENBURG));

        events.addAll(handlingEvents);
        cargo.deriveDeliveryProgress(new HandlingHistory(events));
        assertFalse(cargo.getDelivery().isMisdirected());

        // Try a couple of failing ones
        cargo = setUpCargoWithItinerary(SampleLocations.SHANGHAI,
                SampleLocations.ROTTERDAM, SampleLocations.GOTHENBURG);
        handlingEvents = new ArrayList<>();

        handlingEvents.add(new HandlingEvent(cargo, LocalDateTime.now(), LocalDateTime.now(),
                HandlingEvent.Type.RECEIVE, SampleLocations.HANGZOU));
        events.addAll(handlingEvents);
        cargo.deriveDeliveryProgress(new HandlingHistory(events));

        assertTrue(cargo.getDelivery().isMisdirected());

        cargo = setUpCargoWithItinerary(SampleLocations.SHANGHAI,
                SampleLocations.ROTTERDAM, SampleLocations.GOTHENBURG);
        handlingEvents = new ArrayList<>();

        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 10, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 20, zoneOffset()),
                HandlingEvent.Type.RECEIVE, SampleLocations.SHANGHAI));
        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 30, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 40, zoneOffset()),
                HandlingEvent.Type.LOAD, SampleLocations.SHANGHAI, voyage));
        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 50, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 60, zoneOffset()),
                HandlingEvent.Type.UNLOAD, SampleLocations.ROTTERDAM, voyage));
        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 70, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 80, zoneOffset()),
                HandlingEvent.Type.LOAD, SampleLocations.ROTTERDAM, voyage));

        events.addAll(handlingEvents);
        cargo.deriveDeliveryProgress(new HandlingHistory(events));

        assertTrue(cargo.getDelivery().isMisdirected());

        cargo = setUpCargoWithItinerary(SampleLocations.SHANGHAI,
                SampleLocations.ROTTERDAM, SampleLocations.GOTHENBURG);
        handlingEvents = new ArrayList<>();

        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 10, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 20, zoneOffset()),
                HandlingEvent.Type.RECEIVE, SampleLocations.SHANGHAI));
        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 30, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 40, zoneOffset()),
                HandlingEvent.Type.LOAD, SampleLocations.SHANGHAI, voyage));
        handlingEvents.add(new HandlingEvent(cargo,
                LocalDateTime
                        .ofEpochSecond(0, 50, zoneOffset()),
                LocalDateTime
                        .ofEpochSecond(0, 60, zoneOffset()),
                HandlingEvent.Type.UNLOAD, SampleLocations.ROTTERDAM, voyage));
        handlingEvents.add(new HandlingEvent(cargo, LocalDateTime.now(), LocalDateTime.now(),
                HandlingEvent.Type.CLAIM, SampleLocations.ROTTERDAM));

        events.addAll(handlingEvents);
        cargo.deriveDeliveryProgress(new HandlingHistory(events));

        assertTrue(cargo.getDelivery().isMisdirected());
    }

    private ZoneOffset zoneOffset() {
        OffsetDateTime odt = OffsetDateTime.now();
        return odt.getOffset();
    }

    private Cargo setUpCargoWithItinerary(Location origin, Location midpoint,
                                          Location destination) {
        Cargo cargo = new Cargo(new TrackingId("CARGO1"),
                new RouteSpecification(origin, destination, LocalDateTime.now()));

        Itinerary itinerary = new Itinerary(Arrays.asList(new Leg(voyage,
                origin, midpoint, LocalDateTime.now(), LocalDateTime.now()), new Leg(voyage,
                midpoint, destination, LocalDateTime.now(), LocalDateTime.now())));

        cargo.assignToRoute(itinerary);
        return cargo;
    }

    /**
     * Parse an ISO 8601 (YYYY-MM-DD) String to Date
     *
     * @param isoFormat String to parse.
     * @return Created date instance.
     * @throws ParseException Thrown if parsing fails.
     */
    private LocalDateTime getDate(String isoFormat) throws ParseException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.from(dateFormat.parse(isoFormat)).atTime(0, 0);
    }
}
