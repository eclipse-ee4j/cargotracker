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
package net.java.cargotracker.domain.model.handling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.Validate;

public class HandlingHistory {

    private final List<HandlingEvent> handlingEvents;
    // Null object pattern.
    public static final HandlingHistory EMPTY = new HandlingHistory(
            Collections.<HandlingEvent>emptyList());

    public HandlingHistory(Collection<HandlingEvent> handlingEvents) {
        Validate.notNull(handlingEvents, "Handling events are required");

        this.handlingEvents = new ArrayList<>(handlingEvents);
    }

    public List<HandlingEvent> getAllHandlingEvents() {
        return handlingEvents;
    }

    /**
     * @return A distinct list (no duplicate registrations) of handling events,
     * ordered by completion time.
     */
    public List<HandlingEvent> getDistinctEventsByCompletionTime() {
        List<HandlingEvent> ordered = new ArrayList<>(new HashSet<>(
                handlingEvents));
        Collections.sort(ordered, BY_COMPLETION_TIME_COMPARATOR);

        return Collections.unmodifiableList(ordered);
    }

    /**
     * @return Most recently completed event, or null if the delivery history is
     * empty.
     */
    public HandlingEvent getMostRecentlyCompletedEvent() {
        List<HandlingEvent> distinctEvents = getDistinctEventsByCompletionTime();

        if (distinctEvents.isEmpty()) {
            return null;
        } else {
            return distinctEvents.get(distinctEvents.size() - 1);
        }
    }

    private boolean sameValueAs(HandlingHistory other) {
        return other != null
                && this.handlingEvents.equals(other.handlingEvents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HandlingHistory other = (HandlingHistory) o;
        return sameValueAs(other);
    }

    @Override
    public int hashCode() {
        return handlingEvents.hashCode();
    }

    private static final Comparator<HandlingEvent> BY_COMPLETION_TIME_COMPARATOR = new Comparator<HandlingEvent>() {
        @Override
        public int compare(HandlingEvent he1, HandlingEvent he2) {
            return he1.getCompletionTime().compareTo(he2.getCompletionTime());
        }
    };
}
