package org.eclipse.cargotracker.domain.model.voyage;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/** A voyage schedule. */
@Embeddable
public class Schedule implements Serializable {

  private static final long serialVersionUID = 1L;

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

  private boolean sameValueAs(Schedule other) {
    return other != null && this.carrierMovements.equals(other.carrierMovements);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || !(o instanceof Schedule)) {
      return false;
    }

    Schedule that = (Schedule) o;

    return sameValueAs(that);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(this.carrierMovements).toHashCode();
  }
}
