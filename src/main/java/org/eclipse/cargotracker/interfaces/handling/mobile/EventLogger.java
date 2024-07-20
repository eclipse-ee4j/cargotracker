package org.eclipse.cargotracker.interfaces.handling.mobile;

import static java.util.stream.Collectors.toMap;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.eclipse.cargotracker.application.ApplicationEvents;
import org.eclipse.cargotracker.application.util.DateConverter;
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

  @Serial private static final long serialVersionUID = 1L;

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
        Arrays.stream(HandlingEvent.Type.values())
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
    trackingIds =
        cargoRepository.findAll().stream()
            .filter(cargo -> !cargo.getItinerary().getLegs().isEmpty())
            .filter(
                cargo ->
                    !cargo.getDelivery().getTransportStatus().sameValueAs(TransportStatus.CLAIMED))
            .map(cargo -> cargo.getTrackingId().getIdString())
            .map(this::buildSelectItem)
            .toList();

    this.locations = locationRepository.findAll().stream().map(this::buildSelectItem).toList();

    this.voyages =
        voyageRepository.findAll().stream()
            .map(Voyage::getVoyageNumber)
            .map(VoyageNumber::getIdString)
            .map(this::buildSelectItem)
            .toList();
  }

  private SelectItem buildSelectItem(String trackingIdLocal) {
    return new SelectItem(trackingIdLocal, trackingIdLocal);
  }

  private SelectItem buildSelectItem(Location locationLocal) {
    String locationCode = locationLocal.getUnLocode().getIdString();
    return buildSelectItem(locationCode);
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

    if (eventType.requiresVoyage()) {
      voyage = new VoyageNumber(voyageNumber);
    } else {
      voyage = null;
    }

    HandlingEventRegistrationAttempt attempt =
        new HandlingEventRegistrationAttempt(
            LocalDateTime.now(),
            completionTime,
            new TrackingId(this.trackingId),
            voyage,
            eventType,
            new UnLocode(this.location));

    applicationEvents.receivedHandlingEventRegistrationAttempt(attempt);

    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Event submitted", ""));
  }
}
