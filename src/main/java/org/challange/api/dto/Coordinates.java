package org.challange.api.dto;

/**
 * Simple DTO for city's coordinates: longitude and latitude (for easier usage)
 * @param latitude
 * @param longitude
 */
public record Coordinates(double latitude, double longitude) {}