package org.eclipse.cargotracker.application.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.Itinerary;
import org.eclipse.cargotracker.domain.model.cargo.Leg;
import org.eclipse.cargotracker.domain.model.cargo.RouteSpecification;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.handling.CannotCreateHandlingEventException;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.handling.HandlingEventFactory;
import org.eclipse.cargotracker.domain.model.handling.HandlingEventRepository;
import org.eclipse.cargotracker.domain.model.handling.HandlingHistory;
import org.eclipse.cargotracker.domain.model.location.SampleLocations;
import org.eclipse.cargotracker.domain.model.voyage.SampleVoyages;

/** Loads sample data for demo. */
@Singleton
@Startup
public class SampleDataGenerator {

  @Inject private Logger logger;

  @PersistenceContext private EntityManager entityManager;
  @Inject private HandlingEventFactory handlingEventFactory;
  @Inject private HandlingEventRepository handlingEventRepository;

  @PostConstruct
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void loadSampleData() {
    logger.info("Loading sample data.");
    unLoadAll(); // Fail-safe in case of application restart that does not trigger a JPA schema
    // drop.
    loadSampleLocations();
    loadSampleVoyages();
    loadSampleCargos();
  }

  private void unLoadAll() {
    logger.info("Unloading all existing data.");
    // In order to remove handling events, must remove references in cargo.
    // Dropping cargo first won't work since handling events have references
    // to it.
    // TODO [Clean Code] See if there is a better way to do this.
    List<Cargo> cargos =
        entityManager.createQuery("Select c from Cargo c", Cargo.class).getResultList();
    for (Cargo cargo : cargos) {
      cargo.getDelivery().setLastEvent(null);
      entityManager.merge(cargo);
    }

    // Delete all entities
    // TODO [Clean Code] See why cascade delete is not working.
    entityManager.createQuery("Delete from HandlingEvent").executeUpdate();
    entityManager.createQuery("Delete from Leg").executeUpdate();
    entityManager.createQuery("Delete from Cargo").executeUpdate();
    entityManager.createQuery("Delete from CarrierMovement").executeUpdate();
    entityManager.createQuery("Delete from Voyage").executeUpdate();
    entityManager.createQuery("Delete from Location").executeUpdate();
  }

  private void loadSampleLocations() {
    logger.info("Loading sample locations.");

    entityManager.persist(SampleLocations.HONGKONG);
    entityManager.persist(SampleLocations.MELBOURNE);
    entityManager.persist(SampleLocations.STOCKHOLM);
    entityManager.persist(SampleLocations.HELSINKI);
    entityManager.persist(SampleLocations.CHICAGO);
    entityManager.persist(SampleLocations.TOKYO);
    entityManager.persist(SampleLocations.HAMBURG);
    entityManager.persist(SampleLocations.SHANGHAI);
    entityManager.persist(SampleLocations.ROTTERDAM);
    entityManager.persist(SampleLocations.GOTHENBURG);
    entityManager.persist(SampleLocations.HANGZOU);
    entityManager.persist(SampleLocations.NEWYORK);
    entityManager.persist(SampleLocations.DALLAS);
  }

  private void loadSampleVoyages() {
    logger.info("Loading sample voyages.");

    entityManager.persist(SampleVoyages.HONGKONG_TO_NEW_YORK);
    entityManager.persist(SampleVoyages.NEW_YORK_TO_DALLAS);
    entityManager.persist(SampleVoyages.DALLAS_TO_HELSINKI);
    entityManager.persist(SampleVoyages.HELSINKI_TO_HONGKONG);
    entityManager.persist(SampleVoyages.DALLAS_TO_HELSINKI_ALT);
  }

