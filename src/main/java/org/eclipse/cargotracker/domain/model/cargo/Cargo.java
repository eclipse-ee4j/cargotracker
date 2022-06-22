package org.eclipse.cargotracker.domain.model.cargo;

import java.io.Serializable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.Validate;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.handling.HandlingHistory;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.shared.DomainObjectUtils;

/**
 * A Cargo. This is the central class in the domain model, and it is the root of the
 * Cargo-Itinerary-Leg-Delivery-RouteSpecification aggregate.
 *
 * <p>A cargo is identified by a unique tracking ID, and it always has an origin and a route
 * specification. The life cycle of a cargo begins with the booking procedure, when the tracking ID
 * is assigned. During a (short) period of time, between booking and initial routing, the cargo has
 * no itinerary.
 *
 * <p>The booking clerk requests a list of possible routes, matching the route specification, and
 * assigns the cargo to one route. The route to which a cargo is assigned is described by an
 * itinerary.
 *
 * <p>A cargo can be re-routed during transport, on demand of the customer, in which case a new
 * route is specified for the cargo and a new route is requested. The old itinerary, being a value
 * object, is discarded and a new one is attached.
 *
 * <p>It may also happen that a cargo is accidentally misrouted, which should notify the proper
 * personnel and also trigger a re-routing procedure.
 *
 * <p>When a cargo is handled, the status of the delivery changes. Everything about the delivery of
 * the cargo is contained in the Delivery value object, which is replaced whenever a cargo is
 * handled by an asynchronous event triggered by the registration of the handling event.
 *
 * <p>The delivery can also be affected by routing changes, i.e. when a the route specification
 * changes, or the cargo is assigned to a new route. In that case, the delivery update is performed
 * synchronously within the cargo aggregate.
 *
 * <p>The life cycle of a cargo ends when the cargo is claimed by the customer.
 *
 * <p>The cargo aggregate, and the entire domain model, is built to solve the problem of booking and
 * tracking cargo. All important business rules for determining whether or not a cargo is
 * misdirected, what the current status of the cargo is (on board carrier, in port etc), are
 * captured in this aggregate.
 */
@Entity
@NamedQuery(name = "Cargo.findAll", query = "Select c from Cargo c")
@NamedQuery(
    name = "Cargo.findByTrackingId",
    query = "Select c from Cargo c where c.trackingId = :trackingId")
public class Cargo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue private Long id;

  @Embedded
  @NotNull(message = "Tracking ID is required.")
  private TrackingId trackingId;

  @ManyToOne
  @JoinColumn(name = "origin_id", updatable = false)
  @NotNull
  private Location origin;

  @Embedded
  @NotNull(message = "Route specification is required.")
  private RouteSpecification routeSpecification;

  @Embedded @NotNull private Itinerary itinerary;
  @Embedded @NotNull private Delivery delivery;

  public Cargo() {
    // Nothing to initialize.
  }

  public Cargo(TrackingId trackingId, RouteSpecification routeSpecification) {
    Validate.notNull(trackingId, "Tracking ID is required.");
    Validate.notNull(routeSpecification, "Route specification is required.");

    this.trackingId = trackingId;
    // Cargo origin never changes, even if the route specification changes.
    // However, at creation, cargo origin can be derived from the initial
    // route specification.
    this.origin = routeSpecification.getOrigin();
    this.routeSpecification = routeSpecification;

    this.delivery =
        Delivery.derivedFrom(this.routeSpecification, this.itinerary, HandlingHistory.EMPTY);
    this.itinerary = Itinerary.EMPTY_ITINERARY;
  }

  public TrackingId getTrackingId() {
    return trackingId;
  }

  public Location getOrigin() {
    return origin;
  }

  public void setOrigin(Location origin) {
    this.origin = origin;
  }

  public RouteSpecification getRouteSpecification() {
    return routeSpecification;
  }

  /** @return The delivery. Never null. */
  public Delivery getDelivery() {
    return delivery;
  }

  /** @return The itinerary. Never null. */
  public Itinerary getItinerary() {
    return DomainObjectUtils.nullSafe(this.itinerary, Itinerary.EMPTY_ITINERARY);
  }

  /** Specifies a new route for this cargo. */
  public void specifyNewRoute(RouteSpecification routeSpecification) {
    Validate.notNull(routeSpecification, "Route specification is required.");

    this.routeSpecification = routeSpecification;
    // Handling consistency within the Cargo aggregate synchronously
    this.delivery = delivery.updateOnRouting(this.routeSpecification, this.itinerary);
  }

  public void assignToRoute(Itinerary itinerary) {
    Validate.notNull(itinerary, "Itinerary is required for assignment.");

    this.itinerary = itinerary;
    // Handling consistency within the Cargo aggregate synchronously
    this.delivery = delivery.updateOnRouting(this.routeSpecification, this.itinerary);
  }

  /**
   * Updates all aspects of the cargo aggregate status based on the current route specification,
   * itinerary and handling of the cargo.
   *
   * <p>When either of those three changes, i.e. when a new route is specified for the cargo, the
   * cargo is assigned to a route or when the cargo is handled, the status must be re-calculated.
   *
   * <p>{@link RouteSpecification} and {@link Itinerary} are both inside the Cargo aggregate, so
   * changes to them cause the status to be updated <b>synchronously</b>, but changes to the
   * delivery history (when a cargo is handled) cause the status update to happen
   * <b>asynchronously</b> since {@link HandlingEvent} is in a different aggregate.
   *
   * @param handlingHistory handling history
   */
  public void deriveDeliveryProgress(HandlingHistory handlingHistory) {
    this.delivery = Delivery.derivedFrom(getRouteSpecification(), getItinerary(), handlingHistory);
  }

  /**
   * @param object to compare
   * @return True if they have the same identity
   * @see #sameIdentityAs(Cargo)
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || !(object instanceof Cargo)) {
      return false;
    }

    Cargo other = (Cargo) object;
    return sameIdentityAs(other);
  }

  private boolean sameIdentityAs(Cargo other) {
    return other != null && trackingId.sameValueAs(other.trackingId);
  }

  /** @return Hash code of tracking id. */
  @Override
  public int hashCode() {
    return trackingId.hashCode();
  }

  @Override
  public String toString() {
    return trackingId.toString();
  }
}
