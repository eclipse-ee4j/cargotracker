package org.eclipse.cargotracker.infrastructure.persistence.jpa;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.LocationRepository;
import org.eclipse.cargotracker.domain.model.location.UnLocode;

@ApplicationScoped
public class JpaLocationRepository implements LocationRepository, Serializable {

  private static final long serialVersionUID = 1L;

  @PersistenceContext private EntityManager entityManager;

  @Override
  public Location find(UnLocode unLocode) {
    return entityManager
        .createNamedQuery("Location.findByUnLocode", Location.class)
        .setParameter("unLocode", unLocode)
        .getSingleResult();
  }

  @Override
  public List<Location> findAll() {
    return entityManager.createNamedQuery("Location.findAll", Location.class).getResultList();
  }
}
