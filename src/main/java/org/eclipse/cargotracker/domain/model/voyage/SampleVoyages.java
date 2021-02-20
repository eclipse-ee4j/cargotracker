package org.eclipse.cargotracker.domain.model.voyage;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.cargotracker.application.util.DateUtil;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.SampleLocations;

/** Sample carrier movements, for test purposes. */
// TODO [Jakarta EE 8] Move to the Java Date-Time API for date manipulation. Also avoid hard-coded
// dates.
public class SampleVoyages {

    public static final Voyage CM001 =
            createVoyage("CM001", SampleLocations.STOCKHOLM, SampleLocations.HAMBURG);
    public static final Voyage CM002 =
            createVoyage("CM002", SampleLocations.HAMBURG, SampleLocations.HONGKONG);
    public static final Voyage CM003 =
            createVoyage("CM003", SampleLocations.HONGKONG, SampleLocations.NEWYORK);
    public static final Voyage CM004 =
            createVoyage("CM004", SampleLocations.NEWYORK, SampleLocations.CHICAGO);
    public static final Voyage CM005 =
            createVoyage("CM005", SampleLocations.CHICAGO, SampleLocations.HAMBURG);
    public static final Voyage CM006 =
            createVoyage("CM006", SampleLocations.HAMBURG, SampleLocations.HANGZOU);
    public static final Voyage v100 =
            new Voyage.Builder(new VoyageNumber("V100"), SampleLocations.HONGKONG)
                    .addMovement(
                            SampleLocations.TOKYO,
                            DateUtil.toDateTime("2014-03-03", "00:00"),
                            DateUtil.toDateTime("2014-03-05", "00:00"))
                    .addMovement(
                            SampleLocations.NEWYORK,
                            DateUtil.toDateTime("2014-03-06", "00:00"),
                            DateUtil.toDateTime("2014-03-09", "00:00"))
                    .build();
    public static final Voyage v200 =
            new Voyage.Builder(new VoyageNumber("V200"), SampleLocations.TOKYO)
                    .addMovement(
                            SampleLocations.NEWYORK,
                            DateUtil.toDateTime("2014-03-06", "00:00"),
                            DateUtil.toDateTime("2014-03-08", "00:00"))
                    .addMovement(
                            SampleLocations.CHICAGO,
                            DateUtil.toDateTime("2014-03-10", "00:00"),
                            DateUtil.toDateTime("2014-03-14", "00:00"))
                    .addMovement(
                            SampleLocations.STOCKHOLM,
                            DateUtil.toDateTime("2014-03-14", "00:00"),
                            DateUtil.toDateTime("2014-03-16", "00:00"))
                    .build();
    public static final Voyage v300 =
            new Voyage.Builder(new VoyageNumber("V300"), SampleLocations.TOKYO)
                    .addMovement(
                            SampleLocations.ROTTERDAM,
                            DateUtil.toDateTime("2014-03-08", "00:00"),
                            DateUtil.toDateTime("2014-03-11", "00:00"))
                    .addMovement(
                            SampleLocations.HAMBURG,
                            DateUtil.toDateTime("2014-03-11", "00:00"),
                            DateUtil.toDateTime("2014-03-12", "00:00"))
                    .addMovement(
                            SampleLocations.MELBOURNE,
                            DateUtil.toDateTime("2014-03-14", "00:00"),
                            DateUtil.toDateTime("2014-03-18", "00:00"))
                    .addMovement(
                            SampleLocations.TOKYO,
                            DateUtil.toDateTime("2014-03-19", "00:00"),
                            DateUtil.toDateTime("2014-03-21", "00:00"))
                    .build();
    public static final Voyage v400 =
            new Voyage.Builder(new VoyageNumber("V400"), SampleLocations.HAMBURG)
                    .addMovement(
                            SampleLocations.STOCKHOLM,
                            DateUtil.toDateTime("2014-03-14", "00:00"),
                            DateUtil.toDateTime("2014-03-15", "00:00"))
                    .addMovement(
                            SampleLocations.HELSINKI,
                            DateUtil.toDateTime("2014-03-15", "00:00"),
                            DateUtil.toDateTime("2014-03-16", "00:00"))
                    .addMovement(
                            SampleLocations.HAMBURG,
                            DateUtil.toDateTime("2014-03-20", "00:00"),
                            DateUtil.toDateTime("2014-03-22", "00:00"))
                    .build();
    /**
     * Voyage number 0100S (by ship)
     *
     * <p>Hongkong - Hangzou - Tokyo - Melbourne - New York
     */
    public static final Voyage HONGKONG_TO_NEW_YORK =
            new Voyage.Builder(new VoyageNumber("0100S"), SampleLocations.HONGKONG)
                    .addMovement(
                            SampleLocations.HANGZOU,
                            DateUtil.toDateTime("2013-10-01", "12:00"),
                            DateUtil.toDateTime("2013-10-03", "14:30"))
                    .addMovement(
                            SampleLocations.TOKYO,
                            DateUtil.toDateTime("2013-10-03", "21:00"),
                            DateUtil.toDateTime("2013-10-06", "06:15"))
                    .addMovement(
                            SampleLocations.MELBOURNE,
                            DateUtil.toDateTime("2013-10-06", "11:00"),
                            DateUtil.toDateTime("2013-10-12", "11:30"))
                    .addMovement(
                            SampleLocations.NEWYORK,
                            DateUtil.toDateTime("2013-10-14", "12:00"),
                            DateUtil.toDateTime("2013-10-23", "23:10"))
                    .build();
    /**
     * Voyage number 0200T (by train)
     *
     * <p>New York - Chicago - Dallas
     */
    public static final Voyage NEW_YORK_TO_DALLAS =
            new Voyage.Builder(new VoyageNumber("0200T"), SampleLocations.NEWYORK)
                    .addMovement(
                            SampleLocations.CHICAGO,
                            DateUtil.toDateTime("2013-10-24", "07:00"),
                            DateUtil.toDateTime("2013-10-24", "17:45"))
                    .addMovement(
                            SampleLocations.DALLAS,
                            DateUtil.toDateTime("2013-10-24", "21:25"),
                            DateUtil.toDateTime("2013-10-25", "19:30"))
                    .build();
    /**
     * Voyage number 0300A (by airplane)
     *
     * <p>Dallas - Hamburg - Stockholm - Helsinki
     */
    public static final Voyage DALLAS_TO_HELSINKI =
            new Voyage.Builder(new VoyageNumber("0300A"), SampleLocations.DALLAS)
                    .addMovement(
                            SampleLocations.HAMBURG,
                            DateUtil.toDateTime("2013-10-29", "03:30"),
                            DateUtil.toDateTime("2013-10-31", "14:00"))
                    .addMovement(
                            SampleLocations.STOCKHOLM,
                            DateUtil.toDateTime("2013-11-01", "15:20"),
                            DateUtil.toDateTime("2013-11-01", "18:40"))
                    .addMovement(
                            SampleLocations.HELSINKI,
                            DateUtil.toDateTime("2013-11-02", "09:00"),
                            DateUtil.toDateTime("2013-11-02", "11:15"))
                    .build();
    /**
     * Voyage number 0301S (by ship)
     *
     * <p>Dallas - Hamburg - Stockholm - Helsinki, alternate route
     */
    public static final Voyage DALLAS_TO_HELSINKI_ALT =
            new Voyage.Builder(new VoyageNumber("0301S"), SampleLocations.DALLAS)
                    .addMovement(
                            SampleLocations.HELSINKI,
                            DateUtil.toDateTime("2013-10-29", "03:30"),
                            DateUtil.toDateTime("2013-11-05", "15:45"))
                    .build();
    /**
     * Voyage number 0400S (by ship)
     *
     * <p>Helsinki - Rotterdam - Shanghai - Hongkong
     */
    public static final Voyage HELSINKI_TO_HONGKONG =
            new Voyage.Builder(new VoyageNumber("0400S"), SampleLocations.HELSINKI)
                    .addMovement(
                            SampleLocations.ROTTERDAM,
                            DateUtil.toDateTime("2013-11-04", "05:50"),
                            DateUtil.toDateTime("2013-11-06", "14:10"))
                    .addMovement(
                            SampleLocations.SHANGHAI,
                            DateUtil.toDateTime("2013-11-10", "21:45"),
                            DateUtil.toDateTime("2013-11-22", "16:40"))
                    .addMovement(
                            SampleLocations.HONGKONG,
                            DateUtil.toDateTime("2013-11-24", "07:00"),
                            DateUtil.toDateTime("2013-11-28", "13:37"))
                    .build();

    public static final Map<VoyageNumber, Voyage> ALL = new HashMap<>();

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

    private static Voyage createVoyage(String id, Location from, Location to) {
        return new Voyage(
                new VoyageNumber(id),
                new Schedule(
                        Collections.singletonList(
                                new CarrierMovement(
                                        from, to, LocalDateTime.now(), LocalDateTime.now()))));
    }

    public static List<Voyage> getAll() {
        return new ArrayList<>(ALL.values());
    }

    public static Voyage lookup(VoyageNumber voyageNumber) {
        return ALL.get(voyageNumber);
    }
}
