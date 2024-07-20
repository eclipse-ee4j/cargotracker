package org.eclipse.cargotracker.domain.model.handling;

import org.eclipse.cargotracker.domain.model.location.UnLocode;

import java.io.Serial;

public class UnknownLocationException extends CannotCreateHandlingEventException {

  @Serial
  private static final long serialVersionUID = 1L;

  private final UnLocode unlocode;

  public UnknownLocationException(UnLocode unlocode) {
    this.unlocode = unlocode;
  }

  @Override
  public String getMessage() {
    return "No location with UN locode " + unlocode.getIdString() + " exists in the system.";
  }
}
