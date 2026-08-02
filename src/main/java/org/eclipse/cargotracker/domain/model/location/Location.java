package org.eclipse.cargotracker.domain.model.location;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A location in our model is stops on a journey, such as cargo origin or destination, or carrier
 * movement end points.
 *
 * <p>It is uniquely identified by a UN location code.
 */
@Entity
@NamedQuery(name = "Location.findAll", query = "Select l from Location l")
@NamedQuery(
    name = "Location.findByUnLocode",
    query = "Select l from Location l where l.unLocode = :unLocode")
public class Location implements Serializable {

  private static final long serialVersionUID = 1L;

  // Special Location object that marks an unknown location.
  public static final Location UNKNOWN = new Location(new UnLocode("XXXXX"), "Unknown location");

  @Id @GeneratedValue private Long id;
  @Embedded @NotNull private UnLocode unLocode;
  @NotEmpty private String name;

  public Location() {
    // Nothing to do.
  }

  /**
   * @param unLocode UN Locode
   * @param name Location name
   * @throws NullPointerException if the UN Locode or name is null
   */
  public Location(UnLocode unLocode, String name) {
    Objects.requireNonNull(unLocode, "UN Locode is required.");
    Objects.requireNonNull(name, "Name is required.");

    this.unLocode = unLocode;
    this.name = name;
  }

  /**
   * @return UN location code for this location.
   */
  public UnLocode getUnLocode() {
    return unLocode;
  }

  /**
   * @return Actual name of this location, e.g. "Stockholm".
   */
  public String getName() {
    return name;
  }

  /**
   * @param object to compare
   * @return Since this is an entiy this will be true iff UN locodes are equal.
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (!(object instanceof Location)) {
      return false;
    }

    Location other = (Location) object;

    return sameIdentityAs(other);
  }

  public boolean sameIdentityAs(Location other) {
    return other != null && Objects.equals(this.unLocode, other.unLocode);
  }

  /**
   * @return Hash code of UN locode.
   */
  @Override
  public int hashCode() {
    return Objects.hash(unLocode);
  }

  @Override
  public String toString() {
    return name + " [" + unLocode + "]";
  }
}
