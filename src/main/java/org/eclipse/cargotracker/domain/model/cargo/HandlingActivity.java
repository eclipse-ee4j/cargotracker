package org.eclipse.cargotracker.domain.model.cargo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;

/**
 * A handling activity represents how and where a cargo can be handled, and can be used to express
 * predictions about what is expected to happen to a cargo in the future.
 */
@Embeddable
public class HandlingActivity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Enumerated(EnumType.STRING)
  @Column(name = "next_expected_handling_event_type")
  private HandlingEvent.Type type;

  @ManyToOne
  @JoinColumn(name = "next_expected_location_id")
  private Location location;

  @ManyToOne
  @JoinColumn(name = "next_expected_voyage_id")
  private Voyage voyage;

  public HandlingActivity() {}

  public HandlingActivity(HandlingEvent.Type type, Location location) {
    Validate.notNull(type, "Handling event type is required");
    Validate.notNull(location, "Location is required");

    this.type = type;
    this.location = location;
  }

  public HandlingActivity(HandlingEvent.Type type, Location location, Voyage voyage) {
    Validate.notNull(type, "Handling event type is required");
    Validate.notNull(location, "Location is required");
    Validate.notNull(voyage, "Voyage is required");

    this.type = type;
    this.location = location;
    this.voyage = voyage;
  }

  public HandlingEvent.Type getType() {
    return type;
  }

  public Location getLocation() {
    return location;
  }

  public Voyage getVoyage() {
    return voyage;
  }

  private boolean sameValueAs(HandlingActivity other) {
    return other != null
        && new EqualsBuilder()
            .append(this.type, other.type)
            .append(this.location, other.location)
            .append(this.voyage, other.voyage)
            .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(this.type)
        .append(this.location)
        .append(this.voyage)
        .toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (obj.getClass() != this.getClass()) {
      return false;
    }

    HandlingActivity other = (HandlingActivity) obj;

    return sameValueAs(other);
  }

  public boolean isEmpty() {
    if (type != null) {
      return false;
    }

    if (location != null) {
      return false;
    }

    return voyage == null;
  }
}
