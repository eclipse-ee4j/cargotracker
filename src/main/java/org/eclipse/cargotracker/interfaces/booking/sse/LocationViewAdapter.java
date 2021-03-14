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

  private static final Map<Location, LocationCoordinates> coordinatesMap = new HashMap<>();

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

  public LocationCoordinates getCoordinates() {
    return coordinatesMap.get(location);
  }

  static {
    // TODO See if there is a service to get the latitude/longitude data from.
    coordinatesMap.put(HONGKONG, new LocationCoordinates(22, 114));
    coordinatesMap.put(MELBOURNE, new LocationCoordinates(-38, 145));
    coordinatesMap.put(STOCKHOLM, new LocationCoordinates(59, 18));
    coordinatesMap.put(HELSINKI, new LocationCoordinates(60, 25));
    coordinatesMap.put(CHICAGO, new LocationCoordinates(42, -88));
    coordinatesMap.put(TOKYO, new LocationCoordinates(36, 140));
    coordinatesMap.put(HAMBURG, new LocationCoordinates(54, 10));
    coordinatesMap.put(SHANGHAI, new LocationCoordinates(31, 121));
    coordinatesMap.put(ROTTERDAM, new LocationCoordinates(52, 5));
    coordinatesMap.put(GOTHENBURG, new LocationCoordinates(58, 12));
    coordinatesMap.put(HANGZOU, new LocationCoordinates(30, 120));
    coordinatesMap.put(NEWYORK, new LocationCoordinates(41, -74));
    coordinatesMap.put(DALLAS, new LocationCoordinates(33, -97));
    coordinatesMap.put(UNKNOWN, new LocationCoordinates(-90, 0)); // the South Pole
  }
}
