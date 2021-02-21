package org.eclipse.cargotracker.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.cargotracker.application.internal.DefaultBookingService;
import org.eclipse.cargotracker.application.util.DateUtil;
import org.eclipse.cargotracker.application.util.RestConfiguration;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.CargoRepository;
import org.eclipse.cargotracker.domain.model.cargo.Delivery;
import org.eclipse.cargotracker.domain.model.cargo.HandlingActivity;
import org.eclipse.cargotracker.domain.model.cargo.Itinerary;
import org.eclipse.cargotracker.domain.model.cargo.Leg;
import org.eclipse.cargotracker.domain.model.cargo.RouteSpecification;
import org.eclipse.cargotracker.domain.model.cargo.RoutingStatus;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.cargo.TransportStatus;
import org.eclipse.cargotracker.domain.model.handling.CannotCreateHandlingEventException;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.handling.HandlingEventFactory;
import org.eclipse.cargotracker.domain.model.handling.HandlingEventRepository;
import org.eclipse.cargotracker.domain.model.handling.HandlingHistory;
import org.eclipse.cargotracker.domain.model.handling.UnknownCargoException;
import org.eclipse.cargotracker.domain.model.handling.UnknownLocationException;
import org.eclipse.cargotracker.domain.model.handling.UnknownVoyageException;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.LocationRepository;
import org.eclipse.cargotracker.domain.model.location.SampleLocations;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.CarrierMovement;
import org.eclipse.cargotracker.domain.model.voyage.SampleVoyages;
import org.eclipse.cargotracker.domain.model.voyage.Schedule;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.eclipse.cargotracker.domain.model.voyage.VoyageRepository;
import org.eclipse.cargotracker.domain.service.RoutingService;
import org.eclipse.cargotracker.domain.shared.AbstractSpecification;
import org.eclipse.cargotracker.domain.shared.AndSpecification;
import org.eclipse.cargotracker.domain.shared.DomainObjectUtils;
import org.eclipse.cargotracker.domain.shared.NotSpecification;
import org.eclipse.cargotracker.domain.shared.OrSpecification;
import org.eclipse.cargotracker.domain.shared.Specification;
import org.eclipse.cargotracker.infrastructure.logging.LoggerProducer;
import org.eclipse.cargotracker.infrastructure.persistence.jpa.JpaCargoRepository;
import org.eclipse.cargotracker.infrastructure.persistence.jpa.JpaHandlingEventRepository;
import org.eclipse.cargotracker.infrastructure.persistence.jpa.JpaLocationRepository;
import org.eclipse.cargotracker.infrastructure.persistence.jpa.JpaVoyageRepository;
import org.eclipse.cargotracker.infrastructure.routing.ExternalRoutingService;
import org.eclipse.pathfinder.api.GraphTraversalService;
import org.eclipse.pathfinder.api.TransitEdge;
import org.eclipse.pathfinder.api.TransitPath;
import org.eclipse.pathfinder.internal.GraphDao;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Application layer integration test covering a number of otherwise fairly trivial components that
 * largely do not warrant their own tests.
 *
 * <p>Ensure a Payara instance is running locally before this test is executed, with the default
 * user name and password.
 */
@RunWith(Arquillian.class)
public class BookingServiceTest {
  private static TrackingId trackingId;
  private static List<Itinerary> candidates;
  private static LocalDate deadline;
  private static Itinerary assigned;

  @Inject private BookingService bookingService;
  @PersistenceContext private EntityManager entityManager;

