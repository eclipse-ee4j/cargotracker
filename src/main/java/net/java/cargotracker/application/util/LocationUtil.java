/*
    The MIT License
    
    Copyright (c) 2019 Oracle and/or its affiliates
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/
package net.java.cargotracker.application.util;

import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.map.LatLng;

/**
 *
 * @author davidd
 */
public class LocationUtil {

    public static String getLocationName(String location) {
        //Helsinki (FIHEL)
        return location.substring(0, location.indexOf("("));
    }

    public static String getLocationCode(String location) {
        return location.substring(location.indexOf("(") + 1, location.indexOf(")") );
    }

    public static List<String> getLocationsCode() {

        List<String> locationsCode = new ArrayList<>(13);

        locationsCode.add("USCHI");
        locationsCode.add("USDAL");
        locationsCode.add("USNYC");

        locationsCode.add("NLRTM");
        locationsCode.add("DEHAM");
        locationsCode.add("FIHEL");
        locationsCode.add("SEGOT");
        locationsCode.add("SESTO");

        locationsCode.add("AUMEL");
        locationsCode.add("CNSHA");
        locationsCode.add("CNHGH");
        locationsCode.add("CNHKG");
        locationsCode.add("JNTKO");

        return locationsCode;
    }

    public static String getPortCoordinates(String portCode) {

        switch (portCode.toUpperCase()) {
            case "USCHI":
                return "41.5, -87.38";
            case "USDAL":
                return "29.387424, -94.125479"; //TODO: approx USDAL coord!            
            case "SEGOT":
                return "57.42, 11.57";
            case "DEHAM":
                return "53.33, 9.59";
            case "CNHGH":
                return "23.6, 113.14";
            case "FIHEL":
                return "60.1015, 25.5715";
            case "CNHKG":
                return "22.336104, 114.073471";
            case "AUMEL":
                return "-37.50, 144.58";
            case "USNYC":
                return "40.4252, -74.0022";
            case "NLRTM":
                return "51.55 , 4.24";
            case "CNSHA":
                return "31.324062, 121.677776";
            case "SESTO":
                return "59.19, 18.3";
            case "JNTKO":
                return "35.43 , 139.46";
            default:
                return null;
        }
    }

    public static String getCode(String city) {
        switch (city.toUpperCase()) {
            case "CHICAGO":
                return "USCHI";
            case "DALLAS":
                return "USDAL";
            case "GUTTENBURG": // TODO: spelling ok? 
                return "SEGOT";
            case "HAMBURG":
                return "DEHAM";
            case "HANGZHOU":
                return "CNHGH";
            case "HELSINKI":
                return "FIHEL";
            case "HONG KONG":
                return "CNHKG";
            case "MELBOURNE":
                return "AUMEL";
            case "NEW YORK":
                return "USNYC";
            case "ROTTERFDAM":
                return "NLRTM";
            case "SHANGHAI":
                return "CNSHA";
            case "STOCKHOLM":
                return "SESTO";
            case "TOKYO":
                return "JNTKO";
            default:
                return null;
        }
    }

    public static LatLng getCoordinatesForLocation(String portCode) {
        switch (portCode.toUpperCase()) {
            case "USCHI":
                return new LatLng(41.5, -87.38);
            case "USDAL":
                return new LatLng(29.387424, -94.125479); //TODO: approx USDAL coord!            
            case "SEGOT":
                return new LatLng(57.42, 11.57);
            case "DEHAM":
                return new LatLng(53.33, 9.59);
            case "CNHGH":
                return new LatLng(23.6, 113.14);
            case "FIHEL":
                return new LatLng(60.1015, 25.5715);
            case "CNHKG":
                return new LatLng(22.336104, 114.073471);
            case "AUMEL":
                return new LatLng(-37.50, 144.58);
            case "USNYC":
                return new LatLng(40.4252, -74.0022);
            case "NLRTM":
                return new LatLng(51.55, 4.24);
            case "CNSHA":
                return new LatLng(31.324062, 121.677776);
            case "SESTO":
                return new LatLng(59.19, 18.3);
            case "JNTKO":
                return new LatLng(35.43, 139.46);
            default:
                return null;
        }
    }

}
