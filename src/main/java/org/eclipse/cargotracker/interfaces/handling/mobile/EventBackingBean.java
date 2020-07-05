package org.eclipse.cargotracker.interfaces.handling.mobile;

import org.eclipse.cargotracker.application.ApplicationEvents;
import org.eclipse.cargotracker.application.util.LocationUtil;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.location.UnLocode;
import org.eclipse.cargotracker.domain.model.voyage.Voyage;
import org.eclipse.cargotracker.domain.model.voyage.VoyageNumber;
import org.eclipse.cargotracker.domain.model.voyage.VoyageRepository;
import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.CargoRoute;
import org.eclipse.cargotracker.interfaces.booking.web.CargoDetails;
import org.eclipse.cargotracker.interfaces.handling.HandlingEventRegistrationAttempt;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author davidd
 */
@Named
@FlowScoped("eventLogger")
@Deprecated
public class EventBackingBean implements Serializable {

    @Inject
    private CargoDetails cargoDetails;

    @Inject
    private BookingServiceFacade bookingServiceFacade;

    @Inject
    private ApplicationEvents applicationEvents;

    @Inject
    private VoyageRepository voyageRepository;


    private List<CargoRoute> cargos;

    private String voyageNumber;
    private Date completionDate;
    private String eventType;
    private String location;
    private String trackId;
    private List<SelectItem> trackIds;
    private List<SelectItem> locations;
    private List<SelectItem> voyages;

    private boolean voyageSelectable = false;
    private boolean eventSubmitable = false;
    private boolean inputsOk = false; // used to check if the filled are ok
    boolean loadEventCondition = false;

    @PostConstruct
    public void init() {

        cargos = bookingServiceFacade.listAllCargos();

        // fill the TrackingId dropdown list
        trackIds = new ArrayList<>();
        for (CargoRoute route : cargos) {
            if (route.isRouted() && !route.isClaimed()) { // we just need getRoutedUnclaimedCargos
                String routedUnclaimedId = route.getTrackingId();
                trackIds.add(new SelectItem(routedUnclaimedId, routedUnclaimedId));
            }
        }

        // fill the Port dropdown list
        locations = new ArrayList<>();
        List<String> allLocations = LocationUtil.getLocationsCode();
        for (String tempLoc : allLocations) {
            locations.add(new SelectItem(tempLoc, tempLoc));
        }

        // fill the Voyage dropdown list (only needed for LOAD & UNLOAD events)
        List<Voyage> allVoyages = voyageRepository.findAll();
        List<SelectItem> allVoyagesModel = new ArrayList<>(allVoyages.size());
        for (Voyage voyage : allVoyages) {
            String voyageNumber = voyage.getVoyageNumber().getIdString();
            allVoyagesModel.add(new SelectItem(voyageNumber, voyageNumber));
        }
        this.voyages = allVoyagesModel;
    }

    public boolean isVoyageSelectable() {
        return voyageSelectable;
    }

    public boolean isInputsOk() {
        return inputsOk;
    }

    public boolean isEventSubmitable() {
        return eventSubmitable;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public void setVoyageNumber(String voyageNumber) {
        this.voyageNumber = voyageNumber;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getTrackId() {
        return trackId;
    }

    public List<SelectItem> getTrackIds() {
        return trackIds;
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public List<SelectItem> getVoyages() {
        return voyages;
    }

    public String getLocation() {
        return location;
    }

    public List<SelectItem> getLocations() {
        return locations;
    }

    public String getEventType() {
        return eventType;
    }

    public Date getCompletionDate() { // todo : can a completion be in the past?
        return completionDate;
    }

    public void restart() {

        trackId = null;
        voyageNumber = null;
        eventType = null;
        location = null;
        completionDate = null;
        voyageSelectable = false;
        loadEventCondition = false;
    }

    public String cancel() {
        return "";
    }

    public void updateListener() {
        checkConditions();
    }

    public void updateListenerEvent() {
        // Voyage should only be set for LOAD & UNLOAD events. For other events, it should be null
        loadEventCondition = false;
        voyageSelectable = false;

        if (eventType.contains("LOAD")) {
            // a voyage has been selected for LOAD/UNLOAD event, ok
            if (voyageNumber != null) {
                loadEventCondition = true;
            }
            voyageSelectable = true;
            //PF.current().ajax().update("firstForm:panelVoyage");
            PrimeFaces.current().ajax().update("firstForm:panelVoyage");
            //RequestContext.getCurrentInstance().update("firstForm:panelVoyage");
        } else {
            this.voyageNumber = null;
            loadEventCondition = true;
            voyageSelectable = false;
        }
        checkConditions();
        //RequestContext.getCurrentInstance().update("firstForm:panelVoyage,:firstScreen:nextBtn");
    }

    public void checkConditions() {
        //If event = LOAD or UNLOAD -> Voyage should be null
        if (trackId != null && eventType != null && location != null && (eventType != null && loadEventCondition)) {
            // All condition are Ok, Next screen button can be enable
            inputsOk = true;
            //PF.current().ajax().update("firstForm:panelVoyage,:firstScreen:nextBtn");
            PrimeFaces.current().ajax().update("firstForm:panelVoyage,:firstScreen:nextBtn");
            //RequestContext.getCurrentInstance().update("firstForm:panelVoyage,:firstScreen:nextBtn");

        } else {
            // All conditions are not OK!
            inputsOk = false;
        }
    }

    public void updateVoyage() {
        //RequestContext.getCurrentInstance().update("eventForm:voyage");
        checkConditions();
    }

    public void timeSet() {
        eventSubmitable = true;
    }

    public void handleEventSubmission() {

        VoyageNumber selectedVoyage = null;

        //Date completionTime = new SimpleDateFormat(ISO_8601_FORMAT).parse(completionDate);
        Date registrationTime = new Date();
        TrackingId trackingId = new TrackingId(trackId);
        UnLocode unLocode = new UnLocode(this.location);
        HandlingEvent.Type type = HandlingEvent.Type.valueOf(eventType);

        if (voyageNumber != null) {  // Only Load & Unload could have a Voyage set
            selectedVoyage = new VoyageNumber(voyageNumber);
        }

        HandlingEventRegistrationAttempt attempt
                = new HandlingEventRegistrationAttempt(registrationTime, completionDate, trackingId, selectedVoyage, type, unLocode);

        applicationEvents.receivedHandlingEventRegistrationAttempt(attempt);

        voyageNumber = null;
        completionDate = null;
        eventType = null;
        location = null;
        trackId = null;
        eventSubmitable = loadEventCondition = voyageSelectable = inputsOk = false;

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Event submitted", ""));
    }

}
