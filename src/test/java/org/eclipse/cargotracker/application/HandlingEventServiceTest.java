package org.eclipse.cargotracker.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.eclipse.cargotracker.application.internal.DefaultHandlingEventService;
import org.eclipse.cargotracker.domain.model.cargo.Cargo;
import org.eclipse.cargotracker.domain.model.cargo.RouteSpecification;
import org.eclipse.cargotracker.domain.model.cargo.TrackingId;
import org.eclipse.cargotracker.domain.model.handling.HandlingEvent;
import org.eclipse.cargotracker.domain.model.location.SampleLocations;
import org.eclipse.cargotracker.domain.model.voyage.SampleVoyages;

public class HandlingEventServiceTest {

  private DefaultHandlingEventService service;
  //	private ApplicationEvents applicationEvents;
  //	private CargoRepository cargoRepository;
  //	private VoyageRepository voyageRepository;
  //	private HandlingEventRepository handlingEventRepository;
  //	private LocationRepository locationRepository;
  private Cargo cargo =
      new Cargo(
          new TrackingId("ABC"),
          new RouteSpecification(SampleLocations.HAMBURG, SampleLocations.TOKYO, LocalDate.now()));

  protected void setUp() throws Exception {
    //        cargoRepository = createMock(CargoRepository.class);
    //        voyageRepository = createMock(VoyageRepository.class);
    //        handlingEventRepository = createMock(HandlingEventRepository.class);
    //        locationRepository = createMock(LocationRepository.class);
    //        applicationEvents = createMock(ApplicationEvents.class);
    //        HandlingEventFactory handlingEventFactory = new HandlingEventFactory(
    //                cargoRepository, voyageRepository, locationRepository);
    //        service = new DefaultHandlingEventService(handlingEventRepository,
    // applicationEvents,
    // handlingEventFactory);
  }

  protected void tearDown() throws Exception {
    //        verify(cargoRepository, voyageRepository, handlingEventRepository,
    // applicationEvents);
  }

  public void testRegisterEvent() throws Exception {
    //        expect(cargoRepository.find(cargo.getTrackingId())).andReturn(cargo);
    //        expect(voyageRepository.find(SampleVoyages.CM001.getVoyageNumber()))
    //                .andReturn(SampleVoyages.CM001);
    //        expect(locationRepository.find(SampleLocations.STOCKHOLM.getUnLocode()))
    //                .andReturn(SampleLocations.STOCKHOLM);
    //        handlingEventRepository.store(isA(HandlingEvent.class));
    //        applicationEvents.cargoWasHandled(isA(HandlingEvent.class));

    //        replay(cargoRepository, voyageRepository, handlingEventRepository,
    //                locationRepository, applicationEvents);

    service.registerHandlingEvent(
        LocalDateTime.now(),
        cargo.getTrackingId(),
        SampleVoyages.CM001.getVoyageNumber(),
        SampleLocations.STOCKHOLM.getUnLocode(),
        HandlingEvent.Type.LOAD);
  }
}
