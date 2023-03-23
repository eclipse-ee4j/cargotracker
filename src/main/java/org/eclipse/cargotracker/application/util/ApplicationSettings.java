package org.eclipse.cargotracker.application.util;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;

/**
 * Application settings. Although this is stored in the database, it is not a true part of the
 * domain model but simply an application infrastructure artifact.
 */
@Entity
public class ApplicationSettings implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private long id;

  // Fixes EclipseLink 4.0.1 mapping to Derby 10.15.2.0
  // Note that this table is not used in tests!!!
  @Column(columnDefinition = "boolean not null")
  private boolean sampleLoaded = false;

  public ApplicationSettings(long id) {
      this.id = id;
  }

  public ApplicationSettings() {

  }

  public boolean isSampleLoaded() {
    return sampleLoaded;
  }

  public void setSampleLoaded(boolean sampleLoaded) {
    this.sampleLoaded = sampleLoaded;
  }
}
