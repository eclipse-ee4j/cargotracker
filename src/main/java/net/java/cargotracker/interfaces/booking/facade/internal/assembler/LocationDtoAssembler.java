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
package net.java.cargotracker.interfaces.booking.facade.internal.assembler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.java.cargotracker.domain.model.location.Location;

public class LocationDtoAssembler {

    public net.java.cargotracker.interfaces.booking.facade.dto.Location toDto(
            Location location) {
        return new net.java.cargotracker.interfaces.booking.facade.dto.Location(
                location.getUnLocode().getIdString(), location.getName());
    }

    public List<net.java.cargotracker.interfaces.booking.facade.dto.Location> toDtoList(
            List<Location> allLocations) {
        List<net.java.cargotracker.interfaces.booking.facade.dto.Location> dtoList = new ArrayList<>(
                allLocations.size());

        for (Location location : allLocations) {
            dtoList.add(toDto(location));
        }

        Collections.sort(
                dtoList,
                new Comparator<net.java.cargotracker.interfaces.booking.facade.dto.Location>() {

                    @Override
                    public int compare(
                            net.java.cargotracker.interfaces.booking.facade.dto.Location location1,
                            net.java.cargotracker.interfaces.booking.facade.dto.Location location2) {
                                return location1.getName().compareTo(location2.getName());
                            }
                });

        return dtoList;
    }
}
