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
package net.java.cargotracker.domain.model.voyage;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;

@Embeddable
public class VoyageNumber implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "voyage_number")
    @NotNull
    private String number;

    public VoyageNumber() {
        // Nothing to initialize.
    }

    public VoyageNumber(String number) {
        Validate.notNull(number);

        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof VoyageNumber)) {
            return false;
        }

        VoyageNumber other = (VoyageNumber) o;

        return sameValueAs(other);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    boolean sameValueAs(VoyageNumber other) {
        return other != null && this.number.equals(other.number);
    }

    @Override
    public String toString() {
        return number;
    }

    public String getIdString() {
        return number;
    }
}
