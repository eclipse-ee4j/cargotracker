package org.eclipse.cargotracker.application.util;

public class LocationUtil {

  public static String getLocationName(String location) {
    // Helsinki (FIHEL)
    return location.substring(0, location.indexOf("("));
  }

  public static String getLocationCode(String location) {
    return location.substring(location.indexOf("(") + 1, location.indexOf(")"));
  }
}
