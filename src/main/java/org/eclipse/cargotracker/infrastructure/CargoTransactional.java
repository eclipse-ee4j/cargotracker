package org.eclipse.cargotracker.infrastructure;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.enterprise.inject.Stereotype;
import javax.transaction.Transactional;
import org.eclipse.cargotracker.domain.model.handling.CannotCreateHandlingEventException;

/**
 *
 * @author guillermo
 */
@Transactional(rollbackOn = CannotCreateHandlingEventException.class)
@Stereotype
@Retention(RUNTIME)
@Target(TYPE)
public @interface CargoTransactional {
}
