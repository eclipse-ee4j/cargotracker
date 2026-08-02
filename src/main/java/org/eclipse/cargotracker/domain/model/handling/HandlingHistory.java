package org.eclipse.cargotracker.domain.model.handling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HandlingHistory {

  // Null object pattern.
  public static final HandlingHistory EMPTY = new HandlingHistory(List.of());
  private static final Comparator<HandlingEvent> BY_COMPLETION_TIME_COMPARATOR =
      Comparator.comparing(HandlingEvent::getCompletionTime);

  private final List<HandlingEvent> handlingEvents;

  public HandlingHistory(Collection<HandlingEvent> handlingEvents) {
    Objects.requireNonNull(handlingEvents, "Handling events are required.");

    this.handlingEvents = List.copyOf(new ArrayList<>(handlingEvents));
  }

  public List<HandlingEvent> getAllHandlingEvents() {
    return handlingEvents;
  }

  /**
   * @return A distinct list (no duplicate registrations) of handling events, ordered by completion
   *     time.
   */
  public List<HandlingEvent> getDistinctEventsByCompletionTime() {
    List<HandlingEvent> ordered =
        handlingEvents.stream()
            .distinct()
            .sorted(BY_COMPLETION_TIME_COMPARATOR)
            .collect(Collectors.toList());

    return List.copyOf(ordered);
  }

  /**
   * @return Most recently completed event, or null if the delivery history is empty.
   */
  public HandlingEvent getMostRecentlyCompletedEvent() {
    List<HandlingEvent> distinctEvents = getDistinctEventsByCompletionTime();

    if (distinctEvents.isEmpty()) {
      return null;
    } else {
      return distinctEvents.get(distinctEvents.size() - 1);
    }
  }

  private boolean sameValueAs(HandlingHistory other) {
    return other != null && this.handlingEvents.equals(other.handlingEvents);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || !(o instanceof HandlingHistory)) {
      return false;
    }

    HandlingHistory other = (HandlingHistory) o;

    return sameValueAs(other);
  }

  @Override
  public int hashCode() {
    return handlingEvents.hashCode();
  }
}
