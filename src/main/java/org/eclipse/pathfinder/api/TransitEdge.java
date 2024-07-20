package org.eclipse.pathfinder.api;

import java.io.Serializable;
import java.time.LocalDateTime;

/** Represents an edge in a path through a graph, describing the route of a cargo. */
public record TransitEdge(
        String voyageNumber,
        String fromUnLocode,
        String toUnLocode,
        LocalDateTime fromDate,
        LocalDateTime toDate) implements Serializable {
}
