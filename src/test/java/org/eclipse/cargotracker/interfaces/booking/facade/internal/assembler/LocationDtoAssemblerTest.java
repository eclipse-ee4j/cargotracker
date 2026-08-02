package org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.junit.jupiter.api.Test;

public class LocationDtoAssemblerTest {

  private final LocationDtoAssembler assembler = new LocationDtoAssembler();

  @Test
  public void testToDtoMapsUnLocodeAndName() {
    Location newYork = new Location(new UnLocode("USNYC"), "New York");

    org.eclipse.cargotracker.interfaces.booking.facade.dto.Location dto = assembler.toDto(newYork);

    assertEquals("USNYC", dto.getUnLocode());
    assertEquals("New York", dto.getName());
  }

  @Test
  public void testToDtoListIsSortedByUnLocode() {
    List<Location> domainLocations =
        List.of(
            new Location(new UnLocode("USNYC"), "New York"),
            new Location(new UnLocode("DEHAM"), "Hamburg"),
            new Location(new UnLocode("JPTKO"), "Tokyo"));

    List<String> unLocodes =
        assembler.toDtoList(domainLocations).stream()
            .map(org.eclipse.cargotracker.interfaces.booking.facade.dto.Location::getUnLocode)
            .collect(Collectors.toList());

    assertEquals(List.of("DEHAM", "JPTKO", "USNYC"), unLocodes);
  }
}
