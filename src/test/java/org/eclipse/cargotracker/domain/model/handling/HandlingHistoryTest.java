package org.eclipse.cargotracker.domain.model.handling;

import org.eclipse.cargotracker.application.util.DateUtil;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.RouteSpecification;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.location.SampleLocations;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;


//TODO This set of tests is very trivial, consider removing them.
public class HandlingHistoryTest {

    Cargo cargo = new Cargo(new TrackingId("ABC"), new RouteSpecification(
            SampleLocations.SHANGHAI, SampleLocations.DALLAS,
            DateUtil.toDate("2009-04-01")));
    Voyage voyage = new Voyage.Builder(new VoyageNumber("X25"),
            SampleLocations.HONGKONG)
            .addMovement(SampleLocations.SHANGHAI, LocalDateTime.now(), LocalDateTime.now())
            .addMovement(SampleLocations.DALLAS, LocalDateTime.now(), LocalDateTime.now())
            .build();
    HandlingEvent event1 = new HandlingEvent(cargo,
            DateUtil.toDate("2009-03-05"), LocalDateTime.ofEpochSecond(0, 100, zoneOffset()),
            HandlingEvent.Type.LOAD, SampleLocations.SHANGHAI, voyage);

    private ZoneOffset zoneOffset() {
        OffsetDateTime odt = OffsetDateTime.now();
        return odt.getOffset();
    }

    HandlingEvent event1duplicate = new HandlingEvent(cargo,
            DateUtil.toDate("2009-03-05"), LocalDateTime.ofEpochSecond(0, 200, zoneOffset()),
            HandlingEvent.Type.LOAD, SampleLocations.SHANGHAI, voyage);
    HandlingEvent event2 = new HandlingEvent(cargo,
            DateUtil.toDate("2009-03-10"), LocalDateTime.ofEpochSecond(0, 150, zoneOffset()),
            HandlingEvent.Type.UNLOAD, SampleLocations.DALLAS, voyage);
    HandlingHistory handlingHistory = new HandlingHistory(Arrays.asList(event2,
            event1, event1duplicate));

    @Test
    public void testDistinctEventsByCompletionTime() {
        assertEquals(Arrays.asList(event1, event2),
                handlingHistory.getDistinctEventsByCompletionTime());
    }

    @Test
    public void testMostRecentlyCompletedEvent() {
        assertEquals(event2, handlingHistory.getMostRecentlyCompletedEvent());
    }
}
