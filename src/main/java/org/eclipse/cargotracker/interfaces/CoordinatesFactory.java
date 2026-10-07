package org.eclipse.cargotracker.interfaces;

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
import org.eclipse.cargotracker.domain.model.location.UnLocode;

/**
 * At the moment, coordinates are produced by a simple factory. It may be converted to a repository
 * if coordinates become a domain layer concern.
 */
public class CoordinatesFactory {

  private static final Map<String, Coordinates> COORDINATES_MAP;

  private CoordinatesFactory() {
    /* Prevent instantiation. */
  }

  public static Coordinates find(Location location) {
    return find(location.getUnLocode());
  }

  public static Coordinates find(UnLocode unLocode) {
    return find(unLocode.code());
  }

  public static Coordinates find(String unLocode) {
    return COORDINATES_MAP.get(unLocode);
  }

  static {
    Map<String, Coordinates> map = new HashMap<>();

    // TODO See if there is a service to get the latitude/longitude data from.
    map.put(HONGKONG.getUnLocode().code(), new Coordinates(22, 114));
    map.put(MELBOURNE.getUnLocode().code(), new Coordinates(-38, 145));
    map.put(STOCKHOLM.getUnLocode().code(), new Coordinates(59, 18));
    map.put(HELSINKI.getUnLocode().code(), new Coordinates(60, 25));
    map.put(CHICAGO.getUnLocode().code(), new Coordinates(42, -88));
    map.put(TOKYO.getUnLocode().code(), new Coordinates(36, 140));
    map.put(HAMBURG.getUnLocode().code(), new Coordinates(54, 10));
    map.put(SHANGHAI.getUnLocode().code(), new Coordinates(31, 121));
    map.put(ROTTERDAM.getUnLocode().code(), new Coordinates(52, 5));
    map.put(GOTHENBURG.getUnLocode().code(), new Coordinates(58, 12));
    map.put(HANGZOU.getUnLocode().code(), new Coordinates(30, 120));
    map.put(NEWYORK.getUnLocode().code(), new Coordinates(41, -74));
    map.put(DALLAS.getUnLocode().code(), new Coordinates(33, -97));
    map.put(UNKNOWN.getUnLocode().code(), new Coordinates(-90, 0)); // The South Pole.

    COORDINATES_MAP = Map.copyOf(map);
  }
}
