package org.eclipse.cargotracker.domain.model.voyage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import org.eclipse.cargotracker.domain.model.location.Location;

/** A carrier movement is a vessel voyage from one location to another. */
@Entity
@Table(name = "carrier_movement")
public class CarrierMovement implements Serializable {

  private static final long serialVersionUID = 1L;

  // Null object pattern
  public static final CarrierMovement NONE =
      new CarrierMovement(Location.UNKNOWN, Location.UNKNOWN, LocalDateTime.MIN, LocalDateTime.MIN);

  @Id @GeneratedValue private Long id;

  @ManyToOne
  @JoinColumn(name = "departure_location_id")
  @NotNull
  private Location departureLocation;

  @ManyToOne
  @JoinColumn(name = "arrival_location_id")
  @NotNull
  private Location arrivalLocation;

  @Column(name = "departure_time")
  @NotNull
  private LocalDateTime departureTime;

  @Column(name = "arrival_time")
  @NotNull
  private LocalDateTime arrivalTime;

  public CarrierMovement() {
    // Nothing to initialize.
  }

  public CarrierMovement(
      Location departureLocation,
      Location arrivalLocation,
      LocalDateTime departureTime,
      LocalDateTime arrivalTime) {
    Objects.requireNonNull(departureLocation, "Departure location is required.");
    Objects.requireNonNull(arrivalLocation, "Arrival location is required.");
    Objects.requireNonNull(departureTime, "Departure time is required.");
    Objects.requireNonNull(arrivalTime, "Arrival time is required.");

    // This is a workaround to a Hibernate issue. when the `LocalDateTime` field is persisted into
    // the DB, and retrieved from the DB, the values are different by nanoseconds.
    this.departureTime = departureTime.truncatedTo(ChronoUnit.SECONDS);
    this.arrivalTime = arrivalTime.truncatedTo(ChronoUnit.SECONDS);
    this.departureLocation = departureLocation;
    this.arrivalLocation = arrivalLocation;
  }

  public Location getDepartureLocation() {
    return departureLocation;
  }

  public Location getArrivalLocation() {
    return arrivalLocation;
  }

  public LocalDateTime getDepartureTime() {
    return departureTime;
  }

  public LocalDateTime getArrivalTime() {
    return arrivalTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || !(o instanceof CarrierMovement)) {
      return false;
    }

    CarrierMovement that = (CarrierMovement) o;

    return sameValueAs(that);
  }

  @Override
  public int hashCode() {
    return Objects.hash(departureLocation, departureTime, arrivalLocation, arrivalTime);
  }

  private boolean sameValueAs(CarrierMovement other) {
    return other != null
        && Objects.equals(this.departureLocation, other.departureLocation)
        && Objects.equals(this.departureTime, other.departureTime)
        && Objects.equals(this.arrivalLocation, other.arrivalLocation)
        && Objects.equals(this.arrivalTime, other.arrivalTime);
  }
}
