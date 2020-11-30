package org.eclipse.cargotracker.domain.model.voyage;

import static org.eclipse.cargotracker.application.util.DateUtil.toDate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.SampleLocations;

/**
 * Sample carrier movements, for test purposes.
 */
//TODO [Jakarta EE 8] Move to the Java Date-Time API for date manipulation. Also avoid hard-coded dates.
public class SampleVoyages {

	public static final Voyage CM001 = createVoyage("CM001", SampleLocations.STOCKHOLM, SampleLocations.HAMBURG);
	public static final Voyage CM002 = createVoyage("CM002", SampleLocations.HAMBURG, SampleLocations.HONGKONG);
	public static final Voyage CM003 = createVoyage("CM003", SampleLocations.HONGKONG, SampleLocations.NEWYORK);
	public static final Voyage CM004 = createVoyage("CM004", SampleLocations.NEWYORK, SampleLocations.CHICAGO);
	public static final Voyage CM005 = createVoyage("CM005", SampleLocations.CHICAGO, SampleLocations.HAMBURG);
	public static final Voyage CM006 = createVoyage("CM006", SampleLocations.HAMBURG, SampleLocations.HANGZOU);

	private static Voyage createVoyage(String id, Location from, Location to) {
		return new Voyage(new VoyageNumber(id),
				new Schedule(Arrays.asList(new CarrierMovement(from, to, new Date(), new Date()))));
	}

	public final static Voyage v100 = new Voyage.Builder(new VoyageNumber("V100"), SampleLocations.HONGKONG)
			.addMovement(SampleLocations.TOKYO, toDate("2014-03-03"), toDate("2014-03-05"))
			.addMovement(SampleLocations.NEWYORK, toDate("2014-03-06"), toDate("2014-03-09")).build();
	public final static Voyage v200 = new Voyage.Builder(new VoyageNumber("V200"), SampleLocations.TOKYO)
			.addMovement(SampleLocations.NEWYORK, toDate("2014-03-06"), toDate("2014-03-08"))
			.addMovement(SampleLocations.CHICAGO, toDate("2014-03-10"), toDate("2014-03-14"))
			.addMovement(SampleLocations.STOCKHOLM, toDate("2014-03-14"), toDate("2014-03-16")).build();
	public final static Voyage v300 = new Voyage.Builder(new VoyageNumber("V300"), SampleLocations.TOKYO)
			.addMovement(SampleLocations.ROTTERDAM, toDate("2014-03-08"), toDate("2014-03-11"))
			.addMovement(SampleLocations.HAMBURG, toDate("2014-03-11"), toDate("2014-03-12"))
			.addMovement(SampleLocations.MELBOURNE, toDate("2014-03-14"), toDate("2014-03-18"))
			.addMovement(SampleLocations.TOKYO, toDate("2014-03-19"), toDate("2014-03-21")).build();
	public final static Voyage v400 = new Voyage.Builder(new VoyageNumber("V400"), SampleLocations.HAMBURG)
			.addMovement(SampleLocations.STOCKHOLM, toDate("2014-03-14"), toDate("2014-03-15"))
			.addMovement(SampleLocations.HELSINKI, toDate("2014-03-15"), toDate("2014-03-16"))
			.addMovement(SampleLocations.HAMBURG, toDate("2014-03-20"), toDate("2014-03-22")).build();
	/**
	 * Voyage number 0100S (by ship)
	 * <p>
	 * Hongkong - Hangzou - Tokyo - Melbourne - New York
	 */
	public static final Voyage HONGKONG_TO_NEW_YORK = new Voyage.Builder(new VoyageNumber("0100S"),
			SampleLocations.HONGKONG)
					.addMovement(SampleLocations.HANGZOU, toDate("2013-10-01", "12:00"), toDate("2013-10-03", "14:30"))
					.addMovement(SampleLocations.TOKYO, toDate("2013-10-03", "21:00"), toDate("2013-10-06", "06:15"))
					.addMovement(SampleLocations.MELBOURNE, toDate("2013-10-06", "11:00"),
							toDate("2013-10-12", "11:30"))
					.addMovement(SampleLocations.NEWYORK, toDate("2013-10-14", "12:00"), toDate("2013-10-23", "23:10"))
					.build();
	/**
	 * Voyage number 0200T (by train)
	 * <p>
	 * New York - Chicago - Dallas
	 */
	public static final Voyage NEW_YORK_TO_DALLAS = new Voyage.Builder(new VoyageNumber("0200T"),
			SampleLocations.NEWYORK)
					.addMovement(SampleLocations.CHICAGO, toDate("2013-10-24", "07:00"), toDate("2013-10-24", "17:45"))
					.addMovement(SampleLocations.DALLAS, toDate("2013-10-24", "21:25"), toDate("2013-10-25", "19:30"))
					.build();
	/**
	 * Voyage number 0300A (by airplane)
	 * <p>
	 * Dallas - Hamburg - Stockholm - Helsinki
	 */
	public static final Voyage DALLAS_TO_HELSINKI = new Voyage.Builder(new VoyageNumber("0300A"),
			SampleLocations.DALLAS)
					.addMovement(SampleLocations.HAMBURG, toDate("2013-10-29", "03:30"), toDate("2013-10-31", "14:00"))
					.addMovement(SampleLocations.STOCKHOLM, toDate("2013-11-01", "15:20"),
							toDate("2013-11-01", "18:40"))
					.addMovement(SampleLocations.HELSINKI, toDate("2013-11-02", "09:00"), toDate("2013-11-02", "11:15"))
					.build();
	/**
	 * Voyage number 0301S (by ship)
	 * <p>
	 * Dallas - Hamburg - Stockholm - Helsinki, alternate route
	 */
	public static final Voyage DALLAS_TO_HELSINKI_ALT = new Voyage.Builder(new VoyageNumber("0301S"),
			SampleLocations.DALLAS)
					.addMovement(SampleLocations.HELSINKI, toDate("2013-10-29", "03:30"), toDate("2013-11-05", "15:45"))
					.build();
	/**
	 * Voyage number 0400S (by ship)
	 * <p>
	 * Helsinki - Rotterdam - Shanghai - Hongkong
	 */
	public static final Voyage HELSINKI_TO_HONGKONG = new Voyage.Builder(new VoyageNumber("0400S"),
			SampleLocations.HELSINKI)
					.addMovement(SampleLocations.ROTTERDAM, toDate("2013-11-04", "05:50"),
							toDate("2013-11-06", "14:10"))
					.addMovement(SampleLocations.SHANGHAI, toDate("2013-11-10", "21:45"), toDate("2013-11-22", "16:40"))
					.addMovement(SampleLocations.HONGKONG, toDate("2013-11-24", "07:00"), toDate("2013-11-28", "13:37"))
					.build();
	public static final Map<VoyageNumber, Voyage> ALL = new HashMap<VoyageNumber, Voyage>();

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
		return new ArrayList<Voyage>(ALL.values());
	}

	public static Voyage lookup(VoyageNumber voyageNumber) {
		return ALL.get(voyageNumber);
	}
}
