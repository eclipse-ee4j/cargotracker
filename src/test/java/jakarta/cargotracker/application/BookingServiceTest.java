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
package jakarta.cargotracker.application;

import jakarta.cargotracker.application.internal.DefaultBookingService;
import jakarta.cargotracker.application.util.DateUtil;
import jakarta.cargotracker.application.util.JsonMoxyConfigurationContextResolver;
import jakarta.cargotracker.domain.model.cargo.Cargo;
import jakarta.cargotracker.domain.model.cargo.CargoRepository;
import jakarta.cargotracker.domain.model.cargo.Delivery;
import jakarta.cargotracker.domain.model.cargo.HandlingActivity;
import jakarta.cargotracker.domain.model.cargo.Itinerary;
import jakarta.cargotracker.domain.model.cargo.Leg;
import jakarta.cargotracker.domain.model.cargo.RouteSpecification;
import jakarta.cargotracker.domain.model.cargo.RoutingStatus;
import jakarta.cargotracker.domain.model.cargo.TrackingId;
import jakarta.cargotracker.domain.model.cargo.TransportStatus;
import jakarta.cargotracker.domain.model.handling.CannotCreateHandlingEventException;
import jakarta.cargotracker.domain.model.handling.HandlingEvent;
import jakarta.cargotracker.domain.model.handling.HandlingEventFactory;
import jakarta.cargotracker.domain.model.handling.HandlingEventRepository;
import jakarta.cargotracker.domain.model.handling.HandlingHistory;
import jakarta.cargotracker.domain.model.handling.UnknownCargoException;
import jakarta.cargotracker.domain.model.handling.UnknownLocationException;
import jakarta.cargotracker.domain.model.handling.UnknownVoyageException;
import jakarta.cargotracker.domain.model.location.Location;
import jakarta.cargotracker.domain.model.location.LocationRepository;
import jakarta.cargotracker.domain.model.location.SampleLocations;
import jakarta.cargotracker.domain.model.location.UnLocode;
import jakarta.cargotracker.domain.model.voyage.CarrierMovement;
import jakarta.cargotracker.domain.model.voyage.SampleVoyages;
import jakarta.cargotracker.domain.model.voyage.Schedule;
import jakarta.cargotracker.domain.model.voyage.Voyage;
import jakarta.cargotracker.domain.model.voyage.VoyageNumber;
import jakarta.cargotracker.domain.model.voyage.VoyageRepository;
import jakarta.cargotracker.domain.service.RoutingService;
import jakarta.cargotracker.domain.shared.AbstractSpecification;
import jakarta.cargotracker.domain.shared.AndSpecification;
import jakarta.cargotracker.domain.shared.DomainObjectUtils;
import jakarta.cargotracker.domain.shared.NotSpecification;
import jakarta.cargotracker.domain.shared.OrSpecification;
import jakarta.cargotracker.domain.shared.Specification;
import jakarta.cargotracker.infrastructure.persistence.jpa.JpaCargoRepository;
import jakarta.cargotracker.infrastructure.persistence.jpa.JpaHandlingEventRepository;
import jakarta.cargotracker.infrastructure.persistence.jpa.JpaLocationRepository;
import jakarta.cargotracker.infrastructure.persistence.jpa.JpaVoyageRepository;
import jakarta.cargotracker.infrastructure.routing.ExternalRoutingService;
import jakarta.pathfinder.api.GraphTraversalService;
import jakarta.pathfinder.api.TransitEdge;
import jakarta.pathfinder.api.TransitPath;
import jakarta.pathfinder.internal.GraphDao;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.validator.cdi.internal.interceptor.ValidationInterceptor;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Application layer integration test covering a number of otherwise fairly
 * trivial components that largely do not wararnt their own tests.
 *
 * @author Reza
 */
@RunWith(Arquillian.class)
public class BookingServiceTest {

    @Inject
    private BookingService bookingService;
    @PersistenceContext
    private EntityManager entityManager;

