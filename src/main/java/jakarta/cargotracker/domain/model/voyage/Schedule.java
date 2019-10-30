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
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.eclipse.persistence.annotations.PrivateOwned;

/**
 * A voyage schedule.
 */
@Embeddable
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;
    // Null object pattern.
    public static final Schedule EMPTY = new Schedule();
    // TODO Look into why cascade delete doesn't work.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "voyage_id")
    // TODO Index as cm_index
    @PrivateOwned
    @NotNull
    @Size(min = 1)
    private List<CarrierMovement> carrierMovements = Collections.emptyList();

    public Schedule() {
        // Nothing to initialize.
    }

    Schedule(List<CarrierMovement> carrierMovements) {
        Validate.notNull(carrierMovements);
        Validate.noNullElements(carrierMovements);
        Validate.notEmpty(carrierMovements);

        this.carrierMovements = carrierMovements;
    }

    public List<CarrierMovement> getCarrierMovements() {
        return Collections.unmodifiableList(carrierMovements);
    }

    private boolean sameValueAs(Schedule other) {
        return other != null
                && this.carrierMovements.equals(other.carrierMovements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Schedule that = (Schedule) o;

        return sameValueAs(that);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.carrierMovements).toHashCode();
    }
}
