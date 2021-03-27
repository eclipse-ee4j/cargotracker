package org.eclipse.cargotracker.interfaces;

/**
 * At the moment, coordinates are effectively a shared DTO in the interface layer. It may be
 * converted to a domain level concern at some point. *
 */
public class Coordinates {

  private final double latitude;
  private final double longitude;

  public Coordinates(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }
}
