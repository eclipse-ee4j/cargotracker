package org.eclipse.cargotracker.interfaces.handling.mobile;

import static java.util.stream.Collectors.toMap;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.cargotracker.application.ApplicationEvents;
import org.eclipse.cargotracker.application.util.DateConverter;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.CargoRepository;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.cargo.TransportStatus;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.location.Location;
import org.eclipse.cargotracker.domain.model.location.LocationRepository;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.eclipse.cargotracker.domain.model.voyage.VoyageRepository;
import org.eclipse.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;
import org.primefaces.event.FlowEvent;

@Named
@ViewScoped
public class EventLogger implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject private CargoRepository cargoRepository;
  @Inject private LocationRepository locationRepository;
  @Inject private VoyageRepository voyageRepository;
  @Inject private ApplicationEvents applicationEvents;
  @Inject private FacesContext context;

  private List<SelectItem> trackingIds;
  private List<SelectItem> locations;
  private List<SelectItem> voyages;

  private String trackingId;
  private String location;
  private HandlingEvent.Type eventType;
  private String voyageNumber;
  private LocalDateTime completionTime;

  public String getTrackingId() {
    return trackingId;
  }

  public void setTrackingId(String trackingId) {
    this.trackingId = trackingId;
  }

  public List<SelectItem> getTrackingIds() {
    return trackingIds;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public List<SelectItem> getLocations() {
    return locations;
  }

  // Move this to a separate utility if it is used in other parts of the UI.
  public Map<HandlingEvent.Type, HandlingEvent.Type> getEventTypes() {
    return Collections.unmodifiableMap(
        Arrays.asList(HandlingEvent.Type.values())
            .stream()
            .collect(toMap(Function.identity(), Function.identity())));
  }

  public HandlingEvent.Type getEventType() {
    return eventType;
  }

  public void setEventType(HandlingEvent.Type eventType) {
    this.eventType = eventType;
  }

  public String getVoyageNumber() {
    return voyageNumber;
  }

  public void setVoyageNumber(String voyageNumber) {
    this.voyageNumber = voyageNumber;
  }

  public List<SelectItem> getVoyages() {
    return voyages;
  }

  public LocalDateTime getCompletionTime() {
    return completionTime;
  }

  public void setCompletionTime(LocalDateTime completionTime) {
    this.completionTime = completionTime;
  }

  public String getCompletionTimeValue() {
    return DateConverter.toString(completionTime);
  }

  public String getCompletionTimePattern() {
    return DateConverter.DATE_TIME_FORMAT;
  }

  @PostConstruct
  public void init() {
    List<Cargo> cargos = cargoRepository.findAll();

    trackingIds = new ArrayList<>(cargos.size());

    // List only routed cargo that is not claimed yet.
    cargos
        .stream()
        .filter(
            cargo ->
                !cargo.getItinerary().getLegs().isEmpty()
                    && !(cargo
                        .getDelivery()
                        .getTransportStatus()
                        .sameValueAs(TransportStatus.CLAIMED)))
        .map(cargo -> cargo.getTrackingId().getIdString())
        .forEachOrdered(trackingId -> trackingIds.add(new SelectItem(trackingId, trackingId)));

    List<Location> locations = locationRepository.findAll();

    this.locations = new ArrayList<>(locations.size());

    locations.forEach(
        location -> {
          String locationCode = location.getUnLocode().getIdString();
          this.locations.add(
              new SelectItem(locationCode, location.getName() + " (" + locationCode + ")"));
        });

    List<Voyage> voyages = voyageRepository.findAll();

    this.voyages = new ArrayList<>(voyages.size());

    voyages.forEach(
        voyage ->
            this.voyages.add(
                new SelectItem(
                    voyage.getVoyageNumber().getIdString(),
                    voyage.getVoyageNumber().getIdString())));
  }

  public String onFlowProcess(FlowEvent event) {
    if (!validate(event.getOldStep())) {
      return event.getOldStep();
    }

    if ("dateTab".equals(event.getNewStep())) {
      completionTime = LocalDateTime.now();
    }

    return event.getNewStep();
  }

  private boolean validate(final String step) {
    if ("voyageTab".equals(step) && eventType.requiresVoyage() && voyageNumber == null) {
      FacesMessage message =
          new FacesMessage(
              FacesMessage.SEVERITY_ERROR,
              "When a cargo is LOADed or UNLOADed a Voyage should be selected, please fix errors to continue.",
              "");
      context.addMessage(null, message);
      return false;
    }

    return true;
  }

  public void submit() {
    VoyageNumber voyage;

    TrackingId trackingId = new TrackingId(this.trackingId);
    UnLocode location = new UnLocode(this.location);

    if (eventType.requiresVoyage()) {
      voyage = new VoyageNumber(voyageNumber);
    } else {
      voyage = null;
    }

    HandlingEventRegistrationAttempt attempt =
        new HandlingEventRegistrationAttempt(
            LocalDateTime.now(), completionTime, trackingId, voyage, eventType, location);

    applicationEvents.receivedHandlingEventRegistrationAttempt(attempt);

    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Event submitted", ""));
  }
}
