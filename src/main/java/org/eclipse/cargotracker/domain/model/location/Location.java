package org.eclipse.cargotracker.domain.model.location;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.Validate;

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

  @Serial private static final long serialVersionUID = 1L;

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
   * @throws IllegalArgumentException if the UN Locode or name is null
   */
  public Location(UnLocode unLocode, String name) {
    Validate.notNull(unLocode);
    Validate.notNull(name);

    this.unLocode = unLocode;
    this.name = name;
  }

  /** @return UN location code for this location. */
  public UnLocode getUnLocode() {
    return unLocode;
  }

  /** @return Actual name of this location, e.g. "Stockholm". */
  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Location location)) return false;
    return Objects.equals(unLocode, location.unLocode);
  }

  public boolean sameIdentityAs(Location other) {
    return Objects.equals(this.unLocode, other.unLocode);
  }

  /** @return Hash code of UN locode. */
  @Override
  public int hashCode() {
    return Objects.hash(unLocode);
  }

  @Override
  public String toString() {
    return name + " [" + unLocode + "]";
  }
}
