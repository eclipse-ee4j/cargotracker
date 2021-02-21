package org.eclipse.cargotracker.domain.model.cargo;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
    Validate.noNullElements(
        new Object[] {voyage, loadLocation, unloadLocation, loadTime, unloadTime});

    this.voyage = voyage;
    this.loadLocation = loadLocation;
    this.unloadLocation = unloadLocation;
    this.loadTime = loadTime;
    this.unloadTime = unloadTime;
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
        && new EqualsBuilder()
            .append(this.voyage, other.voyage)
            .append(this.loadLocation, other.loadLocation)
            .append(this.unloadLocation, other.unloadLocation)
            .append(this.loadTime, other.loadTime)
            .append(this.unloadTime, other.unloadTime)
            .isEquals();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Leg leg = (Leg) o;

    return sameValueAs(leg);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(voyage)
        .append(loadLocation)
        .append(unloadLocation)
        .append(loadTime)
        .append(unloadTime)
        .toHashCode();
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
