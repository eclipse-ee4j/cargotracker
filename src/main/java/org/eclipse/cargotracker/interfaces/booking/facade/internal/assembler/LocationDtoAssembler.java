package org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Comparator;
import java.util.List;
import org.eclipse.cargotracker.domain.model.location.Location;

@ApplicationScoped
public class LocationDtoAssembler {

  public org.eclipse.cargotracker.interfaces.booking.facade.dto.Location toDto(Location location) {
    return new org.eclipse.cargotracker.interfaces.booking.facade.dto.Location(
        location.getUnLocode().getIdString(), location.getName());
  }

  public List<org.eclipse.cargotracker.interfaces.booking.facade.dto.Location> toDtoList(
      List<Location> allLocations) {

    return allLocations.stream()
        .map(this::toDto)
        .sorted(
            Comparator.comparing(
                org.eclipse.cargotracker.interfaces.booking.facade.dto.Location::getUnLocode))
        .toList();
  }
}
