package org.eclipse.cargotracker.interfaces.booking.web;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.eclipse.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import org.eclipse.cargotracker.interfaces.booking.facade.dto.Location;
import org.primefaces.PrimeFaces;

/**
 * Handles booking cargo. Operates against a dedicated service facade, and could easily be rewritten
 * as a thick client. Completely separated from the domain layer, unlike the tracking user
 * interface.
 *
 * <p>In order to successfully keep the domain model shielded from user interface considerations,
 * this approach is generally preferred to the one taken in the tracking controller. However, there
 * is never any one perfect solution for all situations, so we've chosen to demonstrate two
 * polarized ways to build user interfaces.
 */
@Named
@FlowScoped("booking")
public class Booking implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final long MIN_JOURNEY_DURATION = 1; // Journey should be 1 day minimum.

  @Inject private BookingServiceFacade bookingServiceFacade;
  @Inject private FacesContext context;

  private LocalDate today = null;
  private List<Location> locations;

  private String originUnlocode;
  private String originName;
  private String destinationName;
  private String destinationUnlocode;
  private LocalDate arrivalDeadline;

  private boolean bookable = false;
  private long duration = -1;

  @PostConstruct
  public void init() {
    today = LocalDate.now();
    locations = bookingServiceFacade.listShippingLocations();
  }

  public List<Location> getLocations() {
    List<Location> filteredLocations = new ArrayList<>();
    String locationToRemove = null;

    if (context.getViewRoot().getViewId().endsWith("destination.xhtml")) {
      // In the destination menu, origin can't be selected.
      locationToRemove = originUnlocode;
    } else { // Vice-versa.
      if (destinationUnlocode != null) {
        locationToRemove = destinationUnlocode;
      }
    }

    for (Location location : locations) {
      if (!location.getUnLocode().equalsIgnoreCase(locationToRemove)) {
        filteredLocations.add(location);
      }
    }

    return filteredLocations;
  }

  public String getOriginUnlocode() {
    return originUnlocode;
  }

  public void setOriginUnlocode(String originUnlocode) {
    this.originUnlocode = originUnlocode;
    for (Location location : locations) {
      if (location.getUnLocode().equalsIgnoreCase(originUnlocode)) {
        this.originName = location.getNameOnly();
      }
    }
  }

  public String getOriginName() {
    return originName;
  }

  public String getDestinationUnlocode() {
    return destinationUnlocode;
  }

  public void setDestinationUnlocode(String destinationUnlocode) {
    this.destinationUnlocode = destinationUnlocode;
    for (Location location : locations) {
      if (location.getUnLocode().equalsIgnoreCase(destinationUnlocode)) {
        destinationName = location.getNameOnly();
      }
    }
  }

  public String getDestinationName() {
    return destinationName;
  }

  public LocalDate getToday() {
    return today;
  }

  public LocalDate getArrivalDeadline() {
    return arrivalDeadline;
  }

  public void setArrivalDeadline(LocalDate arrivalDeadline) {
    this.arrivalDeadline = arrivalDeadline;
  }

  public long getDuration() {
    return duration;
  }

  public boolean isBookable() {
    return bookable;
  }

  public void deadlineUpdated() {
    duration = ChronoUnit.DAYS.between(today, arrivalDeadline);

    if (duration >= MIN_JOURNEY_DURATION) {
      bookable = true;
    } else {
      bookable = false;
    }

    PrimeFaces.current().ajax().update("dateForm:durationPanel");
    PrimeFaces.current().ajax().update("dateForm:bookBtn");
  }

  public String register() {
    if (!originUnlocode.equals(destinationUnlocode)) {
      bookingServiceFacade.bookNewCargo(originUnlocode, destinationUnlocode, arrivalDeadline);
    } else {
      // UI now prevents from selecting same origin/destination
      FacesMessage message = new FacesMessage("Origin and destination cannot be the same.");
      message.setSeverity(FacesMessage.SEVERITY_ERROR);
      context.addMessage(null, message);
      return null;
    }

    return "/admin/dashboard.xhtml";
  }

  public String getReturnValue() {
    return "/admin/dashboard.xhtml";
  }
}
