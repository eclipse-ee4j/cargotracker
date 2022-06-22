package org.eclipse.cargotracker.domain.model.cargo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.eclipse.cargotracker.domain.model.location.SampleLocations;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.junit.Test;

public class RouteSpecificationTest {

  Voyage hongKongTokyoNewYork =
      new Voyage.Builder(new VoyageNumber("V001"), SampleLocations.HONGKONG)
          .addMovement(
              SampleLocations.TOKYO,
              LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(1),
              LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(5))
          .addMovement(
              SampleLocations.NEWYORK,
              LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(6),
              LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(10))
          .addMovement(
              SampleLocations.HONGKONG,
              LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(11),
              LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(14))
          .build();
  Voyage dallasNewYorkChicago =
      new Voyage.Builder(new VoyageNumber("V002"), SampleLocations.DALLAS)
          .addMovement(
              SampleLocations.NEWYORK,
              LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(6),
              LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(7))
          .addMovement(
              SampleLocations.CHICAGO,
              LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(12),
              LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(20))
          .build();
  Itinerary itinerary =
      new Itinerary(
          Arrays.asList(
              new Leg(
                  hongKongTokyoNewYork,
                  SampleLocations.HONGKONG,
                  SampleLocations.NEWYORK,
                  LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(1),
                  LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(10)),
              new Leg(
                  dallasNewYorkChicago,
                  SampleLocations.NEWYORK,
                  SampleLocations.CHICAGO,
                  LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(12),
                  LocalDateTime.now().minusYears(1).plusMonths(2).plusDays(20))));

  @Test
  public void testIsSatisfiedBySuccess() {
    RouteSpecification routeSpecification =
        new RouteSpecification(
            SampleLocations.HONGKONG,
            SampleLocations.CHICAGO,
            LocalDate.now().minusYears(1).plusMonths(3).plusDays(1));

    assertTrue(routeSpecification.isSatisfiedBy(itinerary));
  }

  @Test
  public void testIsNotSatisfiedByWrongOrigin() {
    RouteSpecification routeSpecification =
        new RouteSpecification(
            SampleLocations.HANGZOU,
            SampleLocations.CHICAGO,
            LocalDate.now().minusYears(1).plusMonths(3).plusDays(1));

    assertFalse(routeSpecification.isSatisfiedBy(itinerary));
  }

  @Test
  public void testIsNotSatisfiedByWrongDestination() {
    RouteSpecification routeSpecification =
        new RouteSpecification(
            SampleLocations.HONGKONG,
            SampleLocations.DALLAS,
            LocalDate.now().minusYears(1).plusMonths(3).plusDays(1));

    assertFalse(routeSpecification.isSatisfiedBy(itinerary));
  }

  @Test
  public void testIsNotSatisfiedByMissedDeadline() {
    RouteSpecification routeSpecification =
        new RouteSpecification(
            SampleLocations.HONGKONG,
            SampleLocations.CHICAGO,
            LocalDate.now().minusYears(1).plusMonths(2).plusDays(15));

    assertFalse(routeSpecification.isSatisfiedBy(itinerary));
  }
}
