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
package jakarta.cargotracker.domain.model.cargo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import jakarta.cargotracker.domain.model.location.Location;
import jakarta.cargotracker.domain.model.voyage.Voyage;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Leg implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "voyage_id")
    @NotNull
    private Voyage voyage;
    @ManyToOne
    @JoinColumn(name = "load_location_id")
    @NotNull
    private Location loadLocation;
    @ManyToOne
    @JoinColumn(name = "unload_location_id")
    @NotNull
    private Location unloadLocation;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "load_time")
    @NotNull
    private Date loadTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "unload_time")
    @NotNull
    private Date unloadTime;

    public Leg() {
        // Nothing to initialize.
    }

    public Leg(Voyage voyage, Location loadLocation, Location unloadLocation,
            Date loadTime, Date unloadTime) {
        Validate.noNullElements(new Object[]{voyage, loadLocation,
            unloadLocation, loadTime, unloadTime});

        this.voyage = voyage;
        this.loadLocation = loadLocation;
        this.unloadLocation = unloadLocation;
        this.loadTime = loadTime;
        this.unloadTime = unloadTime;
    }

    public Voyage getVoyage() {
        return voyage;
    }

    public Location getLoadLocation() {
        return loadLocation;
    }

    public Location getUnloadLocation() {
        return unloadLocation;
    }

    public Date getLoadTime() {
        return new Date(loadTime.getTime());
    }

    public Date getUnloadTime() {
        return new Date(unloadTime.getTime());
    }

    private boolean sameValueAs(Leg other) {
        return other != null
                && new EqualsBuilder().append(this.voyage, other.voyage)
                .append(this.loadLocation, other.loadLocation)
                .append(this.unloadLocation, other.unloadLocation)
                .append(this.loadTime, other.loadTime)
                .append(this.unloadTime, other.unloadTime).isEquals();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Leg leg = (Leg) o;

        return sameValueAs(leg);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(voyage).append(loadLocation)
                .append(unloadLocation).append(loadTime).append(unloadTime)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Leg{" + "id=" + id + ", voyage=" + voyage + ", loadLocation="
                + loadLocation + ", unloadLocation=" + unloadLocation
                + ", loadTime=" + loadTime + ", unloadTime=" + unloadTime + '}';
    }
}