  @Deployment
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "cargo-tracker-test.war")
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
        .addClass(DateUtil.class)
        .addClass(RestConfiguration.class)
        // Infrastructure layer components.
        .addClass(JpaCargoRepository.class)
        .addClass(JpaVoyageRepository.class)
        .addClass(JpaHandlingEventRepository.class)
        .addClass(JpaLocationRepository.class)
        .addClass(ExternalRoutingService.class)
        .addClass(LoggerProducer.class)
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
        // Persistence unit descriptor
        .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
        // Web application descriptor
        .addAsWebInfResource("test-web.xml", "web.xml")
        // Library dependencies
        .addAsLibraries(
            Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.apache.commons:commons-lang3", "com.h2database:h2")
                .withTransitivity()
                .asFile());
  }

  @Test
  @InSequence(1)
  public void testRegisterNew() {
    UnLocode fromUnlocode = new UnLocode("USCHI");
    UnLocode toUnlocode = new UnLocode("SESTO");

    deadline = LocalDate.now().plusMonths(6);

    trackingId = bookingService.bookNewCargo(fromUnlocode, toUnlocode, deadline);

    Cargo cargo =
        entityManager
            .createNamedQuery("Cargo.findByTrackingId", Cargo.class)
            .setParameter("trackingId", trackingId)
            .getSingleResult();

    assertEquals(SampleLocations.CHICAGO, cargo.getOrigin());
    assertEquals(SampleLocations.STOCKHOLM, cargo.getRouteSpecification().getDestination());
    assertTrue(deadline.isEqual(cargo.getRouteSpecification().getArrivalDeadline()));
    assertEquals(TransportStatus.NOT_RECEIVED, cargo.getDelivery().getTransportStatus());
    assertEquals(Location.UNKNOWN, cargo.getDelivery().getLastKnownLocation());
    assertEquals(Voyage.NONE, cargo.getDelivery().getCurrentVoyage());
    assertFalse(cargo.getDelivery().isMisdirected());
    assertEquals(Delivery.ETA_UNKOWN, cargo.getDelivery().getEstimatedTimeOfArrival());
    assertEquals(Delivery.NO_ACTIVITY, cargo.getDelivery().getNextExpectedActivity());
    assertFalse(cargo.getDelivery().isUnloadedAtDestination());
    assertEquals(RoutingStatus.NOT_ROUTED, cargo.getDelivery().getRoutingStatus());
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
    assigned = candidates.get(new Random().nextInt(candidates.size()));

    bookingService.assignCargoToRoute(assigned, trackingId);

    Cargo cargo =
        entityManager
            .createNamedQuery("Cargo.findByTrackingId", Cargo.class)
            .setParameter("trackingId", trackingId)
            .getSingleResult();

    assertEquals(assigned, cargo.getItinerary());
    assertEquals(TransportStatus.NOT_RECEIVED, cargo.getDelivery().getTransportStatus());
    assertEquals(Location.UNKNOWN, cargo.getDelivery().getLastKnownLocation());
    assertEquals(Voyage.NONE, cargo.getDelivery().getCurrentVoyage());
    assertFalse(cargo.getDelivery().isMisdirected());
    assertTrue(cargo.getDelivery().getEstimatedTimeOfArrival().isBefore(deadline.atStartOfDay()));
    Assert.assertEquals(
        HandlingEvent.Type.RECEIVE, cargo.getDelivery().getNextExpectedActivity().getType());
    Assert.assertEquals(
        SampleLocations.CHICAGO, cargo.getDelivery().getNextExpectedActivity().getLocation());
    Assert.assertEquals(null, cargo.getDelivery().getNextExpectedActivity().getVoyage());
    assertFalse(cargo.getDelivery().isUnloadedAtDestination());
    assertEquals(RoutingStatus.ROUTED, cargo.getDelivery().getRoutingStatus());
  }

  @Test
  @InSequence(4)
  public void testChangeDestination() {
    bookingService.changeDestination(trackingId, new UnLocode("FIHEL"));

    Cargo cargo =
        entityManager
            .createNamedQuery("Cargo.findByTrackingId", Cargo.class)
            .setParameter("trackingId", trackingId)
            .getSingleResult();

    assertEquals(SampleLocations.CHICAGO, cargo.getOrigin());
    assertEquals(SampleLocations.HELSINKI, cargo.getRouteSpecification().getDestination());
    assertTrue(deadline.isEqual(cargo.getRouteSpecification().getArrivalDeadline()));
    assertEquals(assigned, cargo.getItinerary());
    assertEquals(TransportStatus.NOT_RECEIVED, cargo.getDelivery().getTransportStatus());
    assertEquals(Location.UNKNOWN, cargo.getDelivery().getLastKnownLocation());
    assertEquals(Voyage.NONE, cargo.getDelivery().getCurrentVoyage());
    assertFalse(cargo.getDelivery().isMisdirected());
    assertEquals(Delivery.ETA_UNKOWN, cargo.getDelivery().getEstimatedTimeOfArrival());
    assertEquals(Delivery.NO_ACTIVITY, cargo.getDelivery().getNextExpectedActivity());
    assertFalse(cargo.getDelivery().isUnloadedAtDestination());
    assertEquals(RoutingStatus.MISROUTED, cargo.getDelivery().getRoutingStatus());
  }

  @Test
  @InSequence(5)
  public void testChangeDeadline() {
    LocalDate newDeadline = deadline.plusMonths(1);
    bookingService.changeDeadline(trackingId, newDeadline);

    Cargo cargo =
        entityManager
            .createNamedQuery("Cargo.findByTrackingId", Cargo.class)
            .setParameter("trackingId", trackingId)
            .getSingleResult();

    assertEquals(SampleLocations.CHICAGO, cargo.getOrigin());
    assertEquals(SampleLocations.HELSINKI, cargo.getRouteSpecification().getDestination());
    assertTrue(newDeadline.isEqual(cargo.getRouteSpecification().getArrivalDeadline()));
    assertEquals(assigned, cargo.getItinerary());
    assertEquals(TransportStatus.NOT_RECEIVED, cargo.getDelivery().getTransportStatus());
    assertEquals(Location.UNKNOWN, cargo.getDelivery().getLastKnownLocation());
    assertEquals(Voyage.NONE, cargo.getDelivery().getCurrentVoyage());
    assertFalse(cargo.getDelivery().isMisdirected());
    assertEquals(Delivery.ETA_UNKOWN, cargo.getDelivery().getEstimatedTimeOfArrival());
    assertEquals(Delivery.NO_ACTIVITY, cargo.getDelivery().getNextExpectedActivity());
    assertFalse(cargo.getDelivery().isUnloadedAtDestination());
    assertEquals(RoutingStatus.MISROUTED, cargo.getDelivery().getRoutingStatus());
  }
}
