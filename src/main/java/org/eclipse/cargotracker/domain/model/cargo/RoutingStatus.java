package org.eclipse.cargotracker.domain.model.cargo;

public enum RoutingStatus {
  NOT_ROUTED,
  ROUTED,
  MISROUTED;

  public boolean sameValueAs(RoutingStatus other) {
    return this.equals(other);
  }
}
