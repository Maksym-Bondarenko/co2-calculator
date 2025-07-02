package org.challange.cli;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentParserTest {
    // Nested just for better understanding of 2 possibilities to show them by testing
    @Nested
    class Positional {

        @Test
        void parsesThreeTokens() {
            CLIOptions opt = ArgumentParser.parse(
                    new String[]{"diesel-car-medium", "Hamburg", "Berlin"});

            assertEquals("diesel-car-medium", opt.carType());
            assertEquals("Hamburg",       opt.start());
            assertEquals("Berlin",        opt.destination());
        }
    }

    @Nested
    class FlagVariants {

        @Test
        void spaceSeparated_anyOrder() {
            CLIOptions opt = ArgumentParser.parse(new String[] {
                    "--end", "Berlin",
                    "--transportation-method", "diesel-car-medium",
                    "--start", "Hamburg"});

            assertEquals("Hamburg", opt.start());
        }

        @Test
        void equalsForm_mixAndMatch() {
            CLIOptions opt = ArgumentParser.parse(new String[] {
                    "--transportation-method=diesel-car-medium",
                    "--start=Hamburg",
                    "--end", "Berlin"});

            assertEquals("Berlin", opt.destination());
        }

        @Test
        void missingFlag_throws() {
            assertThrows(IllegalArgumentException.class,
                    () -> ArgumentParser.parse(
                            new String[]{"--transportation-method=diesel-car-medium", "--start=Hamburg"}));
        }
    }
}