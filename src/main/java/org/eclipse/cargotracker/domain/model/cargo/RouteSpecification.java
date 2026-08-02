package org.eclipse.cargotracker.domain.model.cargo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.shared.AbstractSpecification;

/**
 * Route specification. Describes where a cargo origin and destination is, and the arrival deadline.
 */
@Embeddable
public class RouteSpecification extends AbstractSpecification<Itinerary> implements Serializable {

  private static final long serialVersionUID = 1L;

  @ManyToOne
  @JoinColumn(name = "spec_origin_id", updatable = false)
  private Location origin;

  @ManyToOne
  @JoinColumn(name = "spec_destination_id")
  private Location destination;

  @Column(name = "spec_arrival_deadline")
  @NotNull
  private LocalDate arrivalDeadline;

  public RouteSpecification() {}

  /**
   * @param origin origin location - can't be the same as the destination
   * @param destination destination location - can't be the same as the origin
   * @param arrivalDeadline arrival deadline
   */
  public RouteSpecification(Location origin, Location destination, LocalDate arrivalDeadline) {
    Objects.requireNonNull(origin, "Origin is required");
    Objects.requireNonNull(destination, "Destination is required");
    Objects.requireNonNull(arrivalDeadline, "Arrival deadline is required");
    if (origin.sameIdentityAs(destination)) {
      throw new IllegalArgumentException(
          "Origin and destination can't be the same: " + origin);
    }

    this.origin = origin;
    this.destination = destination;
    this.arrivalDeadline = arrivalDeadline;
  }

  public Location getOrigin() {
    return origin;
  }

  public Location getDestination() {
    return destination;
  }

  public LocalDate getArrivalDeadline() {
    return arrivalDeadline;
  }

  @Override
  public boolean isSatisfiedBy(Itinerary itinerary) {
    return itinerary != null
        && getOrigin().sameIdentityAs(itinerary.getInitialDepartureLocation())
        && getDestination().sameIdentityAs(itinerary.getFinalArrivalLocation())
        && getArrivalDeadline().isAfter(itinerary.getFinalArrivalDate().toLocalDate());
  }

  private boolean sameValueAs(RouteSpecification other) {
    return other != null
        && Objects.equals(this.origin, other.origin)
        && Objects.equals(this.destination, other.destination)
        && Objects.equals(this.arrivalDeadline, other.arrivalDeadline);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || !(o instanceof RouteSpecification)) {
      return false;
    }

    RouteSpecification that = (RouteSpecification) o;

    return sameValueAs(that);
  }

  @Override
  public int hashCode() {
    return Objects.hash(origin, destination, arrivalDeadline);
  }
}
