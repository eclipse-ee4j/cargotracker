package org.eclipse.cargotracker.interfaces;

/**
 * At the moment, coordinates are effectively a shared DTO in the interface layer. It may be
 * converted to a domain level concern at some point. *
 */
public record Coordinates(double latitude, double longitude) {}
