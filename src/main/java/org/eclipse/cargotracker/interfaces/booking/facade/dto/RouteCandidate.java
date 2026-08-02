package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;
import java.util.List;

/** DTO for presenting and selecting an itinerary from a collection of candidates. */
public class RouteCandidate implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<Leg> legs;

  public RouteCandidate(List<Leg> legs) {
    this.legs = List.copyOf(legs);
  }

  public List<Leg> getLegs() {
    return legs;
  }

  @Override
  public String toString() {
    return "RouteCandidate{" + "legs=" + legs + '}';
  }
}
