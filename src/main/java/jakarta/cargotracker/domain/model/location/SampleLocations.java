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
package jakarta.cargotracker.domain.model.location;

/**
 * Sample locations, for test purposes.
 */
public class SampleLocations {

    // TODO Move these to the Location domain object?
    public static final Location HONGKONG = new Location(new UnLocode("CNHKG"), "Hong Kong");
    public static final Location MELBOURNE = new Location(new UnLocode("AUMEL"), "Melbourne");
    public static final Location STOCKHOLM = new Location(new UnLocode("SESTO"), "Stockholm");
    public static final Location HELSINKI = new Location(new UnLocode("FIHEL"), "Helsinki");
    public static final Location CHICAGO = new Location(new UnLocode("USCHI"), "Chicago");
    public static final Location TOKYO = new Location(new UnLocode("JNTKO"), "Tokyo");
    public static final Location HAMBURG = new Location(new UnLocode("DEHAM"), "Hamburg");
    public static final Location SHANGHAI = new Location(new UnLocode("CNSHA"), "Shanghai");
    public static final Location ROTTERDAM = new Location(new UnLocode("NLRTM"), "Rotterdam");
    public static final Location GOTHENBURG = new Location(new UnLocode("SEGOT"), "Guttenburg");
    public static final Location HANGZOU = new Location(new UnLocode("CNHGH"), "Hangzhou");
    public static final Location NEWYORK = new Location(new UnLocode("USNYC"), "New York");
    public static final Location DALLAS = new Location(new UnLocode("USDAL"), "Dallas");
}
