package org.eclipse.cargotracker.domain.model.voyage;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public record VoyageNumber(
    @Column(name = "voyage_number") @NotEmpty(message = "Voyage number cannot be empty.")
        String number)
    implements Serializable {

  public VoyageNumber {
    Objects.requireNonNull(number, "Voyage number is required");
  }

  @Override
  public String toString() {
    return number;
  }
}
