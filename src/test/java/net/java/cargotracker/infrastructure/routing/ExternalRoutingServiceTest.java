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
package net.java.cargotracker.infrastructure.routing;

import java.util.Date;
import java.util.List;
import net.java.cargotracker.domain.model.cargo.Cargo;
import net.java.cargotracker.domain.model.cargo.Itinerary;
import net.java.cargotracker.domain.model.cargo.Leg;
import net.java.cargotracker.domain.model.cargo.RouteSpecification;
import net.java.cargotracker.domain.model.cargo.TrackingId;
import net.java.cargotracker.domain.model.location.Location;
import net.java.cargotracker.domain.model.location.SampleLocations;
import net.java.cargotracker.domain.model.voyage.VoyageRepository;

public class ExternalRoutingServiceTest {

    private ExternalRoutingService externalRoutingService;
    private VoyageRepository voyageRepository;

//    protected void setUp() throws Exception {
//        externalRoutingService = new ExternalRoutingService();
//        LocationRepository locationRepository = new LocationRepositoryInMem();
//        externalRoutingService.setLocationRepository(locationRepository);
//
//        voyageRepository = createMock(VoyageRepository.class);
//        externalRoutingService.setVoyageRepository(voyageRepository);
//
//        GraphTraversalService graphTraversalService = new GraphTraversalServiceImpl(new GraphDAO() {
//            public List<String> listLocations() {
//                return Arrays.asList(SampleLocations.TOKYO.getUnLocode().getIdString(), SampleLocations.STOCKHOLM.getUnLocode().getIdString(), SampleLocations.GOTHENBURG.getUnLocode().getIdString());
//            }
//
//            public void storeCarrierMovementId(String cmId, String from, String to) {
//            }
//        });
//        externalRoutingService.setGraphTraversalService(graphTraversalService);
//    }
    // TODO this test belongs in com.pathfinder
    public void testCalculatePossibleRoutes() {
        TrackingId trackingId = new TrackingId("ABC");
        RouteSpecification routeSpecification = new RouteSpecification(SampleLocations.HONGKONG, SampleLocations.HELSINKI, new Date());
        Cargo cargo = new Cargo(trackingId, routeSpecification);

//        expect(voyageRepository.find(isA(VoyageNumber.class))).andStubReturn(SampleVoyages.CM002);
//
//        replay(voyageRepository);

        List<Itinerary> candidates = externalRoutingService.fetchRoutesForSpecification(routeSpecification);
        org.junit.Assert.assertNotNull(candidates);

        for (Itinerary itinerary : candidates) {
            List<Leg> legs = itinerary.getLegs();
            org.junit.Assert.assertNotNull(legs);
            org.junit.Assert.assertFalse(legs.isEmpty());

            // Cargo origin and start of first leg should match
            org.junit.Assert.assertEquals(cargo.getOrigin(), legs.get(0).getLoadLocation());

            // Cargo final destination and last leg stop should match
            Location lastLegStop = legs.get(legs.size() - 1).getUnloadLocation();
            org.junit.Assert.assertEquals(cargo.getRouteSpecification().getDestination(), lastLegStop);

            for (int i = 0; i < legs.size() - 1; i++) {
                // Assert that all legs are connected
                org.junit.Assert.assertEquals(legs.get(i).getUnloadLocation(), legs.get(i + 1).getLoadLocation());
            }
        }

//        verify(voyageRepository);
    }
}
