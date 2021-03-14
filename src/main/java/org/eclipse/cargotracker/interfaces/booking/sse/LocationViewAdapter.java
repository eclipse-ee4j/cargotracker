package org.eclipse.cargotracker.interfaces.booking.sse;

import static org.eclipse.cargotracker.domain.model.location.Location.UNKNOWN;
import static org.eclipse.cargotracker.domain.model.location.SampleLocations.CHICAGO;
import static org.eclipse.cargotracker.domain.model.location.SampleLocations.DALLAS;
import static org.eclipse.cargotracker.domain.model.location.SampleLocations.GOTHENBURG;
import static org.eclipse.cargotracker.domain.model.location.SampleLocations.HAMBURG;
import static org.eclipse.cargotracker.domain.model.location.SampleLocations.HANGZOU;
import static org.eclipse.cargotracker.domain.model.location.SampleLocations.HELSINKI;
import static org.eclipse.cargotracker.domain.model.location.SampleLocations.HONGKONG;
import static org.eclipse.cargotracker.domain.model.location.SampleLocations.MELBOURNE;
import static org.eclipse.cargotracker.domain.model.location.SampleLocations.NEWYORK;
import static org.eclipse.cargotracker.domain.model.location.SampleLocations.ROTTERDAM;
import static org.eclipse.cargotracker.domain.model.location.SampleLocations.SHANGHAI;
import static org.eclipse.cargotracker.domain.model.location.SampleLocations.STOCKHOLM;
import static org.eclipse.cargotracker.domain.model.location.SampleLocations.TOKYO;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.cargotracker.domain.model.location.Location;

/** View adapter for displaying a location in a realtime tracking context. */
public class LocationViewAdapter {

  private static final Map<Location, Coordinates> coordinatesMap = new HashMap<>();

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

  public Coordinates getCoordinates() {
    return coordinatesMap.get(location);
  }

  static {
    // TODO See if there is a service to get the latitude/longitude data from.
    coordinatesMap.put(HONGKONG, new Coordinates(22, 114));
    coordinatesMap.put(MELBOURNE, new Coordinates(-38, 145));
    coordinatesMap.put(STOCKHOLM, new Coordinates(59, 18));
    coordinatesMap.put(HELSINKI, new Coordinates(60, 25));
    coordinatesMap.put(CHICAGO, new Coordinates(42, -88));
    coordinatesMap.put(TOKYO, new Coordinates(36, 140));
    coordinatesMap.put(HAMBURG, new Coordinates(54, 10));
    coordinatesMap.put(SHANGHAI, new Coordinates(31, 121));
    coordinatesMap.put(ROTTERDAM, new Coordinates(52, 5));
    coordinatesMap.put(GOTHENBURG, new Coordinates(58, 12));
    coordinatesMap.put(HANGZOU, new Coordinates(30, 120));
    coordinatesMap.put(NEWYORK, new Coordinates(41, -74));
    coordinatesMap.put(DALLAS, new Coordinates(33, -97));
    coordinatesMap.put(UNKNOWN, new Coordinates(-90, 0)); // The South Pole.
  }

  public static class Coordinates {

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
}
