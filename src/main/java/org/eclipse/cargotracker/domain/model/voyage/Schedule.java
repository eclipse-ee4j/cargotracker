package org.eclipse.cargotracker.domain.model.voyage;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.Validate;

/** A voyage schedule. */
@Embeddable
public class Schedule implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  // Null object pattern.
  public static final Schedule EMPTY = new Schedule();

  // TODO [Clean Code] Look into why cascade delete doesn't work.
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "voyage_id")
  @OrderColumn(name = "movement_order")
  @NotNull
  @Size(min = 1)
  private List<CarrierMovement> carrierMovements = Collections.emptyList();

  public Schedule() {
    // Nothing to initialize.
  }

  Schedule(List<CarrierMovement> carrierMovements) {
    Validate.notNull(carrierMovements);
    Validate.noNullElements(carrierMovements);
    Validate.notEmpty(carrierMovements);

    this.carrierMovements = carrierMovements;
  }

  public List<CarrierMovement> getCarrierMovements() {
    return Collections.unmodifiableList(carrierMovements);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Schedule schedule)) return false;
    return Objects.equals(carrierMovements, schedule.carrierMovements);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(carrierMovements);
  }
}
