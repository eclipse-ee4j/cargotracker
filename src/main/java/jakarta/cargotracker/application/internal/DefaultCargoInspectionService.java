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
package jakarta.cargotracker.application.internal;

import jakarta.cargotracker.infrastructure.events.cdi.CargoInspected;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import jakarta.cargotracker.application.ApplicationEvents;
import jakarta.cargotracker.application.CargoInspectionService;
import jakarta.cargotracker.domain.model.cargo.Cargo;
import jakarta.cargotracker.domain.model.cargo.CargoRepository;
import jakarta.cargotracker.domain.model.cargo.TrackingId;
import jakarta.cargotracker.domain.model.handling.HandlingEventRepository;
import jakarta.cargotracker.domain.model.handling.HandlingHistory;

@Stateless
public class DefaultCargoInspectionService implements CargoInspectionService {

    @Inject
    private ApplicationEvents applicationEvents;
    @Inject
    private CargoRepository cargoRepository;
    @Inject
    private HandlingEventRepository handlingEventRepository;

    @Inject
    @CargoInspected
    private Event<Cargo> cargoInspected;

    private static final Logger logger = Logger.getLogger(
            DefaultCargoInspectionService.class.getName());

    @Override
    public void inspectCargo(TrackingId trackingId) {
        Cargo cargo = cargoRepository.find(trackingId);

        if (cargo == null) {
            logger.log(Level.WARNING, "Can't inspect non-existing cargo {0}", trackingId);
            return;
        }

        HandlingHistory handlingHistory = handlingEventRepository
                .lookupHandlingHistoryOfCargo(trackingId);

        cargo.deriveDeliveryProgress(handlingHistory);

        if (cargo.getDelivery().isMisdirected()) {
            applicationEvents.cargoWasMisdirected(cargo);
        }

        if (cargo.getDelivery().isUnloadedAtDestination()) {
            applicationEvents.cargoHasArrived(cargo);
        }

        cargoRepository.store(cargo);

        cargoInspected.fire(cargo);
    }
}
