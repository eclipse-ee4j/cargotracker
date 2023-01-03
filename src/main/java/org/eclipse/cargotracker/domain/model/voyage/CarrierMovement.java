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
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.eclipse.cargotracker.domain.model.location.Location;

/** A carrier movement is a vessel voyage from one location to another. */
@Entity
@Table(name = "carrier_movement")
public class CarrierMovement implements Serializable {

  // Null object pattern
  public static final CarrierMovement NONE =
      new CarrierMovement(Location.UNKNOWN, Location.UNKNOWN, LocalDateTime.MIN, LocalDateTime.MIN);
  private static final long serialVersionUID = 1L;
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
    Validate.noNullElements(
        new Object[] {departureLocation, arrivalLocation, departureTime, arrivalTime});

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
    return new HashCodeBuilder()
        .append(this.departureLocation)
        .append(this.departureTime)
        .append(this.arrivalLocation)
        .append(this.arrivalTime)
        .toHashCode();
  }

  private boolean sameValueAs(CarrierMovement other) {
    return other != null
        && new EqualsBuilder()
            .append(this.departureLocation, other.departureLocation)
            .append(this.departureTime, other.departureTime)
            .append(this.arrivalLocation, other.arrivalLocation)
            .append(this.arrivalTime, other.arrivalTime)
            .isEquals();
  }
}
