package org.eclipse.cargotracker.interfaces.booking.facade.dto;

import java.util.List;

public record CargoStatus(
    String trackingId,
    String destination,
    String statusText,
    boolean misdirected,
    String eta,
    String nextExpectedActivity,
    List<TrackingEvents> events) {}
