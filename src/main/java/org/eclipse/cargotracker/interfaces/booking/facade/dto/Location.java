package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;

/** Location DTO. */
public class Location implements Serializable {

  private static final long serialVersionUID = 1L;

  private String unLocode;
  private String name;

  public Location(String unLocode, String name) {
    this.unLocode = unLocode;
    this.name = name;
  }

  public String getUnLocode() {
    return unLocode;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name + " (" + unLocode + ")";
  }
}
