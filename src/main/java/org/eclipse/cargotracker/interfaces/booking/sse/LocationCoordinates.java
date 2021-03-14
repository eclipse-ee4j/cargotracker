package org.eclipse.cargotracker.interfaces.booking.sse;

public class LocationCoordinates {

  private final double latitude;
  private final double longitude;

  public LocationCoordinates(double latitude, double longitude) {
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
