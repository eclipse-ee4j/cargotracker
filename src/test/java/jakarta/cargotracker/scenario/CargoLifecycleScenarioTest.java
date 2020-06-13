package jakarta.cargotracker.scenario;

import jakarta.cargotracker.application.ApplicationEvents;
import jakarta.cargotracker.application.BookingService;
import jakarta.cargotracker.application.CargoInspectionService;
import jakarta.cargotracker.application.HandlingEventService;
import jakarta.cargotracker.application.util.DateUtil;
import jakarta.cargotracker.domain.model.cargo.*;
import jakarta.cargotracker.domain.model.handling.CannotCreateHandlingEventException;
import jakarta.cargotracker.domain.model.handling.HandlingEvent;
import jakarta.cargotracker.domain.model.handling.HandlingEventFactory;
import jakarta.cargotracker.domain.model.handling.HandlingEventRepository;
import jakarta.cargotracker.domain.model.location.Location;
import jakarta.cargotracker.domain.model.location.LocationRepository;
import jakarta.cargotracker.domain.model.location.SampleLocations;
import jakarta.cargotracker.domain.model.location.UnLocode;
import jakarta.cargotracker.domain.model.voyage.SampleVoyages;
import jakarta.cargotracker.domain.model.voyage.Voyage;
import jakarta.cargotracker.domain.model.voyage.VoyageNumber;
import jakarta.cargotracker.domain.model.voyage.VoyageRepository;
import jakarta.cargotracker.domain.service.RoutingService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CargoLifecycleScenarioTest {

    /**
     * Repository implementations are part of the infrastructure layer, which in
     * this test is stubbed out by in-memory replacements.
     */
    HandlingEventRepository handlingEventRepository;
    CargoRepository cargoRepository;
    LocationRepository locationRepository;
    VoyageRepository voyageRepository;
    /**
     * This interface is part of the application layer, and defines a number of
     * events that occur during aplication execution. It is used for
     * message-driving and is implemented using JMS.
     * <p>
     * In this test it is stubbed with synchronous calls.
     */
    ApplicationEvents applicationEvents;
    /**
     * These three components all belong to the application layer, and map
     * against use cases of the application. The "real" implementations are used
     * in this lifecycle test, but wired with stubbed infrastructure.
     */
    BookingService bookingService;
    HandlingEventService handlingEventService;
    CargoInspectionService cargoInspectionService;
    /**
     * This factory is part of the handling aggregate and belongs to the domain
     * layer. Similar to the application layer components, the "real"
     * implementation is used here too, wired with stubbed infrastructure.
     */
    HandlingEventFactory handlingEventFactory;
    /**
     * This is a domain service interface, whose implementation is part of the
     * infrastructure layer (remote call to external system).
     * <p>
     * It is stubbed in this test.
     */
    RoutingService routingService;

    public void testCargoFromHongkongToStockholm() throws Exception {
        /* Test setup: A cargo should be shipped from Hongkong to SampleLocations.STOCKHOLM,
         and it should arrive in no more than two weeks. */
        Location origin = SampleLocations.HONGKONG;
        Location destination = SampleLocations.STOCKHOLM;
        Date arrivalDeadline = DateUtil.toDate("2009-03-18");

        /* Use case 1: booking

         A new cargo is booked, and the unique tracking id is assigned to the cargo. */
        TrackingId trackingId = bookingService.bookNewCargo(
                origin.getUnLocode(), destination.getUnLocode(), arrivalDeadline);

        /* The tracking id can be used to lookup the cargo in the repository.

         Important: The cargo, and thus the domain model, is responsible for determining
         the status of the cargo, whether it is on the right track or not and so on.
         This is core domain logic.

         Tracking the cargo basically amounts to presenting information extracted from
         the cargo aggregate in a suitable way. */
        Cargo cargo = cargoRepository.find(trackingId);
        org.junit.Assert.assertNotNull(cargo);
        org.junit.Assert.assertEquals(TransportStatus.NOT_RECEIVED,
                cargo.getDelivery().getTransportStatus());
        org.junit.Assert.assertEquals(RoutingStatus.NOT_ROUTED,
                cargo.getDelivery().getRoutingStatus());
        org.junit.Assert.assertFalse(cargo.getDelivery().isMisdirected());
        org.junit.Assert.assertNull(cargo.getDelivery().getEstimatedTimeOfArrival());
        org.junit.Assert.assertNull(cargo.getDelivery().getNextExpectedActivity());

        /* Use case 2: routing

         A number of possible routes for this cargo is requested and may be
         presented to the customer in some way for him/her to choose from.
         Selection could be affected by things like price and time of delivery,
         but this test simply uses an arbitrary selection to mimic that process.

         The cargo is then assigned to the selected route, described by an itinerary. */
        List<Itinerary> itineraries
                = bookingService.requestPossibleRoutesForCargo(trackingId);
        Itinerary itinerary = selectPreferedItinerary(itineraries);
        cargo.assignToRoute(itinerary);

        org.junit.Assert.assertEquals(TransportStatus.NOT_RECEIVED,
                cargo.getDelivery().getTransportStatus());
        org.junit.Assert.assertEquals(RoutingStatus.ROUTED,
                cargo.getDelivery().getRoutingStatus());
        org.junit.Assert.assertNotNull(cargo.getDelivery().getEstimatedTimeOfArrival());
        org.junit.Assert.assertEquals(new HandlingActivity(
                        HandlingEvent.Type.RECEIVE, SampleLocations.HONGKONG),
                cargo.getDelivery().getNextExpectedActivity());

        /*
         Use case 3: handling

         A handling event registration attempt will be formed from parsing
         the data coming in as a handling report either via
         the web service interface or as an uploaded CSV file.

         The handling event factory tries to create a HandlingEvent from the attempt,
         and if the factory decides that this is a plausible handling event, it is stored.
         If the attempt is invalid, for example if no cargo exists for the specfied tracking id,
         the attempt is rejected.

         Handling begins: cargo is received in Hongkong.
         */
        handlingEventService.registerHandlingEvent(
                DateUtil.toDate("2009-03-01"), trackingId, null,
                SampleLocations.HONGKONG.getUnLocode(),
                HandlingEvent.Type.RECEIVE);

        org.junit.Assert.assertEquals(TransportStatus.IN_PORT,
                cargo.getDelivery().getTransportStatus());
        org.junit.Assert.assertEquals(SampleLocations.HONGKONG,
                cargo.getDelivery().getLastKnownLocation());

        // Next event: Load onto voyage SampleVoyages.CM003 in Hongkong
        handlingEventService.registerHandlingEvent(
                DateUtil.toDate("2009-03-03"), trackingId,
                SampleVoyages.v100.getVoyageNumber(),
                SampleLocations.HONGKONG.getUnLocode(), HandlingEvent.Type.LOAD);

        // Check current state - should be ok
        org.junit.Assert.assertEquals(SampleVoyages.v100,
                cargo.getDelivery().getCurrentVoyage());
        org.junit.Assert.assertEquals(SampleLocations.HONGKONG,
                cargo.getDelivery().getLastKnownLocation());
        org.junit.Assert.assertEquals(TransportStatus.ONBOARD_CARRIER,
                cargo.getDelivery().getTransportStatus());
        org.junit.Assert.assertFalse(cargo.getDelivery().isMisdirected());
        org.junit.Assert.assertEquals(new HandlingActivity(
                HandlingEvent.Type.UNLOAD, SampleLocations.NEWYORK,
                SampleVoyages.v100), cargo.getDelivery().getNextExpectedActivity());

        /*
         Here's an attempt to register a handling event that's not valid
         because there is no voyage with the specified voyage number,
         and there's no location with the specified UN Locode either.

         This attempt will be rejected and will not affect the cargo delivery in any way.
         */
        VoyageNumber noSuchVoyageNumber = new VoyageNumber("XX000");
        UnLocode noSuchUnLocode = new UnLocode("ZZZZZ");
        try {
            handlingEventService.registerHandlingEvent(
                    DateUtil.toDate("2009-03-05"), trackingId,
                    noSuchVoyageNumber, noSuchUnLocode, HandlingEvent.Type.LOAD);
            org.junit.Assert.fail(
                    "Should not be able to register a handling event with invalid location and voyage");
        } catch (CannotCreateHandlingEventException expected) {
        }

        // Cargo is now (incorrectly) unloaded in Tokyo
        handlingEventService.registerHandlingEvent(
                DateUtil.toDate("2009-03-05"), trackingId,
                SampleVoyages.v100.getVoyageNumber(),
                SampleLocations.TOKYO.getUnLocode(), HandlingEvent.Type.UNLOAD);

        // Check current state - cargo is misdirected!
        org.junit.Assert.assertEquals(Voyage.NONE,
                cargo.getDelivery().getCurrentVoyage());
        org.junit.Assert.assertEquals(SampleLocations.TOKYO,
                cargo.getDelivery().getLastKnownLocation());
        org.junit.Assert.assertEquals(TransportStatus.IN_PORT,
                cargo.getDelivery().getTransportStatus());
        org.junit.Assert.assertTrue(cargo.getDelivery().isMisdirected());
        org.junit.Assert.assertNull(cargo.getDelivery().getNextExpectedActivity());

        // -- Cargo needs to be rerouted --
        // TODO cleaner reroute from "earliest location from where the new route originates"
        // Specify a new route, this time from Tokyo (where it was incorrectly unloaded) to SampleLocations.STOCKHOLM
        RouteSpecification fromTokyo = new RouteSpecification(
                SampleLocations.TOKYO, SampleLocations.STOCKHOLM, arrivalDeadline);
        cargo.specifyNewRoute(fromTokyo);

        // The old itinerary does not satisfy the new specification
        org.junit.Assert.assertEquals(RoutingStatus.MISROUTED,
                cargo.getDelivery().getRoutingStatus());
        org.junit.Assert.assertNull(cargo.getDelivery().getNextExpectedActivity());

        // Repeat procedure of selecting one out of a number of possible routes satisfying the route spec
        List<Itinerary> newItineraries
                = bookingService.requestPossibleRoutesForCargo(cargo.getTrackingId());
        Itinerary newItinerary = selectPreferedItinerary(newItineraries);
        cargo.assignToRoute(newItinerary);

        // New itinerary should satisfy new route
        org.junit.Assert.assertEquals(RoutingStatus.ROUTED, cargo.getDelivery().getRoutingStatus());

        // TODO we can't handle the face that after a reroute, the cargo isn't misdirected anymore
        //org.junit.Assert.assertFalse(cargo.isMisdirected());
        //org.junit.Assert.assertEquals(new HandlingActivity(HandlingEvent.Type.LOAD, SampleLocations.TOKYO), cargo.getNextExpectedActivity());
        // -- Cargo has been rerouted, shipping continues --
        // Load in Tokyo
        handlingEventService.registerHandlingEvent(
                DateUtil.toDate("2009-03-08"), trackingId,
                SampleVoyages.v300.getVoyageNumber(),
                SampleLocations.TOKYO.getUnLocode(), HandlingEvent.Type.LOAD);

        // Check current state - should be ok
        org.junit.Assert.assertEquals(SampleVoyages.v300,
                cargo.getDelivery().getCurrentVoyage());
        org.junit.Assert.assertEquals(SampleLocations.TOKYO,
                cargo.getDelivery().getLastKnownLocation());
        org.junit.Assert.assertEquals(TransportStatus.ONBOARD_CARRIER,
                cargo.getDelivery().getTransportStatus());
        org.junit.Assert.assertFalse(cargo.getDelivery().isMisdirected());
        org.junit.Assert.assertEquals(new HandlingActivity(
                HandlingEvent.Type.UNLOAD, SampleLocations.HAMBURG,
                SampleVoyages.v300), cargo.getDelivery().getNextExpectedActivity());

        // Unload in Hamburg
        handlingEventService.registerHandlingEvent(
                DateUtil.toDate("2009-03-12"), trackingId,
                SampleVoyages.v300.getVoyageNumber(),
                SampleLocations.HAMBURG.getUnLocode(), HandlingEvent.Type.UNLOAD);

        // Check current state - should be ok
        org.junit.Assert.assertEquals(Voyage.NONE,
                cargo.getDelivery().getCurrentVoyage());
        org.junit.Assert.assertEquals(SampleLocations.HAMBURG,
                cargo.getDelivery().getLastKnownLocation());
        org.junit.Assert.assertEquals(TransportStatus.IN_PORT,
                cargo.getDelivery().getTransportStatus());
        org.junit.Assert.assertFalse(cargo.getDelivery().isMisdirected());
        org.junit.Assert.assertEquals(new HandlingActivity(
                HandlingEvent.Type.LOAD, SampleLocations.HAMBURG,
                SampleVoyages.v400), cargo.getDelivery().getNextExpectedActivity());

        // Load in Hamburg
        handlingEventService.registerHandlingEvent(
                DateUtil.toDate("2009-03-14"), trackingId,
                SampleVoyages.v400.getVoyageNumber(),
                SampleLocations.HAMBURG.getUnLocode(), HandlingEvent.Type.LOAD);

        // Check current state - should be ok
        org.junit.Assert.assertEquals(SampleVoyages.v400,
                cargo.getDelivery().getCurrentVoyage());
        org.junit.Assert.assertEquals(SampleLocations.HAMBURG,
                cargo.getDelivery().getLastKnownLocation());
        org.junit.Assert.assertEquals(TransportStatus.ONBOARD_CARRIER,
                cargo.getDelivery().getTransportStatus());
        org.junit.Assert.assertFalse(cargo.getDelivery().isMisdirected());
        org.junit.Assert.assertEquals(new HandlingActivity(
                HandlingEvent.Type.UNLOAD, SampleLocations.STOCKHOLM,
                SampleVoyages.v400), cargo.getDelivery().getNextExpectedActivity());

        // Unload in SampleLocations.STOCKHOLM
        handlingEventService.registerHandlingEvent(
                DateUtil.toDate("2009-03-15"), trackingId,
                SampleVoyages.v400.getVoyageNumber(),
                SampleLocations.STOCKHOLM.getUnLocode(), HandlingEvent.Type.UNLOAD);

        // Check current state - should be ok
        org.junit.Assert.assertEquals(Voyage.NONE,
                cargo.getDelivery().getCurrentVoyage());
        org.junit.Assert.assertEquals(SampleLocations.STOCKHOLM,
                cargo.getDelivery().getLastKnownLocation());
        org.junit.Assert.assertEquals(TransportStatus.IN_PORT,
                cargo.getDelivery().getTransportStatus());
        org.junit.Assert.assertFalse(cargo.getDelivery().isMisdirected());
        org.junit.Assert.assertEquals(new HandlingActivity(
                        HandlingEvent.Type.CLAIM, SampleLocations.STOCKHOLM),
                cargo.getDelivery().getNextExpectedActivity());

        // Finally, cargo is claimed in SampleLocations.STOCKHOLM. This ends the cargo lifecycle from our perspective.
        handlingEventService.registerHandlingEvent(
                DateUtil.toDate("2009-03-16"), trackingId, null,
                SampleLocations.STOCKHOLM.getUnLocode(), HandlingEvent.Type.CLAIM);

        // Check current state - should be ok
        org.junit.Assert.assertEquals(Voyage.NONE,
                cargo.getDelivery().getCurrentVoyage());
        org.junit.Assert.assertEquals(SampleLocations.STOCKHOLM,
                cargo.getDelivery().getLastKnownLocation());
        org.junit.Assert.assertEquals(TransportStatus.CLAIMED,
                cargo.getDelivery().getTransportStatus());
        org.junit.Assert.assertFalse(cargo.getDelivery().isMisdirected());
        org.junit.Assert.assertNull(cargo.getDelivery().getNextExpectedActivity());
    }


    /*
     * Utility stubs below.
     */
    private Itinerary selectPreferedItinerary(List<Itinerary> itineraries) {
        return itineraries.get(0);
    }

    protected void setUp() throws Exception {
        routingService = new RoutingService() {
            public List<Itinerary> fetchRoutesForSpecification(
                    RouteSpecification routeSpecification) {
                if (routeSpecification.getOrigin().equals(SampleLocations.HONGKONG)) {
                    // Hongkong - NYC - Chicago - SampleLocations.STOCKHOLM, initial routing
                    return Arrays.asList(new Itinerary(Arrays.asList(new Leg(
                                    SampleVoyages.v100, SampleLocations.HONGKONG,
                                    SampleLocations.NEWYORK,
                                    DateUtil.toDate("2009-03-03"),
                                    DateUtil.toDate("2009-03-09")),
                            new Leg(SampleVoyages.v200,
                                    SampleLocations.NEWYORK,
                                    SampleLocations.CHICAGO,
                                    DateUtil.toDate("2009-03-10"),
                                    DateUtil.toDate("2009-03-14")),
                            new Leg(SampleVoyages.v200,
                                    SampleLocations.CHICAGO,
                                    SampleLocations.STOCKHOLM,
                                    DateUtil.toDate("2009-03-07"),
                                    DateUtil.toDate("2009-03-11")))));
                } else {
                    // Tokyo - Hamburg - SampleLocations.STOCKHOLM, rerouting misdirected cargo from Tokyo
                    return Arrays.asList(new Itinerary(Arrays.asList(new Leg(
                            SampleVoyages.v300, SampleLocations.TOKYO,
                            SampleLocations.HAMBURG,
                            DateUtil.toDate("2009-03-08"),
                            DateUtil.toDate("2009-03-12")), new Leg(
                            SampleVoyages.v400, SampleLocations.HAMBURG,
                            SampleLocations.STOCKHOLM,
                            DateUtil.toDate("2009-03-14"),
                            DateUtil.toDate("2009-03-15")))));
                }
            }
        };

//        applicationEvents = new SynchronousApplicationEventsStub();
        // In-memory implementations of the repositories
//        handlingEventRepository = new HandlingEventRepositoryInMem();
//        cargoRepository = new CargoRepositoryInMem();
//        locationRepository = new LocationRepositoryInMem();
//        voyageRepository = new VoyageRepositoryInMem();
        // Actual factories and application services, wired with stubbed or in-memory infrastructure
//        handlingEventFactory = new HandlingEventFactory(cargoRepository, voyageRepository, locationRepository);
//        cargoInspectionService = new CargoInspectionServiceImpl(applicationEvents, cargoRepository, handlingEventRepository);
//        handlingEventService = new DefaultHandlingEventService(handlingEventRepository, applicationEvents, handlingEventFactory);
//        bookingService = new BookingServiceImpl(cargoRepository, locationRepository, routingService);
        // Circular dependency when doing synchrounous calls
//        ((SynchronousApplicationEventsStub) applicationEvents).setCargoInspectionService(cargoInspectionService);
    }
}
