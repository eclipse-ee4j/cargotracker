package org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.cargotracker.domain.model.location.Location;

public class LocationDtoAssembler {

  public org.eclipse.cargotracker.interfaces.booking.facade.dto.Location toDto(Location location) {
    return new org.eclipse.cargotracker.interfaces.booking.facade.dto.Location(
        location.getUnLocode().getIdString(), location.getName());
  }

  public List<org.eclipse.cargotracker.interfaces.booking.facade.dto.Location> toDtoList(
      List<Location> allLocations) {
    List<org.eclipse.cargotracker.interfaces.booking.facade.dto.Location> dtoList =
        allLocations
            .stream()
            .map(this::toDto)
            .sorted(
                Comparator.comparing(
                    org.eclipse.cargotracker.interfaces.booking.facade.dto.Location::getName))
            .collect(Collectors.toList());
    return dtoList;
  }
}
