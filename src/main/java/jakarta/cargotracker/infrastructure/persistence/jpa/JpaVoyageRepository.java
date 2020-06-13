package jakarta.cargotracker.infrastructure.persistence.jpa;

import jakarta.cargotracker.domain.model.voyage.Voyage;
import jakarta.cargotracker.domain.model.voyage.VoyageNumber;
import jakarta.cargotracker.domain.model.voyage.VoyageRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class JpaVoyageRepository implements VoyageRepository, Serializable {

    private static final long serialVersionUID = 1L;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Voyage find(VoyageNumber voyageNumber) {
        return entityManager
                .createNamedQuery("Voyage.findByVoyageNumber", Voyage.class)
                .setParameter("voyageNumber", voyageNumber).getSingleResult();
    }

    @Override
    public List<Voyage> findAll() {
        return entityManager.createNamedQuery("Voyage.findAll", Voyage.class)
                .getResultList();
    }


}
