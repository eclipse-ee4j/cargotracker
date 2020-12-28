package org.eclipse.cargotracker.application.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.SampleLocations;

public class LocationUtil {

    public static String getLocationName(String location) {
        //Helsinki (FIHEL)
        return location.substring(0, location.indexOf("("));
    }

    public static String getLocationCode(String location) {
        return location.substring(location.indexOf("(") + 1, location.indexOf(")"));
    }

    public static List<Location> getLocationsCode() {

        List<Location> locationsCode = new ArrayList<>(13);

        locationsCode.add(SampleLocations.CHICAGO);
        locationsCode.add(SampleLocations.DALLAS);
        locationsCode.add(SampleLocations.NEWYORK);

        locationsCode.add(SampleLocations.ROTTERDAM);
        locationsCode.add(SampleLocations.HAMBURG);
        locationsCode.add(SampleLocations.HELSINKI);
        locationsCode.add(SampleLocations.GOTHENBURG);
        locationsCode.add(SampleLocations.STOCKHOLM);

        locationsCode.add(SampleLocations.MELBOURNE);
        locationsCode.add(SampleLocations.SHANGHAI);
        locationsCode.add(SampleLocations.HANGZOU);
        locationsCode.add(SampleLocations.HONGKONG);
        locationsCode.add(SampleLocations.TOKYO);

        return locationsCode;
    }
}
