package org.eclipse.cargotracker.domain.model.location;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

/**
 * United nations location code.
 *
 * <p>http://www.unece.org/cefact/locode/
 * http://www.unece.org/cefact/locode/DocColumnDescription.htm#LOCODE
 */
@Embeddable
public record UnLocode(
    @Column(name = "unlocode")
        @NotEmpty(message = "Location code must not be empty.")
        @Pattern(regexp = "[a-zA-Z]{2}[a-zA-Z2-9]{3}")
        String code)
    implements Serializable {

  private static final java.util.regex.Pattern VALID_PATTERN =
      java.util.regex.Pattern.compile("[a-zA-Z]{2}[a-zA-Z2-9]{3}");

  public UnLocode {
    Objects.requireNonNull(code, "Country and location may not be null.");
    if (!VALID_PATTERN.matcher(code).matches()) {
      throw new IllegalArgumentException(
          code + " is not a valid UN/LOCODE (does not match pattern)");
    }

    code = code.toUpperCase();
  }

  @Override
  public String toString() {
    return code;
  }
}
