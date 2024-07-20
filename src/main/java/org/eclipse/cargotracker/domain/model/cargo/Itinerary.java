package org.eclipse.cargotracker.domain.model.cargo;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.Validate;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.location.Location;

@Embeddable
public class Itinerary implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  // Null object pattern.
  public static final Itinerary EMPTY_ITINERARY = new Itinerary();

  // TODO [Clean Code] Look into why cascade delete doesn't work.
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "cargo_id")
  @OrderColumn(name = "leg_order")
  @Size(min = 1)
  @NotEmpty(message = "Legs must not be empty.")
  private List<Leg> legs = Collections.emptyList();

  public Itinerary() {
    // Nothing to initialize.
  }

  public Itinerary(List<Leg> legs) {
    Validate.notEmpty(legs);
    Validate.noNullElements(legs);

    this.legs = legs;
  }

  public List<Leg> getLegs() {
    return Collections.unmodifiableList(legs);
  }

  /** Test if the given handling event is expected when executing this itinerary. */
  public boolean isExpected(HandlingEvent event) {
    if (legs.isEmpty()) {
      return true; // Handle empty case
    }

    Leg firstLeg = legs.get(0);
    Leg lastLeg = legs.get(legs.size() - 1);

    return switch (event.getType()) {
      case RECEIVE -> firstLeg.getLoadLocation().equals(event.getLocation());
      case LOAD -> legs.stream()
              .anyMatch(leg ->
                      leg.getLoadLocation().equals(event.getLocation()) &&
                      leg.getVoyage().equals(event.getVoyage())
              );
      case UNLOAD -> legs.stream()
              .anyMatch(leg ->
                      leg.getUnloadLocation().equals(event.getLocation()) &&
                      leg.getVoyage().equals(event.getVoyage())
              );
      case CLAIM -> lastLeg.getUnloadLocation().equals(event.getLocation());
      case CUSTOMS -> true; // Always allow customs events? (Consider if this is appropriate for your logic)
    };
  }

  Location getInitialDepartureLocation() {
    if (legs.isEmpty()) {
      return Location.UNKNOWN;
    } else {
      return legs.get(0).getLoadLocation();
    }
  }

  Location getFinalArrivalLocation() {
    if (legs.isEmpty()) {
      return Location.UNKNOWN;
    } else {
      return getLastLeg().getUnloadLocation();
    }
  }

  /** @return Date when cargo arrives at final destination. */
  LocalDateTime getFinalArrivalDate() {
    Leg lastLeg = getLastLeg();

    if (lastLeg == null) {
      return LocalDateTime.MAX;
    } else {
      return lastLeg.getUnloadTime();
    }
  }

  /** @return The last leg on the itinerary. */
  Leg getLastLeg() {
    if (legs.isEmpty()) {
      return null;
    } else {
      return legs.get(legs.size() - 1);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Itinerary itinerary)) return false;
    return Objects.equals(legs, itinerary.legs);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(legs);
  }

  @Override
  public String toString() {
    return "Itinerary{" + "legs=" + legs + '}';
  }
}
