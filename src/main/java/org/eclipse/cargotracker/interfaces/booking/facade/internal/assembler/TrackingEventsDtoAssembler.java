package org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler;

import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.TrackingEvents;

import java.text.SimpleDateFormat;

public class TrackingEventsDtoAssembler {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");

    public TrackingEvents toDto(Cargo cargo, HandlingEvent handlingEvent) {
        String location = locationFrom(handlingEvent);
        HandlingEvent.Type type = handlingEvent.getType();
        String voyageNumber = voyageNumberFrom(handlingEvent);
        return new TrackingEvents(
                location,
                timeFrom(handlingEvent),
                handlingEvent.getType().toString(),
                voyageNumber,
                cargo.getItinerary().isExpected(handlingEvent),
                descriptionFrom(type, location, voyageNumber)
        );
    }

    private String descriptionFrom(HandlingEvent.Type type, String location, String voyageNumber) {
        switch (type) {
            case LOAD:
                return "Loaded onto voyage " + voyageNumber + " in "
                        + location;
            case UNLOAD:
                return "Unloaded off voyage " + voyageNumber + " in "
                        + location;
            case RECEIVE:
                return "Received in " + location;
            case CLAIM:
                return "Claimed in " + location;
            case CUSTOMS:
                return "Cleared customs in " + location;
            default:
                return "[Unknown]";
        }
    }

    private String voyageNumberFrom(HandlingEvent handlingEvent) {
        Voyage voyage = handlingEvent.getVoyage();
        return voyage.getVoyageNumber().getIdString();
    }

    private HandlingEvent.Type typeFrom(HandlingEvent handlingEvent) {
        return handlingEvent.getType();
    }

    private String timeFrom(HandlingEvent handlingEvent) {
        return DATE_FORMAT.format(handlingEvent.getCompletionTime());
    }

    private String locationFrom(HandlingEvent handlingEvent) {
        return handlingEvent.getLocation().getName();
    }
}
