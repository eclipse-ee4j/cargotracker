package org.eclipse.cargotracker.domain.model.handling;

import javax.ejb.ApplicationException;

/**
 * If a {@link HandlingEvent} can't be created from a given set of parameters.
 *
 * <p>It is a checked exception because it's not a programming error, but rather a special case that
 * the application is built to handle. It can occur during normal program execution.
 */
@ApplicationException(rollback = true)
public class CannotCreateHandlingEventException extends Exception {

  private static final long serialVersionUID = 1L;

  public CannotCreateHandlingEventException(Exception e) {
    super(e);
  }

  public CannotCreateHandlingEventException() {
    super();
  }
}
