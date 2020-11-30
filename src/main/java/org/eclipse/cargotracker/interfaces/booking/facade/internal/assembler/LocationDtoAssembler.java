package org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.cargotracker.domain.model.location.Location;

public class LocationDtoAssembler {

	public org.eclipse.cargotracker.interfaces.booking.facade.dto.Location toDto(Location location) {
		return new org.eclipse.cargotracker.interfaces.booking.facade.dto.Location(location.getUnLocode().getIdString(),
				location.getName());
	}

	public List<org.eclipse.cargotracker.interfaces.booking.facade.dto.Location> toDtoList(
			List<Location> allLocations) {
		List<org.eclipse.cargotracker.interfaces.booking.facade.dto.Location> dtoList = new ArrayList<>(
				allLocations.size());

		for (Location location : allLocations) {
			dtoList.add(toDto(location));
		}

		Collections.sort(dtoList, new Comparator<org.eclipse.cargotracker.interfaces.booking.facade.dto.Location>() {

			@Override
			public int compare(org.eclipse.cargotracker.interfaces.booking.facade.dto.Location location1,
					org.eclipse.cargotracker.interfaces.booking.facade.dto.Location location2) {
				return location1.getName().compareTo(location2.getName());
			}
		});

		return dtoList;
	}
}
