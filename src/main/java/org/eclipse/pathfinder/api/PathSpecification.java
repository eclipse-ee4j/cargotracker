package org.eclipse.pathfinder.api;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.QueryParam;

@PathSpecification.NotSameLocation
public class PathSpecification {

  private static final String UNLOCODE_PATTERN_VIOLATION_MESSAGE =
      "UN location code value must be five characters long, "
          + "the first two must be alphabetic and "
          + "the last three must be alphanumeric(excluding 0 and 1).";

  @NotNull(message = "Missing origin UN location code.")
  @Pattern(
      regexp = "[a-zA-Z]{2}[a-zA-Z2-9]{3}",
      message = "Origin " + UNLOCODE_PATTERN_VIOLATION_MESSAGE)
  @QueryParam("origin")
  private String originUnLocode;

  @NotNull(message = "Missing destination UN location code.")
  @Pattern(
      regexp = "[a-zA-Z]{2}[a-zA-Z2-9]{3}",
      message = "Destination " + UNLOCODE_PATTERN_VIOLATION_MESSAGE)
  @QueryParam("destination")
  private String destinationUnLocode;

  // TODO [DDD] Apply regular expression validation.
  @Size(min = 8, max = 8, message = "Deadline value must be eight characters long.")
  @QueryParam("deadline")
  private String deadline;

  public PathSpecification() {};

  public String getOriginUnLocode() {
    return originUnLocode;
  }

  public void setOriginUnLocode(String originUnLocode) {
    this.originUnLocode = originUnLocode;
  }

  public String getDestinationUnLocode() {
    return destinationUnLocode;
  }

  public void setDestinationUnLocode(String destinationUnLocode) {
    this.destinationUnLocode = destinationUnLocode;
  }

  public String getDeadline() {
    return deadline;
  }

  public void setDeadline(String deadline) {
    this.deadline = deadline;
  }

  @Constraint(validatedBy = {NotSameLocationValidator.class})
  @Documented
  @Target({TYPE, METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
  @Retention(RUNTIME)
  public static @interface NotSameLocation {

    String message() default "Orign UN location code and destination one must not be the same.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
  }

  public static class NotSameLocationValidator
      implements ConstraintValidator<NotSameLocation, PathSpecification> {

    @Override
    public boolean isValid(PathSpecification value, ConstraintValidatorContext context) {
      return !value.getOriginUnLocode().equals(value.getDestinationUnLocode());
    }
  }
}
