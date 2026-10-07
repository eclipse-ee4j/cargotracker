package org.eclipse.cargotracker.domain.model.handling;

import org.eclipse.cargotracker.domain.model.location.UnLocode;

public class UnknownLocationException extends CannotCreateHandlingEventException {

  private final UnLocode unlocode;

  public UnknownLocationException(UnLocode unlocode) {
    this.unlocode = unlocode;
  }

  @Override
  public String getMessage() {
    return "No location with UN locode " + unlocode + " exists in the system.";
  }
}
