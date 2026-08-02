package org.eclipse.cargotracker.domain.model.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class UnLocodeTest {

  @Test
  public void testNormalizesToUpperCase() {
    assertEquals("USNYC", new UnLocode("usnyc").getIdString());
  }

  @Test
  public void testAcceptsDigitsInLocationPart() {
    assertEquals("US2A9", new UnLocode("us2a9").getIdString());
  }

  @Test
  public void testRejectsInvalidFormat() {
    assertThrows(IllegalArgumentException.class, () -> new UnLocode("US"));
    assertThrows(IllegalArgumentException.class, () -> new UnLocode("USNYCX"));
    assertThrows(IllegalArgumentException.class, () -> new UnLocode("1SNYC"));
  }

  @Test
  public void testRejectsNull() {
    assertThrows(NullPointerException.class, () -> new UnLocode(null));
  }
}
