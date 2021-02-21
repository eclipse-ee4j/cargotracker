package org.eclipse.cargotracker.application;

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
import org.eclipse.cargotracker.domain.model.location.SampleLocations;
import org.eclipse.cargotracker.domain.model.voyage.SampleVoyages;

/** Loads sample data for demo. */
@Singleton
@Startup
public class BookingServiceTestDataGenerator {

  @Inject private Logger logger;
  @PersistenceContext private EntityManager entityManager;

  @PostConstruct
  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void loadSampleData() {
    logger.info("Loading sample data.");
    unLoadAll(); // Fail-safe in case of application restart that does not
    // trigger a JPA schema drop.
    loadSampleLocations();
    loadSampleVoyages();
    // loadSampleCargos();
  }

  private void unLoadAll() {
    logger.info("Unloading all existing data.");
    // In order to remove handling events, must remove references in cargo.
    // Dropping cargo first won't work since handling events have references
    // to it.
    // TODO [Clean Code] See if there is a better way to do this.
    List<Cargo> cargos =
        entityManager.createQuery("Select c from Cargo c", Cargo.class).getResultList();
    cargos.forEach(
        cargo -> {
          cargo.getDelivery().setLastEvent(null);
          entityManager.merge(cargo);
        });

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
}
