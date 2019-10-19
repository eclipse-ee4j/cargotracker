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
package jakarta.cargotracker.domain.model.voyage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static jakarta.cargotracker.application.util.DateUtil.toDate;
import jakarta.cargotracker.domain.model.location.Location;
import static jakarta.cargotracker.domain.model.location.SampleLocations.CHICAGO;
import static jakarta.cargotracker.domain.model.location.SampleLocations.DALLAS;
import static jakarta.cargotracker.domain.model.location.SampleLocations.HAMBURG;
import static jakarta.cargotracker.domain.model.location.SampleLocations.HANGZOU;
import static jakarta.cargotracker.domain.model.location.SampleLocations.HELSINKI;
import static jakarta.cargotracker.domain.model.location.SampleLocations.HONGKONG;
import static jakarta.cargotracker.domain.model.location.SampleLocations.MELBOURNE;
import static jakarta.cargotracker.domain.model.location.SampleLocations.NEWYORK;
import static jakarta.cargotracker.domain.model.location.SampleLocations.ROTTERDAM;
import static jakarta.cargotracker.domain.model.location.SampleLocations.SHANGHAI;
import static jakarta.cargotracker.domain.model.location.SampleLocations.STOCKHOLM;
import static jakarta.cargotracker.domain.model.location.SampleLocations.TOKYO;

/**
 * Sample carrier movements, for test purposes.
 */
public class SampleVoyages {

    public static final Voyage CM001 = createVoyage("CM001", STOCKHOLM, HAMBURG);
    public static final Voyage CM002 = createVoyage("CM002", HAMBURG, HONGKONG);
    public static final Voyage CM003 = createVoyage("CM003", HONGKONG, NEWYORK);
    public static final Voyage CM004 = createVoyage("CM004", NEWYORK, CHICAGO);
    public static final Voyage CM005 = createVoyage("CM005", CHICAGO, HAMBURG);
    public static final Voyage CM006 = createVoyage("CM006", HAMBURG, HANGZOU);

    private static Voyage createVoyage(String id, Location from, Location to) {
        return new Voyage(new VoyageNumber(id),
                new Schedule(Arrays.asList(new CarrierMovement(from, to,
                                        new Date(), new Date()))));
    }

    public final static Voyage v100 = new Voyage.Builder(
            new VoyageNumber("V100"), HONGKONG)
            .addMovement(TOKYO, toDate("2014-03-03"), toDate("2014-03-05"))
            .addMovement(NEWYORK, toDate("2014-03-06"), toDate("2014-03-09"))
            .build();
    public final static Voyage v200 = new Voyage.Builder(
            new VoyageNumber("V200"), TOKYO)
            .addMovement(NEWYORK, toDate("2014-03-06"), toDate("2014-03-08"))
            .addMovement(CHICAGO, toDate("2014-03-10"), toDate("2014-03-14"))
            .addMovement(STOCKHOLM, toDate("2014-03-14"), toDate("2014-03-16"))
            .build();
    public final static Voyage v300 = new Voyage.Builder(
            new VoyageNumber("V300"), TOKYO)
            .addMovement(ROTTERDAM, toDate("2014-03-08"), toDate("2014-03-11"))
            .addMovement(HAMBURG, toDate("2014-03-11"), toDate("2014-03-12"))
            .addMovement(MELBOURNE, toDate("2014-03-14"), toDate("2014-03-18"))
            .addMovement(TOKYO, toDate("2014-03-19"), toDate("2014-03-21"))
            .build();
    public final static Voyage v400 = new Voyage.Builder(
            new VoyageNumber("V400"), HAMBURG)
            .addMovement(STOCKHOLM, toDate("2014-03-14"), toDate("2014-03-15"))
            .addMovement(HELSINKI, toDate("2014-03-15"), toDate("2014-03-16"))
            .addMovement(HAMBURG, toDate("2014-03-20"), toDate("2014-03-22"))
            .build();
    /**
     * Voyage number 0100S (by ship)
     *
     * Hongkong - Hangzou - Tokyo - Melbourne - New York
     */
    public static final Voyage HONGKONG_TO_NEW_YORK = new Voyage.Builder(
            new VoyageNumber("0100S"), HONGKONG)
            .addMovement(HANGZOU, toDate("2013-10-01", "12:00"),
                    toDate("2013-10-03", "14:30"))
            .addMovement(TOKYO, toDate("2013-10-03", "21:00"),
                    toDate("2013-10-06", "06:15"))
            .addMovement(MELBOURNE, toDate("2013-10-06", "11:00"),
                    toDate("2013-10-12", "11:30"))
            .addMovement(NEWYORK, toDate("2013-10-14", "12:00"),
                    toDate("2013-10-23", "23:10")).build();
    /**
     * Voyage number 0200T (by train)
     *
     * New York - Chicago - Dallas
     */
    public static final Voyage NEW_YORK_TO_DALLAS = new Voyage.Builder(
            new VoyageNumber("0200T"), NEWYORK)
            .addMovement(CHICAGO, toDate("2013-10-24", "07:00"),
                    toDate("2013-10-24", "17:45"))
            .addMovement(DALLAS, toDate("2013-10-24", "21:25"),
                    toDate("2013-10-25", "19:30")).build();
    /**
     * Voyage number 0300A (by airplane)
     *
     * Dallas - Hamburg - Stockholm - Helsinki
     */
    public static final Voyage DALLAS_TO_HELSINKI = new Voyage.Builder(
            new VoyageNumber("0300A"), DALLAS)
            .addMovement(HAMBURG, toDate("2013-10-29", "03:30"),
                    toDate("2013-10-31", "14:00"))
            .addMovement(STOCKHOLM, toDate("2013-11-01", "15:20"),
                    toDate("2013-11-01", "18:40"))
            .addMovement(HELSINKI, toDate("2013-11-02", "09:00"),
                    toDate("2013-11-02", "11:15")).build();
    /**
     * Voyage number 0301S (by ship)
     *
     * Dallas - Hamburg - Stockholm - Helsinki, alternate route
     */
    public static final Voyage DALLAS_TO_HELSINKI_ALT = new Voyage.Builder(
            new VoyageNumber("0301S"), DALLAS)
            .addMovement(HELSINKI, toDate("2013-10-29", "03:30"),
                    toDate("2013-11-05", "15:45"))
            .build();
    /**
     * Voyage number 0400S (by ship)
     *
     * Helsinki - Rotterdam - Shanghai - Hongkong
     *
     */
    public static final Voyage HELSINKI_TO_HONGKONG = new Voyage.Builder(
            new VoyageNumber("0400S"), HELSINKI)
            .addMovement(ROTTERDAM, toDate("2013-11-04", "05:50"),
                    toDate("2013-11-06", "14:10"))
            .addMovement(SHANGHAI, toDate("2013-11-10", "21:45"),
                    toDate("2013-11-22", "16:40"))
            .addMovement(HONGKONG, toDate("2013-11-24", "07:00"),
                    toDate("2013-11-28", "13:37")).build();
    public static final Map<VoyageNumber, Voyage> ALL = new HashMap();

    static {
        for (Field field : SampleVoyages.class.getDeclaredFields()) {
            if (field.getType().equals(Voyage.class)) {
                try {
                    Voyage voyage = (Voyage) field.get(null);
                    ALL.put(voyage.getVoyageNumber(), voyage);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static List<Voyage> getAll() {
        return new ArrayList(ALL.values());
    }

    public static Voyage lookup(VoyageNumber voyageNumber) {
        return ALL.get(voyageNumber);
    }
}