    private static TrackingId trackingId;
    private static List<Itinerary> candidates;
    private static Date deadline;
    private static Itinerary assigned;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "cargo-tracker-test.war")
                // Application layer component directly under test.
                .addClass(BookingService.class)
                // Domain layer components.
                .addClass(TrackingId.class)
                .addClass(UnLocode.class)
                .addClass(Itinerary.class)
                .addClass(Leg.class)
                .addClass(Voyage.class)
                .addClass(VoyageNumber.class)
                .addClass(Schedule.class)
                .addClass(CarrierMovement.class)
                .addClass(Location.class)
                .addClass(HandlingEvent.class)
                .addClass(Cargo.class)
                .addClass(RouteSpecification.class)
                .addClass(AbstractSpecification.class)
                .addClass(Specification.class)
                .addClass(AndSpecification.class)
                .addClass(OrSpecification.class)
                .addClass(NotSpecification.class)
                .addClass(Delivery.class)
                .addClass(TransportStatus.class)
                .addClass(HandlingActivity.class)
                .addClass(RoutingStatus.class)
                .addClass(HandlingHistory.class)
                .addClass(DomainObjectUtils.class)
                .addClass(CargoRepository.class)
                .addClass(LocationRepository.class)
                .addClass(VoyageRepository.class)
                .addClass(HandlingEventRepository.class)
                .addClass(HandlingEventFactory.class)
                .addClass(CannotCreateHandlingEventException.class)
                .addClass(UnknownCargoException.class)
                .addClass(UnknownVoyageException.class)
                .addClass(UnknownLocationException.class)
                .addClass(RoutingService.class)
                // Application layer components
                .addClass(DefaultBookingService.class)
                // Infrastructure layer components.
                .addClass(JpaCargoRepository.class)
                .addClass(JpaVoyageRepository.class)
                .addClass(JpaHandlingEventRepository.class)
                .addClass(JpaLocationRepository.class)
                .addClass(ExternalRoutingService.class)
                .addClass(JsonMoxyConfigurationContextResolver.class)
                // Interface components
                .addClass(TransitPath.class)
                .addClass(TransitEdge.class)
                // Third-party system simulator
                .addClass(GraphTraversalService.class)
                .addClass(GraphDao.class)
                // Sample data.
                .addClass(BookingServiceTestDataGenerator.class)
                .addClass(SampleLocations.class)
                .addClass(SampleVoyages.class)
                .addClass(DateUtil.class)
                .addClass(BookingServiceTestRestConfiguration.class)
                .addClass(ValidationInterceptor.class)
                .addAsResource("META-INF/persistence.xml",
                        "META-INF/persistence.xml");

        // The web.xml is slightly different for weblogic.
        if (System.getProperty("profileId").equals("weblogic")) {
            war.addAsWebInfResource("test-web-weblogic.xml", "web.xml");
        } else {
            war.addAsWebInfResource("test-web.xml", "web.xml");
        }

        war.addAsLibraries(
                Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.apache.commons:commons-lang3")
                .withTransitivity().asFile());

        return war;
    }

    @Test
    @InSequence(1)
    public void testRegisterNew() {
        UnLocode fromUnlocode = new UnLocode("USCHI");
        UnLocode toUnlocode = new UnLocode("SESTO");

        deadline = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(deadline);
        calendar.add(Calendar.MONTH, 6); // Six months ahead.
        deadline.setTime(calendar.getTime().getTime());

        trackingId = bookingService.bookNewCargo(fromUnlocode, toUnlocode,
                deadline);

        Cargo cargo = entityManager
                .createNamedQuery("Cargo.findByTrackingId", Cargo.class)
                .setParameter("trackingId", trackingId).getSingleResult();

        assertEquals(SampleLocations.CHICAGO, cargo.getOrigin());
        assertEquals(SampleLocations.STOCKHOLM, cargo.getRouteSpecification()
                .getDestination());
        assertTrue(DateUtils.isSameDay(deadline, cargo.getRouteSpecification()
                .getArrivalDeadline()));
        assertEquals(TransportStatus.NOT_RECEIVED, cargo.getDelivery()
                .getTransportStatus());
        assertEquals(Location.UNKNOWN, cargo.getDelivery()
                .getLastKnownLocation());
        assertEquals(Voyage.NONE, cargo.getDelivery().getCurrentVoyage());
        assertFalse(cargo.getDelivery().isMisdirected());
        assertEquals(Delivery.ETA_UNKOWN, cargo.getDelivery()
                .getEstimatedTimeOfArrival());
        assertEquals(Delivery.NO_ACTIVITY, cargo.getDelivery()
                .getNextExpectedActivity());
        assertFalse(cargo.getDelivery().isUnloadedAtDestination());
        assertEquals(RoutingStatus.NOT_ROUTED, cargo.getDelivery()
                .getRoutingStatus());
        assertEquals(Itinerary.EMPTY_ITINERARY, cargo.getItinerary());
    }

    @Test
    @InSequence(2)
    public void testRouteCandidates() {
        candidates = bookingService.requestPossibleRoutesForCargo(trackingId);

        assertFalse(candidates.isEmpty());
    }

    @Test
    @InSequence(3)
    public void testAssignRoute() {
        assigned = candidates.get(new Random().nextInt(candidates
                .size()));

        bookingService.assignCargoToRoute(assigned, trackingId);

        Cargo cargo = entityManager
                .createNamedQuery("Cargo.findByTrackingId", Cargo.class)
                .setParameter("trackingId", trackingId).getSingleResult();

        assertEquals(assigned, cargo.getItinerary());
        assertEquals(TransportStatus.NOT_RECEIVED, cargo.getDelivery()
                .getTransportStatus());
        assertEquals(Location.UNKNOWN, cargo.getDelivery()
                .getLastKnownLocation());
        assertEquals(Voyage.NONE, cargo.getDelivery().getCurrentVoyage());
        assertFalse(cargo.getDelivery().isMisdirected());
        assertTrue(cargo.getDelivery().getEstimatedTimeOfArrival()
                .before(deadline));
        assertEquals(HandlingEvent.Type.RECEIVE, cargo.getDelivery()
                .getNextExpectedActivity().getType());
        assertEquals(SampleLocations.CHICAGO, cargo.getDelivery()
                .getNextExpectedActivity().getLocation());
        assertEquals(null, cargo.getDelivery().getNextExpectedActivity()
                .getVoyage());
        assertFalse(cargo.getDelivery().isUnloadedAtDestination());
        assertEquals(RoutingStatus.ROUTED, cargo.getDelivery()
                .getRoutingStatus());
    }

    @Test
    @InSequence(4)
    public void testChangeDestination() {
        bookingService.changeDestination(trackingId, new UnLocode("FIHEL"));

        Cargo cargo = entityManager
                .createNamedQuery("Cargo.findByTrackingId", Cargo.class)
                .setParameter("trackingId", trackingId).getSingleResult();

        assertEquals(SampleLocations.CHICAGO, cargo.getOrigin());
        assertEquals(SampleLocations.HELSINKI, cargo.getRouteSpecification()
                .getDestination());
        assertTrue(DateUtils.isSameDay(deadline, cargo.getRouteSpecification()
                .getArrivalDeadline()));
        assertEquals(assigned, cargo.getItinerary());
        assertEquals(TransportStatus.NOT_RECEIVED, cargo.getDelivery()
                .getTransportStatus());
        assertEquals(Location.UNKNOWN, cargo.getDelivery()
                .getLastKnownLocation());
        assertEquals(Voyage.NONE, cargo.getDelivery().getCurrentVoyage());
        assertFalse(cargo.getDelivery().isMisdirected());
        assertEquals(Delivery.ETA_UNKOWN, cargo.getDelivery()
                .getEstimatedTimeOfArrival());
        assertEquals(Delivery.NO_ACTIVITY, cargo.getDelivery()
                .getNextExpectedActivity());
        assertFalse(cargo.getDelivery().isUnloadedAtDestination());
        assertEquals(RoutingStatus.MISROUTED, cargo.getDelivery()
                .getRoutingStatus());
    }
}
