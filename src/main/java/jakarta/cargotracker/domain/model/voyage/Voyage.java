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
package jakarta.cargotracker.domain.model.voyage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

import jakarta.cargotracker.domain.model.location.Location;

import org.apache.commons.lang3.Validate;

@Entity
@NamedQueries({
@NamedQuery(name = "Voyage.findByVoyageNumber", query = "Select v from Voyage v where v.voyageNumber = :voyageNumber"),
@NamedQuery(name = "Voyage.findAll", query = "Select v from Voyage v order by v.voyageNumber")})

public class Voyage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    @Embedded
    @NotNull
    private VoyageNumber voyageNumber;
    @Embedded
    @NotNull
    private Schedule schedule;
    // Null object pattern
    public static final Voyage NONE = new Voyage(new VoyageNumber(""),
            Schedule.EMPTY);

    public Voyage() {
        // Nothing to initialize
    }

    public Voyage(VoyageNumber voyageNumber, Schedule schedule) {
        Validate.notNull(voyageNumber, "Voyage number is required");
        Validate.notNull(schedule, "Schedule is required");

        this.voyageNumber = voyageNumber;
        this.schedule = schedule;
    }

    public VoyageNumber getVoyageNumber() {
        return voyageNumber;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    @Override
    public int hashCode() {
        return voyageNumber.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Voyage)) {
            return false;
        }

        Voyage that = (Voyage) o;

        return sameIdentityAs(that);
    }

    public boolean sameIdentityAs(Voyage other) {
        return other != null
                && this.getVoyageNumber().sameValueAs(other.getVoyageNumber());
    }

    @Override
    public String toString() {
        return "Voyage " + voyageNumber;
    }

    /**
     * Builder pattern is used for incremental construction of a Voyage
     * aggregate. This serves as an aggregate factory.
     */
    public static class Builder {

        private List<CarrierMovement> carrierMovements = new ArrayList<>();
        private VoyageNumber voyageNumber;
        private Location departureLocation;

        public Builder(VoyageNumber voyageNumber, Location departureLocation) {
            Validate.notNull(voyageNumber, "Voyage number is required");
            Validate.notNull(departureLocation,
                    "Departure location is required");

            this.voyageNumber = voyageNumber;
            this.departureLocation = departureLocation;
        }

        public Builder addMovement(Location arrivalLocation,
                Date departureTime, Date arrivalTime) {
            carrierMovements.add(new CarrierMovement(departureLocation,
                    arrivalLocation, departureTime, arrivalTime));

            // Next departure location is the same as this arrival location
            this.departureLocation = arrivalLocation;

            return this;
        }

        public Voyage build() {
            return new Voyage(voyageNumber, new Schedule(carrierMovements));
        }
    }
}
