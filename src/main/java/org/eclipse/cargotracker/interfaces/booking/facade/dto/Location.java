package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;

/** Location DTO. */
public record Location(String unLocode, String name) implements Serializable {

  // keeping this for compatibility with the original code
  public String getUnLocode() {
    return unLocode;
  }

  public String getName() {
    return name;
  }
}
