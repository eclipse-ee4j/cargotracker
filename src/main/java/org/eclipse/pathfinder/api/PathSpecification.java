package org.eclipse.pathfinder.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.ws.rs.QueryParam;

public class PathSpecification {

  @NotBlank(message = "Missing origin UN location code.")
  @Size(min = 5, max = 5, message = "Origin UN location code value must be five characters long.")
  @QueryParam("origin")
  private String originUnLocode;

  @NotBlank(message = "Missing destination UN location code.")
  @Size(
      min = 5,
      max = 5,
      message = "Destination UN location code value must be five characters long.")
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
}
