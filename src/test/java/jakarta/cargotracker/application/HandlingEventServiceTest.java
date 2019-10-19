/*
    The MIT License
    
    Copyright (c) 2019 Oracle and/or its affiliates
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
*/
package jakarta.cargotracker.application;

import jakarta.cargotracker.application.ApplicationEvents;
import java.util.Date;
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
