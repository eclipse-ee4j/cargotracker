package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;

/** Location DTO. */
public record Location(String unLocode, String name) implements Serializable {

  @Override
  public String toString() {
    return name + " (" + unLocode + ")";
  }
}
