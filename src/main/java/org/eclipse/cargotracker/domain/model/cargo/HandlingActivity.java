package org.eclipse.cargotracker.domain.model.cargo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
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
  @NotNull(message = "Handling event type is required.")
  private HandlingEvent.Type type;

  @ManyToOne
  @JoinColumn(name = "next_expected_location_id")
  @NotNull(message = "Location is required.")
  private Location location;

  @ManyToOne
  @JoinColumn(name = "next_expected_voyage_id")
  private Voyage voyage;

  public HandlingActivity() {}

  public HandlingActivity(HandlingEvent.Type type, Location location) {
    this(type, location, null);
  }

  public HandlingActivity(HandlingEvent.Type type, Location location, Voyage voyage) {
    Objects.requireNonNull(type, "Handling event type is required.");
    Objects.requireNonNull(location, "Location is required.");

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
        && Objects.equals(this.type, other.type)
        && Objects.equals(this.location, other.location)
        && Objects.equals(this.voyage, other.voyage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, location, voyage);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (!(obj instanceof HandlingActivity)) {
      return false;
    }

    HandlingActivity other = (HandlingActivity) obj;

    return sameValueAs(other);
  }

  public boolean isEmpty() {
    return type == null && location == null && voyage == null;
  }
}
