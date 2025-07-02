package org.challange.cli;

/**
 * Record to store CLI options to run the program with (order is not important, naming and parameters are)
 *
 * @param start start-city (must be a valid string)
 * @param destination destination-city (must be a valid string)
 * @param carType (valid vehicle type from the {@link org.challange.core.EmissionsConstants})
 */
public record CLIOptions(
   String carType,
   String start,
   String destination
) {}