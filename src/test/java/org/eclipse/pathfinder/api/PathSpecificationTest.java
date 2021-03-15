package org.eclipse.pathfinder.api;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.eclipse.cargotracker.application.BookingService;
import org.eclipse.cargotracker.application.BookingServiceTestDataGenerator;
import org.eclipse.cargotracker.application.internal.DefaultBookingService;
import org.eclipse.cargotracker.application.util.DateConverter;
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
import org.eclipse.pathfinder.internal.GraphDao;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PathSpecificationTest {

  @Inject Validator validator;

  @Deployment
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "cargo-tracker-test.war")
        // component directly under test.
        .addClass(PathSpecification.class)
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
        .addClass(BookingService.class)
        .addClass(DefaultBookingService.class)
        .addClass(DateConverter.class)
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
  public void testValidSpec() {
    PathSpecification spec = new PathSpecification();
    spec.setOriginUnLocode("USCHI");
    spec.setDestinationUnLocode("SESTO");

    Set<ConstraintViolation<PathSpecification>> violations = validator.validate(spec);

    assertEquals(0, violations.size());
  }

  @Test
  @InSequence(2)
  public void testSameLocationSpec() {
    PathSpecification spec = new PathSpecification();
    spec.setOriginUnLocode("USCHI");
    spec.setDestinationUnLocode("USCHI");

    Set<ConstraintViolation<PathSpecification>> violations = validator.validate(spec);

    assertEquals(1, violations.size());

    ConstraintViolation violation = violations.iterator().next();
    assertEquals(
        "Orign UN location code and destination one must not be the same.", violation.getMessage());
  }
}
