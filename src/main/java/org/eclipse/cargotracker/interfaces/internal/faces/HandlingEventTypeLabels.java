package org.eclipse.cargotracker.interfaces.internal.faces;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;

@ApplicationScoped
public class HandlingEventTypeLabels {

  @Named("labels.handlingEventType")
  @Produces
  private static final Map<HandlingEvent.Type, HandlingEvent.Type> HANDLING_EVENT_TYPE_LABELS =
      Collections.unmodifiableMap(
          Arrays.asList(HandlingEvent.Type.values())
              .stream()
              .collect(toMap(Function.identity(), Function.identity())));
}
