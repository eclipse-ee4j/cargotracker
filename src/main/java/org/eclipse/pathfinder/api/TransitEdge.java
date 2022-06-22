package org.eclipse.pathfinder.api;

import java.io.Serializable;
import java.time.LocalDateTime;

/** Represents an edge in a path through a graph, describing the route of a cargo. */
public class TransitEdge implements Serializable {

  private static final long serialVersionUID = 1L;

  private String voyageNumber;
  private String fromUnLocode;
  private String toUnLocode;
  private LocalDateTime fromDate;
  private LocalDateTime toDate;

  public TransitEdge() {
    // Nothing to do.
  }

  public TransitEdge(
      String voyageNumber,
      String fromUnLocode,
      String toUnLocode,
      LocalDateTime fromDate,
      LocalDateTime toDate) {
    this.voyageNumber = voyageNumber;
    this.fromUnLocode = fromUnLocode;
    this.toUnLocode = toUnLocode;
    this.fromDate = fromDate;
    this.toDate = toDate;
  }

  public String getVoyageNumber() {
    return voyageNumber;
  }

  public void setVoyageNumber(String voyageNumber) {
    this.voyageNumber = voyageNumber;
  }

  public String getFromUnLocode() {
    return fromUnLocode;
  }

  public void setFromUnLocode(String fromUnLocode) {
    this.fromUnLocode = fromUnLocode;
  }

  public String getToUnLocode() {
    return toUnLocode;
  }

  public void setToUnLocode(String toUnLocode) {
    this.toUnLocode = toUnLocode;
  }

  public LocalDateTime getFromDate() {
    return fromDate;
  }

  public void setFromDate(LocalDateTime fromDate) {
    this.fromDate = fromDate;
  }

  public LocalDateTime getToDate() {
    return toDate;
  }

  public void setToDate(LocalDateTime toDate) {
    this.toDate = toDate;
  }

  @Override
  public String toString() {
    return "TransitEdge{"
        + "voyageNumber="
        + voyageNumber
        + ", fromUnLocode="
        + fromUnLocode
        + ", toUnLocode="
        + toUnLocode
        + ", fromDate="
        + fromDate
        + ", toDate="
        + toDate
        + '}';
  }
}
