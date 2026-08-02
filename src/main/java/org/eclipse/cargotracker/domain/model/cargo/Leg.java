package org.eclipse.cargotracker.domain.model.cargo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;

@Entity
public class Leg implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue private Long id;

  @ManyToOne
  @JoinColumn(name = "voyage_id")
  @NotNull
  private Voyage voyage;

  @ManyToOne
  @JoinColumn(name = "load_location_id")
  @NotNull
  private Location loadLocation;

  @ManyToOne
  @JoinColumn(name = "unload_location_id")
  @NotNull
  private Location unloadLocation;

  @Column(name = "load_time")
  @NotNull
  private LocalDateTime loadTime;

  @Column(name = "unload_time")
  @NotNull
  private LocalDateTime unloadTime;

  public Leg() {
    // Nothing to initialize.
  }

  public Leg(
      Voyage voyage,
      Location loadLocation,
      Location unloadLocation,
      LocalDateTime loadTime,
      LocalDateTime unloadTime) {
    Objects.requireNonNull(voyage, "Voyage is required.");
    Objects.requireNonNull(loadLocation, "Load location is required.");
    Objects.requireNonNull(unloadLocation, "Unload location is required.");
    Objects.requireNonNull(loadTime, "Load time is required.");
    Objects.requireNonNull(unloadTime, "Unload time is required.");

    this.voyage = voyage;
    this.loadLocation = loadLocation;
    this.unloadLocation = unloadLocation;

    // This is a workaround to a Hibernate issue. when the `LocalDateTime` field is persisted into
    // the DB, and retrieved from the DB, the values are different by nanoseconds.
    this.loadTime = loadTime.truncatedTo(ChronoUnit.SECONDS);
    this.unloadTime = unloadTime.truncatedTo(ChronoUnit.SECONDS);
  }

  public Voyage getVoyage() {
    return voyage;
  }

  public Location getLoadLocation() {
    return loadLocation;
  }

  public Location getUnloadLocation() {
    return unloadLocation;
  }

  public LocalDateTime getLoadTime() {
    return this.loadTime;
  }

  public LocalDateTime getUnloadTime() {
    return this.unloadTime;
  }

  private boolean sameValueAs(Leg other) {
    return other != null
        && Objects.equals(this.voyage, other.voyage)
        && Objects.equals(this.loadLocation, other.loadLocation)
        && Objects.equals(this.unloadLocation, other.unloadLocation)
        && Objects.equals(this.loadTime, other.loadTime)
        && Objects.equals(this.unloadTime, other.unloadTime);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || !(o instanceof Leg)) {
      return false;
    }

    Leg leg = (Leg) o;

    return sameValueAs(leg);
  }

  @Override
  public int hashCode() {
    return Objects.hash(voyage, loadLocation, unloadLocation, loadTime, unloadTime);
  }

  @Override
  public String toString() {
    return "Leg{"
        + "id="
        + id
        + ", voyage="
        + voyage
        + ", loadLocation="
        + loadLocation
        + ", unloadLocation="
        + unloadLocation
        + ", loadTime="
        + loadTime
        + ", unloadTime="
        + unloadTime
        + '}';
  }
}
