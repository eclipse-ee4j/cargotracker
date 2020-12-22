package org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler;

import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoTracking;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.HandlingTrackingEvents;

import java.util.ArrayList;
import java.util.List;

public class CargoTrackingDtoAssembler {

    public CargoTracking toDto(TrackingId trackingId, Cargo cargo, List<HandlingEvent> handlingEvents) {
        List<HandlingTrackingEvents> trackingEvents = new ArrayList<>(handlingEvents.size());

        HandlingTrackingEventsDtoAssembler assembler = new HandlingTrackingEventsDtoAssembler();

        for (HandlingEvent handlingEvent : handlingEvents) {
            trackingEvents.add(assembler.toDto(cargo, handlingEvent));
        }

        return new CargoTracking(cargo, trackingEvents);
    }
}
