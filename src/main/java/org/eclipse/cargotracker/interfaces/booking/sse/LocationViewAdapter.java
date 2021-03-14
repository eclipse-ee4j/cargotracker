package org.eclipse.cargotracker.interfaces.booking.sse;

import org.eclipse.cargotracker.domain.model.location.Location;

public class LocationViewAdapter {

  private final Location location;

  public LocationViewAdapter(Location location) {
    this.location = location;
  }

  public String getUnLocode() {
    return location.getUnLocode().getIdString();
  }

  public String getName() {
    return location.getName();
  }
}
