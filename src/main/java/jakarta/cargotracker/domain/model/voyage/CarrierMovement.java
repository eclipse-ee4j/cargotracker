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
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import jakarta.cargotracker.domain.model.location.Location;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A carrier movement is a vessel voyage from one location to another.
 */
@Entity
@Table(name = "carrier_movement")
public class CarrierMovement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "departure_location_id")
    @NotNull
    private Location departureLocation;
    @ManyToOne
    @JoinColumn(name = "arrival_location_id")
    @NotNull
    private Location arrivalLocation;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "departure_time")
    @NotNull
    private Date departureTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "arrival_time")
    @NotNull
    private Date arrivalTime;
    // Null object pattern
    public static final CarrierMovement NONE = new CarrierMovement(
            Location.UNKNOWN, Location.UNKNOWN, new Date(0), new Date(0));

    public CarrierMovement() {
        // Nothing to initialize.
    }

    public CarrierMovement(Location departureLocation,
            Location arrivalLocation, Date departureTime, Date arrivalTime) {
        Validate.noNullElements(new Object[]{departureLocation,
            arrivalLocation, departureTime, arrivalTime});
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
    }

    public Location getDepartureLocation() {
        return departureLocation;
    }

    public Location getArrivalLocation() {
        return arrivalLocation;
    }

    public Date getDepartureTime() {
        return new Date(departureTime.getTime());
    }

    public Date getArrivalTime() {
        return new Date(arrivalTime.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CarrierMovement that = (CarrierMovement) o;

        return sameValueAs(that);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.departureLocation)
                .append(this.departureTime).append(this.arrivalLocation)
                .append(this.arrivalTime).toHashCode();
    }

    private boolean sameValueAs(CarrierMovement other) {
        return other != null
                && new EqualsBuilder()
                .append(this.departureLocation, other.departureLocation)
                .append(this.departureTime, other.departureTime)
                .append(this.arrivalLocation, other.arrivalLocation)
                .append(this.arrivalTime, other.arrivalTime).isEquals();
    }
}
