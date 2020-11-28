package org.eclipse.cargotracker.infrastructure.persistence.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.CargoRepository;
import org.eclipse.cargotracker.domain.model.cargo.Leg;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;

@ApplicationScoped
public class JpaCargoRepository implements CargoRepository, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(JpaCargoRepository.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Cargo find(TrackingId trackingId) {
		Cargo cargo;

		try {
			cargo = entityManager.createNamedQuery("Cargo.findByTrackingId", Cargo.class)
					.setParameter("trackingId", trackingId).getSingleResult();
		} catch (NoResultException e) {
			logger.log(Level.FINE, "Find called on non-existant tracking ID.", e);
			cargo = null;
		}

		return cargo;
	}

	@Override
	public void store(Cargo cargo) {
		// TODO See why cascade is not working correctly for legs.
		for (Leg leg : cargo.getItinerary().getLegs()) {
			entityManager.persist(leg);
		}

		entityManager.persist(cargo);
	}

	@Override
	public TrackingId nextTrackingId() {
		String random = UUID.randomUUID().toString().toUpperCase();

		return new TrackingId(random.substring(0, random.indexOf("-")));
	}

	@Override
	public List<Cargo> findAll() {
		return entityManager.createNamedQuery("Cargo.findAll", Cargo.class).getResultList();
	}

	@Override
	public List<TrackingId> getAllTrackingIds() {
		List<TrackingId> trackingIds = new ArrayList<>();

		try {
			trackingIds = entityManager.createNamedQuery("Cargo.getAllTrackingIds", TrackingId.class).getResultList();
		} catch (NoResultException e) {
			logger.log(Level.FINE, "Unable to get all tracking IDs", e);
		}

		return trackingIds;
	}
}