  private void loadSampleCargos() {
    logger.info("Loading sample cargo data.");

    // Cargo ABC123. This one is en-route.
    TrackingId trackingId1 = new TrackingId("ABC123");

    RouteSpecification routeSpecification1 =
        new RouteSpecification(
            SampleLocations.HONGKONG, SampleLocations.HELSINKI, LocalDate.now().plusDays(15));
    Cargo abc123 = new Cargo(trackingId1, routeSpecification1);

    Itinerary itinerary1 =
        new Itinerary(
            Arrays.asList(
                new Leg(
                    SampleVoyages.HONGKONG_TO_NEW_YORK,
                    SampleLocations.HONGKONG,
                    SampleLocations.NEWYORK,
                    LocalDateTime.now().minusDays(7).minusHours(2).minusMinutes(4),
                    LocalDateTime.now().minusDays(1).minusHours(10).minusMinutes(52)),
                new Leg(
                    SampleVoyages.NEW_YORK_TO_DALLAS,
                    SampleLocations.NEWYORK,
                    SampleLocations.DALLAS,
                    LocalDateTime.now().plusDays(2).minusHours(18).minusMinutes(38),
                    LocalDateTime.now().plusDays(6).minusHours(3).minusMinutes(22)),
                new Leg(
                    SampleVoyages.DALLAS_TO_HELSINKI,
                    SampleLocations.DALLAS,
                    SampleLocations.HELSINKI,
                    LocalDateTime.now().plusDays(8).minusHours(8).minusMinutes(18),
                    LocalDateTime.now().plusDays(14).minusHours(12).minusMinutes(29))));
    abc123.assignToRoute(itinerary1);

    entityManager.persist(abc123);

    try {
      HandlingEvent event1 =
          handlingEventFactory.createHandlingEvent(
              LocalDateTime.now().minusHours(5).minusMinutes(5),
              LocalDateTime.now().minusDays(10).minusHours(9).minusMinutes(11),
              trackingId1,
              null,
              SampleLocations.HONGKONG.getUnLocode(),
              HandlingEvent.Type.RECEIVE);
      entityManager.persist(event1);

      HandlingEvent event2 =
          handlingEventFactory.createHandlingEvent(
              LocalDateTime.now().minusHours(6).minusMinutes(46),
              LocalDateTime.now().minusDays(7).minusHours(2).minusMinutes(50),
              trackingId1,
              SampleVoyages.HONGKONG_TO_NEW_YORK.getVoyageNumber(),
              SampleLocations.HONGKONG.getUnLocode(),
              HandlingEvent.Type.LOAD);
      entityManager.persist(event2);

      HandlingEvent event3 =
          handlingEventFactory.createHandlingEvent(
              LocalDateTime.now().minusHours(15).minusMinutes(26),
              LocalDateTime.now().minusDays(1).minusHours(4).minusMinutes(32),
              trackingId1,
              SampleVoyages.HONGKONG_TO_NEW_YORK.getVoyageNumber(),
              SampleLocations.NEWYORK.getUnLocode(),
              HandlingEvent.Type.UNLOAD);
      entityManager.persist(event3);
    } catch (CannotCreateHandlingEventException e) {
      throw new RuntimeException(e);
    }

    HandlingHistory handlingHistory1 =
        handlingEventRepository.lookupHandlingHistoryOfCargo(trackingId1);
    abc123.deriveDeliveryProgress(handlingHistory1);

    entityManager.persist(abc123);

    // Cargo JKL567. This one was loaded on the wrong voyage.
    TrackingId trackingId2 = new TrackingId("JKL567");

    RouteSpecification routeSpecification2 =
        new RouteSpecification(
            SampleLocations.HANGZOU, SampleLocations.STOCKHOLM, LocalDate.now().plusDays(18));
    Cargo jkl567 = new Cargo(trackingId2, routeSpecification2);

    Itinerary itinerary2 =
        new Itinerary(
            Arrays.asList(
                new Leg(
                    SampleVoyages.HONGKONG_TO_NEW_YORK,
                    SampleLocations.HANGZOU,
                    SampleLocations.NEWYORK,
                    LocalDateTime.now().minusDays(10).minusHours(1).minusMinutes(19),
                    LocalDateTime.now().minusDays(3).minusHours(7).minusMinutes(2)),
                new Leg(
                    SampleVoyages.NEW_YORK_TO_DALLAS,
                    SampleLocations.NEWYORK,
                    SampleLocations.DALLAS,
                    LocalDateTime.now().minusDays(2).minusHours(17).minusMinutes(3),
                    LocalDateTime.now().plusDays(2).minusHours(5).minusMinutes(20)),
                new Leg(
                    SampleVoyages.DALLAS_TO_HELSINKI,
                    SampleLocations.DALLAS,
                    SampleLocations.STOCKHOLM,
                    LocalDateTime.now().plusDays(6).minusHours(11).minusMinutes(38),
                    LocalDateTime.now().plusDays(15).minusHours(4).minusMinutes(1))));
    jkl567.assignToRoute(itinerary2);

    entityManager.persist(jkl567);

    try {
      HandlingEvent event1 =
          handlingEventFactory.createHandlingEvent(
              LocalDateTime.now().minusHours(2).minusMinutes(44),
              LocalDateTime.now().minusDays(15).minusHours(14).minusMinutes(33),
              trackingId2,
              null,
              SampleLocations.HANGZOU.getUnLocode(),
              HandlingEvent.Type.RECEIVE);
      entityManager.persist(event1);

      HandlingEvent event2 =
          handlingEventFactory.createHandlingEvent(
              LocalDateTime.now().minusHours(3).minusMinutes(12),
              LocalDateTime.now().minusDays(10).minusHours(3).minusMinutes(17),
              trackingId2,
              SampleVoyages.HONGKONG_TO_NEW_YORK.getVoyageNumber(),
              SampleLocations.HANGZOU.getUnLocode(),
              HandlingEvent.Type.LOAD);
      entityManager.persist(event2);

      HandlingEvent event3 =
          handlingEventFactory.createHandlingEvent(
              LocalDateTime.now().minusHours(3).minusMinutes(21),
              LocalDateTime.now().minusDays(3).minusHours(22).minusMinutes(1),
              trackingId2,
              SampleVoyages.HONGKONG_TO_NEW_YORK.getVoyageNumber(),
              SampleLocations.NEWYORK.getUnLocode(),
              HandlingEvent.Type.UNLOAD);
      entityManager.persist(event3);

      // The wrong voyage!
      HandlingEvent event4 =
          handlingEventFactory.createHandlingEvent(
              LocalDateTime.now().minusHours(18).minusMinutes(36),
              LocalDateTime.now().minusDays(2).minusHours(8).minusMinutes(7),
              trackingId2,
              SampleVoyages.HONGKONG_TO_NEW_YORK.getVoyageNumber(),
              SampleLocations.NEWYORK.getUnLocode(),
              HandlingEvent.Type.LOAD);
      entityManager.persist(event4);
    } catch (CannotCreateHandlingEventException e) {
      throw new RuntimeException(e);
    }

    HandlingHistory handlingHistory2 =
        handlingEventRepository.lookupHandlingHistoryOfCargo(trackingId2);
    jkl567.deriveDeliveryProgress(handlingHistory2);

    entityManager.persist(jkl567);

    // Cargo definition DEF789. This one will remain un-routed.
    TrackingId trackingId3 = new TrackingId("DEF789");

    RouteSpecification routeSpecification3 =
        new RouteSpecification(
            SampleLocations.HONGKONG, SampleLocations.MELBOURNE, LocalDate.now().plusMonths(2));

    Cargo def789 = new Cargo(trackingId3, routeSpecification3);
    entityManager.persist(def789);

    // Cargo definition MNO456. This one will be claimed properly.
    TrackingId trackingId4 = new TrackingId("MNO456");
    RouteSpecification routeSpecification4 =
        new RouteSpecification(
            SampleLocations.NEWYORK, SampleLocations.DALLAS, LocalDate.now().minusDays(24));

    Cargo mno456 = new Cargo(trackingId4, routeSpecification4);

    Itinerary itinerary4 =
        new Itinerary(
            Arrays.asList(
                new Leg(
                    SampleVoyages.NEW_YORK_TO_DALLAS,
                    SampleLocations.NEWYORK,
                    SampleLocations.DALLAS,
                    LocalDateTime.now().minusDays(34).minusHours(1).minusMinutes(1),
                    LocalDateTime.now().minusDays(28).minusHours(10).minusMinutes(1))));

    mno456.assignToRoute(itinerary4);
    entityManager.persist(mno456);

    try {
      HandlingEvent event1 =
          handlingEventFactory.createHandlingEvent(
              LocalDateTime.now().minusHours(16).minusMinutes(9),
              LocalDateTime.now().minusDays(37).minusHours(2).minusMinutes(21),
              trackingId4,
              null,
              SampleLocations.NEWYORK.getUnLocode(),
              HandlingEvent.Type.RECEIVE);

      entityManager.persist(event1);

      HandlingEvent event2 =
          handlingEventFactory.createHandlingEvent(
              LocalDateTime.now().minusHours(3).minusMinutes(9),
              LocalDateTime.now().minusDays(34).minusHours(3).minusMinutes(8),
              trackingId4,
              SampleVoyages.NEW_YORK_TO_DALLAS.getVoyageNumber(),
              SampleLocations.NEWYORK.getUnLocode(),
              HandlingEvent.Type.LOAD);

      entityManager.persist(event2);

      HandlingEvent event3 =
          handlingEventFactory.createHandlingEvent(
              LocalDateTime.now().minusHours(14).minusMinutes(10),
              LocalDateTime.now().minusDays(28).minusHours(7).minusMinutes(7),
              trackingId4,
              SampleVoyages.NEW_YORK_TO_DALLAS.getVoyageNumber(),
              SampleLocations.DALLAS.getUnLocode(),
              HandlingEvent.Type.UNLOAD);

      entityManager.persist(event3);

      HandlingEvent event4 =
          handlingEventFactory.createHandlingEvent(
              LocalDateTime.now().minusHours(22).minusMinutes(1),
              LocalDateTime.now().minusDays(27).minusHours(18).minusMinutes(3),
              trackingId4,
              null,
              SampleLocations.DALLAS.getUnLocode(),
              HandlingEvent.Type.CUSTOMS);

      entityManager.persist(event4);

      HandlingEvent event5 =
          handlingEventFactory.createHandlingEvent(
              LocalDateTime.now().minusHours(9).minusMinutes(30),
              LocalDateTime.now().minusDays(26).minusHours(14).minusMinutes(15),
              trackingId4,
              null,
              SampleLocations.DALLAS.getUnLocode(),
              HandlingEvent.Type.CLAIM);

      entityManager.persist(event5);

      HandlingHistory handlingHistory3 =
          handlingEventRepository.lookupHandlingHistoryOfCargo(trackingId4);

      mno456.deriveDeliveryProgress(handlingHistory3);

      entityManager.persist(mno456);
    } catch (CannotCreateHandlingEventException e) {
      throw new RuntimeException(e);
    }
  }
}
