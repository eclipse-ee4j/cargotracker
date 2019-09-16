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
package net.java.cargotracker.interfaces.booking.web;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import net.java.cargotracker.interfaces.booking.facade.BookingServiceFacade;
import net.java.cargotracker.interfaces.booking.facade.dto.CargoRoute;

/**
 * Handles listing cargo. Operates against a dedicated service facade, and could
 * easily be rewritten as a thick Swing client. Completely separated from the
 * domain layer, unlike the tracking user interface.
 * <p/>
 * In order to successfully keep the domain model shielded from user interface
 * considerations, this approach is generally preferred to the one taken in the
 * tracking controller. However, there is never any one perfect solution for all
 * situations, so we've chosen to demonstrate two polarized ways to build user
 * interfaces.
 */
@Named
@RequestScoped
public class ListCargo {

    private List<CargoRoute> cargos;
    private List<CargoRoute> routedCargos;
    private List<CargoRoute> claimedCargos;
    private List<CargoRoute> notRoutedCargos;
    private List<CargoRoute> routedUnclaimedCargos;

    @Inject
    private BookingServiceFacade bookingServiceFacade;

    public List<CargoRoute> getCargos() {
        return cargos;
    }

    @PostConstruct
    public void init() {
        cargos = bookingServiceFacade.listAllCargos();
    }

    public List<CargoRoute> getRoutedCargos() {
        routedCargos = new ArrayList<>();

        for (CargoRoute route : cargos) {
            if (route.isRouted()) {
                routedCargos.add(route);
            }
        }

        return routedCargos;
    }

    public List<CargoRoute> getRoutedUnclaimedCargos() {
        routedUnclaimedCargos = new ArrayList<>();
        for (CargoRoute route : cargos) {
            if (route.isRouted() && !route.isClaimed()) {
                routedUnclaimedCargos.add(route);
            }
        }

        return routedUnclaimedCargos;
    }

    public List<CargoRoute> getClaimedCargos() {
        claimedCargos = new ArrayList<>();

        for (CargoRoute route : cargos) {
            if (route.isClaimed()) {
                claimedCargos.add(route);
            }
        }

        return claimedCargos;
    }

    public List<CargoRoute> getNotRoutedCargos() {
        notRoutedCargos = new ArrayList<>();

        for (CargoRoute route : cargos) {
            if (!route.isRouted()) {
                notRoutedCargos.add(route);
            }
        }

        return notRoutedCargos;
    }

}
