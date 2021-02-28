package org.eclipse.cargotracker.interfaces.booking.facade.internal.assembler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.cargotracker.application.util.DateUtil;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.Delivery;
import org.eclipse.cargotracker.domain.model.cargo.HandlingActivity;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoStatus;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.TrackingEvents;

// TODO [Clean Code] Could this be a CDI singleton?
public class CargoStatusDtoAssembler {

  public CargoStatus toDto(Cargo cargo, List<HandlingEvent> handlingEvents) {
    List<TrackingEvents> trackingEvents;

    TrackingEventsDtoAssembler assembler = new TrackingEventsDtoAssembler();

    trackingEvents =
        handlingEvents
            .stream()
            .map(handlingEvent -> assembler.toDto(cargo, handlingEvent))
            .collect(Collectors.toList());

    return new CargoStatus(
        cargo.getTrackingId().getIdString(),
        cargo.getRouteSpecification().getDestination().getName(),
        getCargoStatusText(cargo),
        cargo.getDelivery().isMisdirected(),
        getEta(cargo),
        getNextExpectedActivity(cargo),
        trackingEvents);
  }

  private String getCargoStatusText(Cargo cargo) {
    Delivery delivery = cargo.getDelivery();

    switch (delivery.getTransportStatus()) {
      case IN_PORT:
        return "In port " + delivery.getLastKnownLocation().getName();
      case ONBOARD_CARRIER:
        return "Onboard voyage " + delivery.getCurrentVoyage().getVoyageNumber().getIdString();
      case CLAIMED:
        return "Claimed";
      case NOT_RECEIVED:
        return "Not received";
      case UNKNOWN:
        return "Unknown";
      default:
        return "[Unknown status]"; // Should never happen.
    }
  }

  private String getEta(Cargo cargo) {
    LocalDateTime eta = cargo.getDelivery().getEstimatedTimeOfArrival();

    if (eta == null) {
      return "?";
    } else {
      return DateUtil.toString(eta);
    }
  }

  private String getNextExpectedActivity(Cargo cargo) {
    HandlingActivity activity = cargo.getDelivery().getNextExpectedActivity();

    if ((activity == null) || (activity.isEmpty())) {
      return "";
    }

    String text = "Next expected activity is to ";
    HandlingEvent.Type type = activity.getType();

    if (type.sameValueAs(HandlingEvent.Type.LOAD)) {
      return text
          + type.name().toLowerCase()
          + " cargo onto voyage "
          + activity.getVoyage().getVoyageNumber()
          + " in "
          + activity.getLocation().getName();
    } else if (type.sameValueAs(HandlingEvent.Type.UNLOAD)) {
      return text
          + type.name().toLowerCase()
          + " cargo off of "
          + activity.getVoyage().getVoyageNumber()
          + " in "
          + activity.getLocation().getName();
    } else {
      return text + type.name().toLowerCase() + " cargo in " + activity.getLocation().getName();
    }
  }
}
