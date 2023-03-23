package org.eclipse.cargotracker.manualtest;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

/**
 * @author David Matejcek
 */
public class WaitForBreak {

  private static final Logger LOG = System.getLogger(WaitForBreak.class.getName());

  static {
    Thread.currentThread().setName("main");
  }

  private WaitForBreak() {
    // hidden
  }

  /**
   * Starts one or more docker containers with the usage of local PostgreSQL database.
   *
   * @param args not used.
   * @throws Exception
   */
  public static void main(final String[] args) throws Exception {
    try {
      while (true) {
        Thread.sleep(100L);
      }
    } finally {
      LOG.log(Level.INFO, "Interrupted.");
    }
  }
}
