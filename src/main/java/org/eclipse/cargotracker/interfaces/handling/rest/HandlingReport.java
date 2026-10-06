package org.eclipse.cargotracker.interfaces.handling.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Transfer object for handling reports. */
public record HandlingReport(
    @NotBlank(message = "Missing completion time.")
        @Size(
            min = 15,
            max = 19,
            message = "Completion time value must be between fifteen and nineteen characters long.")
        String completionTime,
    @NotBlank(message = "Missing tracking ID.")
        @Size(min = 4, message = "Tracking ID must be at least four characters.")
        String trackingId,
    @NotBlank(message = "Missing event type.")
        @Pattern(
            regexp = "RECEIVE|LOAD|UNLOAD|CUSTOMS|CLAIM",
            message = "Event type value must be one of: RECEIVE, LOAD, UNLOAD, CUSTOMS, CLAIM")
        String eventType,
    @NotBlank(message = "UN location code missing.")
        @Size(min = 5, max = 5, message = "UN location code must be five characters long.")
        String unLocode,
    @Size(
            min = 4,
            max = 5,
            message = "Voyage number value must be between four and five characters long.")
        String voyageNumber) {}
