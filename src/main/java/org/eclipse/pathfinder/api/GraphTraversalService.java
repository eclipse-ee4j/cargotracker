package org.eclipse.pathfinder.api;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.eclipse.pathfinder.internal.GraphDao;

@Stateless
@Path("/graph-traversal")
public class GraphTraversalService {

  private static final long ONE_MIN_MS = 1000 * 60;
  private static final long ONE_DAY_MS = ONE_MIN_MS * 60 * 24;
  private final Random random = new Random();
  @Inject private GraphDao dao;

  @GET
  @Path("/shortest-path")
  @Produces({"application/json", "application/xml; qs=.75"})
  public List<TransitPath> findShortestPath(
      @NotBlank(message = "Missing origin UN location code.")
          @Size(
              min = 5,
              max = 5,
              message = "Origin UN location code value must be five characters long.")
          @QueryParam("origin")
          String originUnLocode,
      @NotBlank(message = "Missing destination UN location code.")
          @Size(
              min = 5,
              max = 5,
              message = "Destination UN location code value must be five characters long.")
          @QueryParam("destination")
          String destinationUnLocode,
      // TODO [DDD] Apply regular expression validation.
      @Size(min = 8, max = 8, message = "Deadline value must be eight characters long.")
          @QueryParam("deadline")
          String deadline) {
    LocalDateTime date = nextDate(LocalDateTime.now());

    List<String> allVertices = dao.listLocations();
    allVertices.remove(originUnLocode);
    allVertices.remove(destinationUnLocode);

    int candidateCount = getRandomNumberOfCandidates();
    List<TransitPath> candidates = new ArrayList<>(candidateCount);

    for (int i = 0; i < candidateCount; i++) {
      allVertices = getRandomChunkOfLocations(allVertices);
      List<TransitEdge> transitEdges = new ArrayList<>(allVertices.size() - 1);
      String firstLegTo = allVertices.get(0);

      LocalDateTime fromDate = nextDate(date);
      LocalDateTime toDate = nextDate(fromDate);
      date = nextDate(toDate);

      transitEdges.add(
          new TransitEdge(
              dao.getVoyageNumber(originUnLocode, firstLegTo),
              originUnLocode,
              firstLegTo,
              fromDate,
              toDate));

      for (int j = 0; j < allVertices.size() - 1; j++) {
        String current = allVertices.get(j);
        String next = allVertices.get(j + 1);
        fromDate = nextDate(date);
        toDate = nextDate(fromDate);
        date = nextDate(toDate);
        transitEdges.add(
            new TransitEdge(dao.getVoyageNumber(current, next), current, next, fromDate, toDate));
      }

      String lastLegFrom = allVertices.get(allVertices.size() - 1);
      fromDate = nextDate(date);
      toDate = nextDate(fromDate);
      transitEdges.add(
          new TransitEdge(
              dao.getVoyageNumber(lastLegFrom, destinationUnLocode),
              lastLegFrom,
              destinationUnLocode,
              fromDate,
              toDate));

      candidates.add(new TransitPath(transitEdges));
    }

    return candidates;
  }

  private LocalDateTime nextDate(LocalDateTime date) {
    return date.plus(ONE_DAY_MS + (random.nextInt(1000) - 500) * ONE_MIN_MS, ChronoUnit.MILLIS);
  }

  private int getRandomNumberOfCandidates() {
    return 3 + random.nextInt(3);
  }

  private List<String> getRandomChunkOfLocations(List<String> allLocations) {
    Collections.shuffle(allLocations);
    int total = allLocations.size();
    int chunk = total > 4 ? 1 + new Random().nextInt(5) : total;
    return allLocations.subList(0, chunk);
  }
}
