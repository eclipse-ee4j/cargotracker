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
package net.java.cargotracker.infrastructure.persistence.jpa;

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
import net.java.cargotracker.domain.model.cargo.Cargo;
import net.java.cargotracker.domain.model.cargo.CargoRepository;
import net.java.cargotracker.domain.model.cargo.Leg;
import net.java.cargotracker.domain.model.cargo.TrackingId;

@ApplicationScoped
public class JpaCargoRepository implements CargoRepository, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(
            JpaCargoRepository.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Cargo find(TrackingId trackingId) {
        Cargo cargo;

        try {
            cargo = entityManager.createNamedQuery("Cargo.findByTrackingId",
                    Cargo.class)
                    .setParameter("trackingId", trackingId)
                    .getSingleResult();
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
        return entityManager.createNamedQuery("Cargo.findAll", Cargo.class)
                .getResultList();
    }

    @Override
    public List<TrackingId> getAllTrackingIds() {
        List<TrackingId> trackingIds = new ArrayList<>();

        try {
            trackingIds = entityManager.createNamedQuery(
                    "Cargo.getAllTrackingIds", TrackingId.class).getResultList();
        } catch (NoResultException e) {
            logger.log(Level.FINE, "Unable to get all tracking IDs", e);
        }

        return trackingIds;
    }
}