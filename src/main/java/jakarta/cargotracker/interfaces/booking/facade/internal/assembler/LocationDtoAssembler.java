package jakarta.cargotracker.interfaces.booking.facade.internal.assembler;

import jakarta.cargotracker.domain.model.location.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LocationDtoAssembler {

    public jakarta.cargotracker.interfaces.booking.facade.dto.Location toDto(
            Location location) {
        return new jakarta.cargotracker.interfaces.booking.facade.dto.Location(
                location.getUnLocode().getIdString(), location.getName());
    }

    public List<jakarta.cargotracker.interfaces.booking.facade.dto.Location> toDtoList(
            List<Location> allLocations) {
        List<jakarta.cargotracker.interfaces.booking.facade.dto.Location> dtoList = new ArrayList<>(
                allLocations.size());

        for (Location location : allLocations) {
            dtoList.add(toDto(location));
        }

        Collections.sort(dtoList,
                new Comparator<jakarta.cargotracker.interfaces.booking.facade.dto.Location>() {

                    @Override
                    public int compare(
                            jakarta.cargotracker.interfaces.booking.facade.dto.Location location1,
                            jakarta.cargotracker.interfaces.booking.facade.dto.Location location2) {
                        return location1.getName().compareTo(location2.getName());
                    }
                });

        return dtoList;
    }
}
