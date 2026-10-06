package org.eclipse.pathfinder.api;

import java.io.Serializable;
import java.util.List;

public record TransitPath(List<TransitEdge> transitEdges) implements Serializable {}
