package jakarta.cargotracker.domain.model.handling;

import jakarta.cargotracker.application.util.DateUtil;
import jakarta.cargotracker.domain.model.cargo.Cargo;
import jakarta.cargotracker.domain.model.cargo.RouteSpecification;
import jakarta.cargotracker.domain.model.cargo.TrackingId;
import jakarta.cargotracker.domain.model.handling.HandlingEvent;
import jakarta.cargotracker.domain.model.handling.HandlingHistory;
import jakarta.cargotracker.domain.model.location.SampleLocations;
import jakarta.cargotracker.domain.model.voyage.Voyage;
import jakarta.cargotracker.domain.model.voyage.VoyageNumber;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

//TODO This set of tests is very trivial, consider removing them.
public class HandlingHistoryTest {

    Cargo cargo = new Cargo(new TrackingId("ABC"), new RouteSpecification(
            SampleLocations.SHANGHAI, SampleLocations.DALLAS,
            DateUtil.toDate("2009-04-01")));
    Voyage voyage = new Voyage.Builder(new VoyageNumber("X25"),
            SampleLocations.HONGKONG)
            .addMovement(SampleLocations.SHANGHAI, new Date(), new Date())
            .addMovement(SampleLocations.DALLAS, new Date(), new Date())
            .build();
    HandlingEvent event1 = new HandlingEvent(cargo,
            DateUtil.toDate("2009-03-05"), new Date(100),
            HandlingEvent.Type.LOAD, SampleLocations.SHANGHAI, voyage);
    HandlingEvent event1duplicate = new HandlingEvent(cargo,
            DateUtil.toDate("2009-03-05"), new Date(200),
            HandlingEvent.Type.LOAD, SampleLocations.SHANGHAI, voyage);
    HandlingEvent event2 = new HandlingEvent(cargo,
            DateUtil.toDate("2009-03-10"), new Date(150),
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
