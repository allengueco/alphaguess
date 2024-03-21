package org.allengueco.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GuessesTest {
    Guesses guesses;

    @BeforeEach
    void setup() {
        guesses = new Guesses(Set.of(), Set.of());
    }

    @Test
    void guessBefore() {
        final String ANSWER = "CASE";
        final String GUESS = "BASE";

        guesses.addGuess(ANSWER, GUESS);

        assertIterableEquals(List.of(GUESS), guesses.after());
        assertIterableEquals(List.of(), guesses.before());
    }

    @Test
    void guessAfter() {
        final String ANSWER = "CASE";
        final String GUESS = "VASE";

        guesses.addGuess(ANSWER, GUESS);

        assertIterableEquals(List.of(GUESS), guesses.before());
        assertIterableEquals(List.of(), guesses.after());
    }

    @Test
    void guessCorrect() {
        final String ANSWER = "CORRECT";
        final String GUESS = "CORRECT";

        var result = guesses.addGuess(ANSWER, GUESS);

        assertEquals(Result.EQUAL, result);

        assertTrue(guesses.before().isEmpty());
        assertTrue(guesses.after().isEmpty());
    }

    @Test
    void guessCaseInsensitive() {
        final String ANSWER = "PACE";
        final String GUESS1 = "BLACK";
        final String GUESS2 = "blacK";

        Result g1 = guesses.addGuess(ANSWER, GUESS1);
        Result g2 = guesses.addGuess(ANSWER, GUESS2);

        assertEquals(Result.ADDED, g1);
        assertEquals(Result.ALREADY_GUESSED, g2);

        assertTrue(guesses.before().isEmpty());
        assertFalse(guesses.after().isEmpty());
    }

    @Test
    void guessBeforeOrder() {
        final String ANSWER = "CASE";

        guesses.addGuess(ANSWER, "BASE");
        guesses.addGuess(ANSWER, "BOOST");
        guesses.addGuess(ANSWER, "ACORN");

        assertIterableEquals(List.of("ACORN", "BASE", "BOOST"), guesses.after());
        assertTrue(guesses.before().isEmpty());
    }
}