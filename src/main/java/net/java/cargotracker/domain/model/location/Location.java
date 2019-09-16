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
package net.java.cargotracker.domain.model.location;

import java.io.Serializable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.Validate;

/**
 * A location in our model is stops on a journey, such as cargo origin or
 * destination, or carrier movement end points.
 *
 * It is uniquely identified by a UN location code.
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Location.findAll", query = "Select l from Location l"),
    @NamedQuery(name = "Location.findByUnLocode",
            query = "Select l from Location l where l.unLocode = :unLocode")})
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    @Embedded
    private UnLocode unLocode;
    @NotNull
    private String name;
    // Special Location object that marks an unknown location.
    public static final Location UNKNOWN = new Location(new UnLocode("XXXXX"),
            "Unknown location");

    public Location() {
        // Nothing to do.
    }

    /**
     * @param unLocode UN Locode
     * @param name Location name
     * @throws IllegalArgumentException if the UN Locode or name is null
     */
    public Location(UnLocode unLocode, String name) {
        Validate.notNull(unLocode);
        Validate.notNull(name);

        this.unLocode = unLocode;
        this.name = name;
    }

    /**
     * @return UN Locode for this location.
     */
    public UnLocode getUnLocode() {
        return unLocode;
    }

    /**
     * @return Actual name of this location, e.g. "Stockholm".
     */
    public String getName() {
        return name;
    }

    /**
     * @param object to compare
     * @return Since this is an entiy this will be true iff UN locodes are
     * equal.
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        return sameIdentityAs(other);
    }

    public boolean sameIdentityAs(Location other) {
        return this.unLocode.sameValueAs(other.unLocode);
    }

    /**
     * @return Hash code of UN locode.
     */
    @Override
    public int hashCode() {
        return unLocode.hashCode();
    }

    @Override
    public String toString() {
        return name + " [" + unLocode + "]";
    }
}