package org.eclipse.cargotracker.domain.model.handling;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.RouteSpecification;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.location.SampleLocations;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.junit.Test;

public class HandlingHistoryTest {

  Cargo cargo =
      new Cargo(
          new TrackingId("ABC"),
          new RouteSpecification(
              SampleLocations.SHANGHAI,
              SampleLocations.DALLAS,
              LocalDate.now().minusYears(1).plusMonths(4).plusDays(1)));
  Voyage voyage =
      new Voyage.Builder(new VoyageNumber("X25"), SampleLocations.HONGKONG)
          .addMovement(SampleLocations.SHANGHAI, LocalDateTime.now(), LocalDateTime.now())
          .addMovement(SampleLocations.DALLAS, LocalDateTime.now(), LocalDateTime.now())
          .build();
  HandlingEvent event1 =
      new HandlingEvent(
          cargo,
          LocalDateTime.now()
              .minusYears(1)
              .plusMonths(3)
              .plusDays(5)
              .truncatedTo(ChronoUnit.SECONDS),
          LocalDateTime.now().plusDays(100),
          HandlingEvent.Type.LOAD,
          SampleLocations.SHANGHAI,
          voyage);
  HandlingEvent event1duplicate =
      new HandlingEvent(
          cargo,
          LocalDateTime.now()
              .minusYears(1)
              .plusMonths(3)
              .plusDays(5)
              .truncatedTo(ChronoUnit.SECONDS),
          LocalDateTime.now().plusDays(200),
          HandlingEvent.Type.LOAD,
          SampleLocations.SHANGHAI,
          voyage);
  HandlingEvent event2 =
      new HandlingEvent(
          cargo,
          LocalDateTime.now()
              .minusYears(1)
              .plusMonths(3)
              .plusDays(10)
              .truncatedTo(ChronoUnit.SECONDS),
          LocalDateTime.now().plusDays(150),
          HandlingEvent.Type.UNLOAD,
          SampleLocations.DALLAS,
          voyage);
  HandlingHistory handlingHistory =
      new HandlingHistory(Arrays.asList(event2, event1, event1duplicate));

  @Test
  public void testDistinctEventsByCompletionTime() {
    assertEquals(
        Arrays.asList(event1, event2), handlingHistory.getDistinctEventsByCompletionTime());
  }

  @Test
  public void testMostRecentlyCompletedEvent() {
    assertEquals(event2, handlingHistory.getMostRecentlyCompletedEvent());
  }
}
