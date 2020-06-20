package org.eclipse.cargotracker.infrastructure.persistence.jpa;

import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.handling.HandlingEventRepository;
import org.eclipse.cargotracker.domain.model.handling.HandlingHistory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@ApplicationScoped
public class JpaHandlingEventRepository implements HandlingEventRepository,
        Serializable {

    private static final long serialVersionUID = 1L;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void store(HandlingEvent event) {
        entityManager.persist(event);
    }

    @Override
    public HandlingHistory lookupHandlingHistoryOfCargo(TrackingId trackingId) {
        return new HandlingHistory(entityManager.createNamedQuery(
                "HandlingEvent.findByTrackingId", HandlingEvent.class)
                .setParameter("trackingId", trackingId).getResultList());
    }
}
