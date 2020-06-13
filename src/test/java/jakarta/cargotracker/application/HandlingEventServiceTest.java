package jakarta.cargotracker.application;

import jakarta.cargotracker.application.internal.DefaultHandlingEventService;
import jakarta.cargotracker.domain.model.cargo.Cargo;
import jakarta.cargotracker.domain.model.cargo.CargoRepository;
import jakarta.cargotracker.domain.model.cargo.RouteSpecification;
import jakarta.cargotracker.domain.model.cargo.TrackingId;
import jakarta.cargotracker.domain.model.handling.HandlingEvent;
import jakarta.cargotracker.domain.model.handling.HandlingEventRepository;
import jakarta.cargotracker.domain.model.location.LocationRepository;
import jakarta.cargotracker.domain.model.location.SampleLocations;
import jakarta.cargotracker.domain.model.voyage.SampleVoyages;
import jakarta.cargotracker.domain.model.voyage.VoyageRepository;

import java.util.Date;

public class HandlingEventServiceTest {

    private DefaultHandlingEventService service;
    private ApplicationEvents applicationEvents;
    private CargoRepository cargoRepository;
    private VoyageRepository voyageRepository;
    private HandlingEventRepository handlingEventRepository;
    private LocationRepository locationRepository;
    private Cargo cargo = new Cargo(new TrackingId("ABC"),
            new RouteSpecification(SampleLocations.HAMBURG, SampleLocations.TOKYO,
                    new Date()));

    protected void setUp() throws Exception {
//        cargoRepository = createMock(CargoRepository.class);
//        voyageRepository = createMock(VoyageRepository.class);
//        handlingEventRepository = createMock(HandlingEventRepository.class);
//        locationRepository = createMock(LocationRepository.class);
//        applicationEvents = createMock(ApplicationEvents.class);
//        HandlingEventFactory handlingEventFactory = new HandlingEventFactory(
//                cargoRepository, voyageRepository, locationRepository);
//        service = new DefaultHandlingEventService(handlingEventRepository, applicationEvents, handlingEventFactory);
    }

    protected void tearDown() throws Exception {
//        verify(cargoRepository, voyageRepository, handlingEventRepository, applicationEvents);
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

        service.registerHandlingEvent(new Date(), cargo.getTrackingId(),
                SampleVoyages.CM001.getVoyageNumber(),
                SampleLocations.STOCKHOLM.getUnLocode(),
                HandlingEvent.Type.LOAD);
    }
}
