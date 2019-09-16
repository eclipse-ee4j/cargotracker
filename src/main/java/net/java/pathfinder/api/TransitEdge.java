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
package net.java.pathfinder.api;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents an edge in a path through a graph, describing the route of a
 * cargo.
 */
public class TransitEdge implements Serializable {

    private String voyageNumber;
    private String fromUnLocode;
    private String toUnLocode;
    private Date fromDate;
    private Date toDate;

    public TransitEdge() {
        // Nothing to do.
    }

    public TransitEdge(String voyageNumber, String fromUnLocode,
            String toUnLocode, Date fromDate, Date toDate) {
        this.voyageNumber = voyageNumber;
        this.fromUnLocode = fromUnLocode;
        this.toUnLocode = toUnLocode;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public void setVoyageNumber(String voyageNumber) {
        this.voyageNumber = voyageNumber;
    }

    public String getFromUnLocode() {
        return fromUnLocode;
    }

    public void setFromUnLocode(String fromUnLocode) {
        this.fromUnLocode = fromUnLocode;
    }

    public String getToUnLocode() {
        return toUnLocode;
    }

    public void setToUnLocode(String toUnLocode) {
        this.toUnLocode = toUnLocode;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "TransitEdge{" + "voyageNumber=" + voyageNumber
                + ", fromUnLocode=" + fromUnLocode + ", toUnLocode="
                + toUnLocode + ", fromDate=" + fromDate
                + ", toDate=" + toDate + '}';
    }
}