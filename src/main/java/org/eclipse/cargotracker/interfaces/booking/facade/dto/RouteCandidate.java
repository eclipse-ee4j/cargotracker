package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for presenting and selecting an itinerary from a collection of candidates.
 */
public record RouteCandidate(List<Leg> legs) implements Serializable {
}
